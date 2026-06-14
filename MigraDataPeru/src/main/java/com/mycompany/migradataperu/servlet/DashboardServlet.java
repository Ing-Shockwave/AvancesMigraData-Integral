package com.mycompany.migradataperu.servlet;

import com.mycompany.migradataperu.dao.CambioDAO;
import com.mycompany.migradataperu.dao.CarnetDAO;
import com.mycompany.migradataperu.dao.SolicitudDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String nacionalidad = request.getParameter("nacionalidad");
        String sexo = request.getParameter("sexo");
        String anio = request.getParameter("anio");
        String mes = request.getParameter("mes");
        String edad = request.getParameter("edad");
        String estadoCivil = request.getParameter("estadoCivil");
        String estadoTramite = request.getParameter("estadoTramite");

        if (nacionalidad == null) nacionalidad = "";
        if (sexo == null) sexo = "";
        if (anio == null) anio = "";
        if (mes == null) mes = "";
        if (edad == null) edad = "";
        if (estadoCivil == null) estadoCivil = "";
        if (estadoTramite == null) estadoTramite = "";

        SolicitudDAO solicitudDAO = new SolicitudDAO();
        CambioDAO cambioDAO = new CambioDAO();
        CarnetDAO carnetDAO = new CarnetDAO();

        int totalSolicitudes = solicitudDAO.contarSolicitudes(
                nacionalidad,
                sexo,
                anio,
                mes,
                edad,
                estadoCivil
        );

        int totalNacionalidades = solicitudDAO.contarNacionalidades();
        int totalCambios = cambioDAO.contarCambios();
        int totalCarnets = carnetDAO.contarCarnets();

        Map<String, Integer> graficoNacionalidades =
                solicitudDAO.solicitudesPorNacionalidad(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        Map<String, Integer> graficoSexo =
                solicitudDAO.solicitudesPorSexo(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        Map<String, Integer> graficoDepartamento =
                solicitudDAO.solicitudesPorDepartamento(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );
        
        Map<String, Integer> graficoEstadoTramite =
        solicitudDAO.tramitesPorEstado();
        
        Map<String, Integer> mapaDepartamento =
        solicitudDAO.solicitudesMapaDepartamento(
                nacionalidad,
                sexo,
                anio,
                mes,
                edad,
                estadoCivil
        );
        
        Map<String, Integer> kpiEstadosTramite =
        solicitudDAO.contarEstadosTramite();

            int totalFinalizados = kpiEstadosTramite.getOrDefault("Finalizado", 0);
            int totalEnProceso = kpiEstadosTramite.getOrDefault("En proceso", 0);
        
        Map<String, Integer> graficoMes =
                solicitudDAO.solicitudesPorMes(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        List<Map<String, String>> tablaResultados =
                solicitudDAO.listarTablaResultados(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil,
                        estadoTramite
                );
        
        List<Map<String, String>> seguimientoEstimado =
                solicitudDAO.listarSeguimientoEstimado();

        List<String> listaNacionalidades =
                solicitudDAO.listarNacionalidades();

        List<String> listaSexos =
                solicitudDAO.listarSexos();

        List<String> listaAnios =
                solicitudDAO.listarAnios();

        List<String> listaMeses =
                solicitudDAO.listarMeses();

        List<String> listaEdades =
                solicitudDAO.listarEdades();

        List<String> listaEstadosCiviles =
                solicitudDAO.listarEstadosCiviles();

        String nacionalidadTop =
                solicitudDAO.nacionalidadTop(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        String departamentoTop =
                solicitudDAO.departamentoTop(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        String sexoTop =
                solicitudDAO.sexoTop(
                        nacionalidad,
                        sexo,
                        anio,
                        mes,
                        edad,
                        estadoCivil
                );

        request.setAttribute("totalSolicitudes", totalSolicitudes);
        request.setAttribute("totalNacionalidades", totalNacionalidades);
        request.setAttribute("totalCambios", totalCambios);
        request.setAttribute("totalCarnets", totalCarnets);

        request.setAttribute("graficoNacionalidades", graficoNacionalidades);
        request.setAttribute("graficoSexo", graficoSexo);
        request.setAttribute("graficoDepartamento", graficoDepartamento);
        request.setAttribute("graficoEstadoTramite", graficoEstadoTramite);
        request.setAttribute("totalFinalizados", totalFinalizados);
        request.setAttribute("totalEnProceso", totalEnProceso);
        request.setAttribute("mapaDepartamento", mapaDepartamento);
        request.setAttribute("graficoMes", graficoMes);

        request.setAttribute("tablaResultados", tablaResultados);
        request.setAttribute("seguimientoEstimado", seguimientoEstimado);

        request.setAttribute("listaNacionalidades", listaNacionalidades);
        request.setAttribute("listaSexos", listaSexos);
        request.setAttribute("listaAnios", listaAnios);
        request.setAttribute("listaMeses", listaMeses);
        request.setAttribute("listaEdades", listaEdades);
        request.setAttribute("listaEstadosCiviles", listaEstadosCiviles);

        request.setAttribute("nacionalidadSeleccionada", nacionalidad);
        request.setAttribute("sexoSeleccionado", sexo);
        request.setAttribute("anioSeleccionado", anio);
        request.setAttribute("mesSeleccionado", mes);
        request.setAttribute("edadSeleccionada", edad);
        request.setAttribute("estadoCivilSeleccionado", estadoCivil);
        request.setAttribute("estadoTramiteSeleccionado", estadoTramite);

        request.setAttribute("nacionalidadTop", nacionalidadTop);
        request.setAttribute("departamentoTop", departamentoTop);
        request.setAttribute("sexoTop", sexoTop);

        request.getRequestDispatcher("index.jsp")
               .forward(request, response);
    }
}
