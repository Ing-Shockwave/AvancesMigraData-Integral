package com.mycompany.migradataperu.dao;

import com.mycompany.migradataperu.conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CarnetDAO {

    public int contarCarnets() {

        int total = 0;

        String sql = "SELECT SUM(cantidad) FROM carnet_extranjeria";

        try (
                Connection con = ConexionBD.conectar();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return total;
    }
}