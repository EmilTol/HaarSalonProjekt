package com.example.haarsalonprojekt.ui;

import com.example.haarsalonprojekt.entity.Table;
import com.example.haarsalonprojekt.dbcontroller.TableDBController;
import com.example.haarsalonprojekt.usecase.TableUseCase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TableController extends BaseController implements Initializable {

    @FXML private CheckBox myCheckBox;
    @FXML private TableView<Table> timeTable;
    @FXML private TableColumn<Table, String> name;
    @FXML private TableColumn<Table, String> number;
    @FXML private TableColumn<Table, String> gender;
    @FXML private TableColumn<Table, String> treatment;
    @FXML private TableColumn<Table, Double> price;
    @FXML private TableColumn<Table, Integer> duration;
    @FXML private TableColumn<Table, Integer> time;
    @FXML private TableColumn<Table, String> barbar;
    @FXML private TableColumn<Table, String> status;

    private TableUseCase tableUseCase;

    public TableController() {
        this.tableUseCase = new TableUseCase(new TableDBController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateTableData(newValue));
        updateTableData(myCheckBox.isSelected());
    }

    private void updateTableData(boolean isChecked) {
        ObservableList<Table> tableData = isChecked ? tableUseCase.getClosedOrCancelledAppointments() : tableUseCase.getOpenAppointments();
        System.out.println("Rows fetched: " + tableData.size());

        name.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        number.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        gender.setCellValueFactory(new PropertyValueFactory<>("customerGender"));
        treatment.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
        price.setCellValueFactory(new PropertyValueFactory<>("treatmentPrice"));
        duration.setCellValueFactory(new PropertyValueFactory<>("treatmentDuration"));
        time.setCellValueFactory(new PropertyValueFactory<>("appointmentDatetime"));
        barbar.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        timeTable.setItems(tableData);
    }

    @FXML
    private void switchToCreate() throws IOException {
        if (sceneManager != null) {
            sceneManager.switchTo("create");
        } else {
            System.out.println("SceneManager is not initialized!");
        }
    }

    @FXML
    private void switchToEdit() throws IOException {
        if (sceneManager != null) {
            sceneManager.switchTo("edit");
        } else {
            System.out.println("SceneManager is not initialized!");
        }
    }

    @FXML
    private void switchToStart() throws IOException {
        if (sceneManager != null) {
            sceneManager.switchTo("login");
        } else {
            System.out.println("SceneManager is not initialized!");
        }
    }

    @FXML
    private void handleCancelAppointment() {
        Table selectedAppointment = timeTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            tableUseCase.updateAppointmentStatus(
                    selectedAppointment.getCustomer_name(),
                    selectedAppointment.getCustomerPhone(),
                    selectedAppointment.getAppointmentDatetime(),
                    selectedAppointment.getEmployeeName(),
                    "cancelled"
            );
            updateTableData(myCheckBox.isSelected());
        } else {
            showAlert("No appointment selected", "Please select an appointment to cancel.");
        }
    }

    @FXML
    private void handleCloseAppointment() {
        Table selectedAppointment = timeTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            tableUseCase.updateAppointmentStatus(
                    selectedAppointment.getCustomer_name(),
                    selectedAppointment.getCustomerPhone(),
                    selectedAppointment.getAppointmentDatetime(),
                    selectedAppointment.getEmployeeName(),
                    "closed"
            );
            updateTableData(myCheckBox.isSelected());
        } else {
            showAlert("No appointment selected", "Please select an appointment to close.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
