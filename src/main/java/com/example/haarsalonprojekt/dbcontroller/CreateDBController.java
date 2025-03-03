package com.example.haarsalonprojekt.dbcontroller;

import com.example.haarsalonprojekt.entity.Create;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;

public class CreateDBController {

    public boolean insertAppointment(Create appointment) {
        int treatmentDuration = getTreatmentDurationById(appointment.getTreatmentId());
        if (treatmentDuration == -1) {
            System.out.println("Treatment duration not found.");
            return false;
        }
        boolean isAvailable = isTimeSlotAvailable(appointment.getEmployeeId(), appointment.getAppointmentDatetime(), treatmentDuration + appointment.getExtraTime());
        if (!isAvailable) {
            System.out.println("Time slot is not available for the employee.");
            return false;
        }
        String query = "INSERT INTO Appointment (customer_name, customer_phone, customer_gender, treatment_id, appointment_datetime, employee_id, status, extra_time, extra_cost) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, appointment.getCustomerName());
            ps.setString(2, appointment.getCustomerPhone());
            ps.setString(3, appointment.getCustomerGender());
            ps.setInt(4, appointment.getTreatmentId());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getAppointmentDatetime()));
            ps.setInt(6, appointment.getEmployeeId());
            ps.setString(7, appointment.getStatus());
            ps.setInt(8, appointment.getExtraTime());
            ps.setDouble(9, appointment.getExtraCost());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTimeSlotAvailable(int employeeId, LocalDateTime newAppointmentStart, int duration) {
        String query = "SELECT COUNT(*) FROM Appointment " +
                "WHERE employee_id = ? " +
                "AND status = 'open' " +
                "AND appointment_datetime < ? + INTERVAL ? MINUTE " +
                "AND appointment_datetime + INTERVAL (SELECT standard_duration + extra_time FROM Treatment WHERE id = Appointment.treatment_id) MINUTE > ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            ps.setTimestamp(2, Timestamp.valueOf(newAppointmentStart));
            ps.setInt(3, duration);
            ps.setTimestamp(4, Timestamp.valueOf(newAppointmentStart));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int getTreatmentDurationById(int treatmentId) {
        String query = "SELECT standard_duration FROM Treatment WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, treatmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("standard_duration");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ObservableList<String> getTreatmentNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT name FROM Treatment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ObservableList<String> getEmployeeNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String query = "SELECT full_name FROM Employee";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTreatmentIdByName(String treatmentName) {
        String query = "SELECT id FROM Treatment WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, treatmentName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getEmployeeIdByName(String employeeName) {
        String query = "SELECT id FROM Employee WHERE full_name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, employeeName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public double getTreatmentPriceByName(String treatmentName) {
        String query = "SELECT standard_price FROM Treatment WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, treatmentName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("standard_price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
