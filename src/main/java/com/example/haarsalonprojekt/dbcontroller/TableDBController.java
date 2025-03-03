package com.example.haarsalonprojekt.dbcontroller;

import com.example.haarsalonprojekt.entity.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;

public class TableDBController {

    public ObservableList<Table> gettingTable() {
        ObservableList<Table> list = FXCollections.observableArrayList();
        String query = "SELECT a.customer_name, a.customer_phone, a.customer_gender, t.name AS treatment_name, " +
                "(t.standard_price + a.extra_cost) AS treatment_price, " +
                "(t.standard_duration + a.extra_time) AS treatment_duration, " +
                "a.appointment_datetime, e.full_name AS employee_name, a.status " +
                "FROM Appointment a " +
                "JOIN Treatment t ON a.treatment_id = t.id " +
                "JOIN Employee e ON a.employee_id = e.id " +
                "WHERE a.status = 'open'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String customer_name = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerGender = rs.getString("customer_gender");
                String treatmentName = rs.getString("treatment_name");
                Double treatmentPrice = rs.getDouble("treatment_price");
                int treatmentDuration = rs.getInt("treatment_duration");
                Timestamp timestamp = rs.getTimestamp("appointment_datetime");
                LocalDateTime appointmentDatetime = timestamp.toLocalDateTime();
                String employeeName = rs.getString("employee_name");
                String status = rs.getString("status");
                Table table = new Table(customer_name, customerPhone, customerGender, treatmentName, treatmentPrice,
                        treatmentDuration, appointmentDatetime, employeeName, status);
                list.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<Table> gettingOtherTable() {
        ObservableList<Table> list = FXCollections.observableArrayList();
        String query = "SELECT a.customer_name, a.customer_phone, a.customer_gender, t.name AS treatment_name, " +
                "t.standard_price AS treatment_price, t.standard_duration AS treatment_duration, " +
                "a.appointment_datetime, e.full_name AS employee_name, a.status " +
                "FROM Appointment a " +
                "JOIN Treatment t ON a.treatment_id = t.id " +
                "JOIN Employee e ON a.employee_id = e.id " +
                "WHERE a.status = 'closed' OR a.status = 'cancelled'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String customer_name = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerGender = rs.getString("customer_gender");
                String treatmentName = rs.getString("treatment_name");
                Double treatmentPrice = rs.getDouble("treatment_price");
                int treatmentDuration = rs.getInt("treatment_duration");
                Timestamp timestamp = rs.getTimestamp("appointment_datetime");
                LocalDateTime appointmentDatetime = timestamp.toLocalDateTime();
                String employeeName = rs.getString("employee_name");
                String status = rs.getString("status");
                Table table = new Table(customer_name, customerPhone, customerGender, treatmentName, treatmentPrice,
                        treatmentDuration, appointmentDatetime, employeeName, status);
                list.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateAppointmentStatus(String customerName, String customerPhone, LocalDateTime appointmentDatetime, String employeeName, String newStatus) {
        String query = "UPDATE Appointment SET status = ? WHERE customer_name = ? AND customer_phone = ? AND appointment_datetime = ? AND employee_id = (SELECT id FROM Employee WHERE full_name = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setString(2, customerName);
            ps.setString(3, customerPhone);
            ps.setTimestamp(4, Timestamp.valueOf(appointmentDatetime));
            ps.setString(5, employeeName);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Appointment status updated for: " + customerName + " at " + appointmentDatetime);
            } else {
                System.out.println("No appointment updated. Check the data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
