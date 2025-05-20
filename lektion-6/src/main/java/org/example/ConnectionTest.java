package org.example;


import java.sql.Connection;

public class ConnectionTest {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Connection successful!");
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
