package com.mycompany.migradataperu.conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/migradata";

    private static final String USER = "root";

    private static final String PASSWORD = "admin";

    public static Connection conectar() {

        Connection con = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Conexión exitosa");

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());

        }

        return con;
    }

    public static void main(String[] args) {

        conectar();

    }
}