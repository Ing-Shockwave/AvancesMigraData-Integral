package com.mycompany.migradataperu.importador;

import com.mycompany.migradataperu.conexion.ConexionBD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ImportadorCSV {

    public void importarSolicitudCalidad(String rutaArchivo) {

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
            int contador = 0;

            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|", -1);

                if (datos.length < 13) {
                    continue;
                }

                ps.setString(1, datos[0]);
                ps.setString(2, datos[1]);
                ps.setString(3, datos[2]);
                ps.setString(4, datos[3]);
                ps.setString(5, datos[4]);
                ps.setString(6, datos[5]);
                ps.setString(7, datos[6]);
                ps.setString(8, datos[7]);
                ps.setString(9, datos[8]);
                ps.setInt(10, convertirEntero(datos[9]));
                ps.setString(11, datos[10]);
                ps.setInt(12, convertirEntero(datos[11]));
                ps.setString(13, datos[12]);

                ps.addBatch();
                contador++;
            }

            ps.executeBatch();

            System.out.println("Solicitud calidad migratoria importada: " + contador + " registros");

        } catch (Exception e) {
            System.out.println("Error importando solicitud calidad migratoria: " + e.getMessage());
        }
    }

    public void importarCambioCalidad(String rutaArchivo) {

        String sql = "INSERT INTO cambio_calidad_migratoria "
                + "(sede_atencion, distrito_atencion, provincia_atencion, departamento_atencion, "
                + "calidad_migratoria, sexo, edad, provincia_beneficiario, departamento_beneficiario, "
                + "nacionalidad, estado_civil, anio_tramite, mes_tramite, cantidad, fecha_proceso) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConexionBD.conectar();
                BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String linea;
            boolean primeraLinea = true;
            int contador = 0;

            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|", -1);

                if (datos.length < 15) {
                    continue;
                }

                ps.setString(1, datos[0]);
                ps.setString(2, datos[1]);
                ps.setString(3, datos[2]);
                ps.setString(4, datos[3]);
                ps.setString(5, datos[4]);
                ps.setString(6, datos[5]);
                ps.setString(7, datos[6]);
                ps.setString(8, datos[7]);
                ps.setString(9, datos[8]);
                ps.setString(10, datos[9]);
                ps.setString(11, datos[10]);
                ps.setInt(12, convertirEntero(datos[11]));
                ps.setString(13, datos[12]);
                ps.setInt(14, convertirEntero(datos[13]));
                ps.setString(15, datos[14]);

                ps.addBatch();
                contador++;
            }

            ps.executeBatch();

            System.out.println("Cambio calidad migratoria importada: " + contador + " registros");

        } catch (Exception e) {
            System.out.println("Error importando cambio calidad migratoria: " + e.getMessage());
        }
    }

    public void importarCarnetExtranjeria(String rutaArchivo) {

        String sql = "INSERT INTO carnet_extranjeria "
                + "(sede_atencion, distrito_atencion, provincia_atencion, departamento_atencion, "
                + "sexo, edad, provincia_beneficiario, departamento_beneficiario, nacionalidad, "
                + "anio_tramite, mes_tramite, tipo_tramite, cantidad, fecha_proceso) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConexionBD.conectar();
                BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            String linea;
            boolean primeraLinea = true;
            int contador = 0;

            while ((linea = br.readLine()) != null) {

                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|", -1);

                if (datos.length < 14) {
                    continue;
                }

                ps.setString(1, datos[0]);
                ps.setString(2, datos[1]);
                ps.setString(3, datos[2]);
                ps.setString(4, datos[3]);
                ps.setString(5, datos[4]);
                ps.setString(6, datos[5]);
                ps.setString(7, datos[6]);
                ps.setString(8, datos[7]);
                ps.setString(9, datos[8]);
                ps.setInt(10, convertirEntero(datos[9]));
                ps.setString(11, datos[10]);
                ps.setString(12, datos[11]);
                ps.setInt(13, convertirEntero(datos[12]));
                ps.setString(14, datos[13]);

                ps.addBatch();
                contador++;
            }

            ps.executeBatch();

            System.out.println("Carnet extranjería importada: " + contador + " registros");

        } catch (Exception e) {
            System.out.println("Error importando carnet extranjería: " + e.getMessage());
        }
    }

    private int convertirEntero(String valor) {

        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {

        ImportadorCSV importador = new ImportadorCSV();

        importador.importarSolicitudCalidad(
                "C:\\Users\\diego\\OneDrive\\Desktop\\datasets\\SOLICITUD DE CALIDAD MIGRATORIA (VISAS) 2025-08.csv"
        );

        importador.importarCambioCalidad(
                "C:\\Users\\diego\\OneDrive\\Desktop\\datasets\\CAMBIO DE CALIDAD MIGRATORIA 2025-08.csv"
        );

        importador.importarCarnetExtranjeria(
                "C:\\Users\\diego\\OneDrive\\Desktop\\datasets\\CARNET DE EXTRANJERIA 2025-08.csv"
        );
    }
}