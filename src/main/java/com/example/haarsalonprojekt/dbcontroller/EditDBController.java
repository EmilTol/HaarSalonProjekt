package com.example.haarsalonprojekt.dbcontroller;

import com.example.haarsalonprojekt.entity.Edit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class EditDBController {

    public ObservableList<String> gettingHairPeople() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT full_name FROM Employee";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> gettingTreatments() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT name FROM Treatment";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Edit getTreatmentDetails(String treatmentName) {
        String query = "SELECT name, standard_duration, standard_price FROM Treatment WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, treatmentName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Edit(rs.getString("name"), rs.getInt("standard_duration"), rs.getDouble("standard_price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
