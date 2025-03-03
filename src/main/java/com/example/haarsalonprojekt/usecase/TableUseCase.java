package com.example.haarsalonprojekt.usecase;

import com.example.haarsalonprojekt.entity.Table;
import com.example.haarsalonprojekt.dbcontroller.TableDBController;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

public class TableUseCase {
    private TableDBController tableDBController;

    public TableUseCase(TableDBController tableDBController) {
        this.tableDBController = tableDBController;
    }

    public ObservableList<Table> getOpenAppointments() {
        return tableDBController.gettingTable();
    }

    public ObservableList<Table> getClosedOrCancelledAppointments() {
        return tableDBController.gettingOtherTable();
    }

    public void updateAppointmentStatus(String customerName, String customerPhone, LocalDateTime appointmentDatetime, String employeeName, String newStatus) {
        tableDBController.updateAppointmentStatus(customerName, customerPhone, appointmentDatetime, employeeName, newStatus);
    }
}
