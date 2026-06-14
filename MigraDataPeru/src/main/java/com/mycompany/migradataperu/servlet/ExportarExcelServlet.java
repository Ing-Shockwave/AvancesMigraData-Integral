package com.mycompany.migradataperu.servlet;

import com.mycompany.migradataperu.conexion.ConexionBD;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/exportarExcel")
public class ExportarExcelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nacionalidad = limpiar(request.getParameter("nacionalidad"));
        String sexo = limpiar(request.getParameter("sexo"));
        String anio = limpiar(request.getParameter("anio"));
        String mes = limpiar(request.getParameter("mes"));
        String edad = limpiar(request.getParameter("edad"));
        String estadoCivil = limpiar(request.getParameter("estadoCivil"));
        String estadoTramite = limpiar(request.getParameter("estadoTramite"));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_migradata_filtrado.xlsx");

        String sql = "SELECT origen, nacionalidad, sexo, departamento_atencion, edad, estado_civil, "
                + "anio_tramite, mes_tramite, fecha_proceso, cantidad, "
                + "CASE "
                + "WHEN origen = 'SOLICITUD' THEN DATE_ADD(fecha_proceso, INTERVAL 30 DAY) "
                + "WHEN origen = 'CAMBIO' THEN DATE_ADD(fecha_proceso, INTERVAL 45 DAY) "
                + "WHEN origen = 'CARNET' THEN DATE_ADD(fecha_proceso, INTERVAL 15 DAY) "
                + "ELSE fecha_proceso "
                + "END AS fecha_estimada, "
                + "CASE "
                + "WHEN (CASE "
                + "WHEN origen = 'SOLICITUD' THEN DATE_ADD(fecha_proceso, INTERVAL 30 DAY) "
                + "WHEN origen = 'CAMBIO' THEN DATE_ADD(fecha_proceso, INTERVAL 45 DAY) "
                + "WHEN origen = 'CARNET' THEN DATE_ADD(fecha_proceso, INTERVAL 15 DAY) "
                + "ELSE fecha_proceso "
                + "END) <= CURDATE() THEN 'Finalizado' "
                + "ELSE 'En proceso' "
                + "END AS estado_tramite "
                + "FROM maestro_migracion "
                + "WHERE (? = '' OR nacionalidad = ?) "
                + "AND (? = '' OR sexo = ?) "
                + "AND (? = '' OR anio_tramite = ?) "
                + "AND (? = '' OR mes_tramite = ?) "
                + "AND (? = '' OR edad = ?) "
                + "AND (? = '' OR estado_civil = ?) "
                + "AND (? = '' OR CASE "
                + "WHEN (CASE "
                + "WHEN origen = 'SOLICITUD' THEN DATE_ADD(fecha_proceso, INTERVAL 30 DAY) "
                + "WHEN origen = 'CAMBIO' THEN DATE_ADD(fecha_proceso, INTERVAL 45 DAY) "
                + "WHEN origen = 'CARNET' THEN DATE_ADD(fecha_proceso, INTERVAL 15 DAY) "
                + "ELSE fecha_proceso "
                + "END) <= CURDATE() THEN 'Finalizado' "
                + "ELSE 'En proceso' "
                + "END = ?) "
                + "ORDER BY fecha_proceso DESC "
                + "LIMIT 1000";

        try (
                Workbook workbook = new XSSFWorkbook();
                Connection con = ConexionBD.conectar();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            asignarFiltros(ps, nacionalidad, sexo, anio, mes, edad, estadoCivil, estadoTramite);

            ResultSet rs = ps.executeQuery();

            Sheet sheet = workbook.createSheet("Reporte Filtrado");

            String[] columnas = {
                    "Origen",
                    "Nacionalidad",
                    "Sexo",
                    "Departamento",
                    "Edad",
                    "Estado Civil",
                    "Año",
                    "Mes",
                    "Fecha Proceso",
                    "Fecha Estimada",
                    "Estado Trámite",
                    "Cantidad"
            };

            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;

            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(rs.getString("origen"));
                row.createCell(1).setCellValue(rs.getString("nacionalidad"));
                row.createCell(2).setCellValue(rs.getString("sexo"));
                row.createCell(3).setCellValue(rs.getString("departamento_atencion"));
                row.createCell(4).setCellValue(rs.getString("edad"));
                row.createCell(5).setCellValue(rs.getString("estado_civil"));
                row.createCell(6).setCellValue(rs.getString("anio_tramite"));
                row.createCell(7).setCellValue(rs.getString("mes_tramite"));
                row.createCell(8).setCellValue(rs.getString("fecha_proceso"));
                row.createCell(9).setCellValue(rs.getString("fecha_estimada"));
                row.createCell(10).setCellValue(rs.getString("estado_tramite"));
                row.createCell(11).setCellValue(rs.getInt("cantidad"));
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().println("Error al exportar Excel filtrado: " + e.getMessage());
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void asignarFiltros(
            PreparedStatement ps,
            String nacionalidad,
            String sexo,
            String anio,
            String mes,
            String edad,
            String estadoCivil,
            String estadoTramite
    ) throws SQLException {

        ps.setString(1, nacionalidad);
        ps.setString(2, nacionalidad);

        ps.setString(3, sexo);
        ps.setString(4, sexo);

        ps.setString(5, anio);
        ps.setString(6, anio);

        ps.setString(7, mes);
        ps.setString(8, mes);

        ps.setString(9, edad);
        ps.setString(10, edad);

        ps.setString(11, estadoCivil);
        ps.setString(12, estadoCivil);

        ps.setString(13, estadoTramite);
        ps.setString(14, estadoTramite);
    }
}