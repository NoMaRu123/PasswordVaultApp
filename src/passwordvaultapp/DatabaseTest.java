/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

import java.sql.*;

/**
 *
 * @author ASUS
 */
public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:PasswordVault.db";
        
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connection to SQLite successfully!");
            }
        }catch(SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
        }
    }
}
