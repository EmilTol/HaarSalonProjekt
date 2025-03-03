package com.example.haarsalonprojekt.dbcontroller;

import com.example.haarsalonprojekt.entity.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDBController {
    private Connection connection;

    public LoginDBController() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.err.println("No connection established");
        }
    }

    public boolean validateLogin(Login login) {
        String query = "SELECT * FROM employee WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, login.getUsername());
            ps.setString(2, login.getPassword());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
