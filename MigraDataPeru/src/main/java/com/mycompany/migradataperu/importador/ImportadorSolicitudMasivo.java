package com.mycompany.migradataperu.importador;

import com.mycompany.migradataperu.conexion.ConexionBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

public class ImportadorSolicitudMasivo {

    public void importarCarpeta(String rutaCarpeta) {

        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("La carpeta no existe: " + rutaCarpeta);
            return;
        }

        File[] archivos = carpeta.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".csv")
                && name.toLowerCase().startsWith("solicitud")
        );

        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos CSV.");
            return;
        }

        Arrays.sort(archivos);

        int totalGeneral = 0;

        for (File archivo : archivos) {
            int totalArchivo = importarArchivo(archivo.getAbsolutePath());
            totalGeneral += totalArchivo;

            System.out.println("Archivo importado: " + archivo.getName()
                    + " | Registros: " + totalArchivo);
        }

        System.out.println("-----------------------------------");
        System.out.println("IMPORTACIÓN FINALIZADA");
        System.out.println("Total registros importados: " + totalGeneral);
    }

    private int importarArchivo(String rutaArchivo) {

        int contador = 0;

        String sql = "INSERT INTO solicitud_calidad_migratoria "
                + "(sede_atencion, distrito_atencion, provincia_atencion, departamento_atencion, "
                + "calidad_migratoria, sexo, edad, nacionalidad, estado_civil, "
                + "anio_tramite, mes_tramite, cantidad, fecha_proceso) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConexionBD.conectar();
                BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|", -1);

                if (datos.length < 13) {
                    continue;
                }

                ps.setString(1, limpiar(datos[0]));
                ps.setString(2, limpiar(datos[1]));
                ps.setString(3, limpiar(datos[2]));
                ps.setString(4, limpiar(datos[3]));
                ps.setString(5, limpiar(datos[4]));
                ps.setString(6, limpiar(datos[5]));
                ps.setString(7, limpiar(datos[6]));
                ps.setString(8, limpiar(datos[7]));
                ps.setString(9, limpiar(datos[8]));
                ps.setInt(10, convertirEntero(datos[9]));
                ps.setString(11, limpiar(datos[10]));
                ps.setInt(12, convertirEntero(datos[11]));
                ps.setString(13, limpiar(datos[12]));

                ps.addBatch();
                contador++;

                if (contador % 500 == 0) {
                    ps.executeBatch();
                }
            }

            ps.executeBatch();

        } catch (Exception e) {
            System.out.println("Error importando archivo: " + rutaArchivo);
            System.out.println("Detalle: " + e.getMessage());
        }

        return contador;
    }

    private String limpiar(String valor) {

        if (valor == null) {
            return "";
        }

        return valor.trim();
    }

    private int convertirEntero(String valor) {

        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {

        ImportadorSolicitudMasivo importador = new ImportadorSolicitudMasivo();

        importador.importarCarpeta(
                "C:\\Users\\diego\\OneDrive\\Desktop\\datasets"
        );
    }
}