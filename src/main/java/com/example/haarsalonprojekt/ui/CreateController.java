package com.example.haarsalonprojekt.ui;

import com.example.haarsalonprojekt.entity.Create;
import com.example.haarsalonprojekt.dbcontroller.CreateDBController;
import com.example.haarsalonprojekt.usecase.CreateAppointmentUseCase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CreateController extends BaseController {
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private ComboBox<String> treatmentComboBox;
    @FXML private ComboBox<String> employeeComboBox;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private TextField appointmentTimeField;
    @FXML private TextField basePriceField;
    @FXML private TextField extraCostField;
    @FXML private TextField extraTimeField;

    private CreateAppointmentUseCase createAppointmentUseCase;

    public CreateController() {
        this.createAppointmentUseCase = new CreateAppointmentUseCase(new CreateDBController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderComboBox.getItems().addAll("M", "F");
        CreateDBController dbController = new CreateDBController();
        ObservableList<String> treatments = dbController.getTreatmentNames();
        treatmentComboBox.setItems(treatments);
        ObservableList<String> employees = dbController.getEmployeeNames();
        employeeComboBox.setItems(employees);

        treatmentComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                double standardPrice = dbController.getTreatmentPriceByName(newVal);
                basePriceField.setText(String.valueOf(standardPrice));
            } else {
                basePriceField.clear();
            }
        });
    }

    @FXML
    private void handleCreateAppointment(ActionEvent event) {
        String customerName = customerNameField.getText().trim();
        String customerPhone = customerPhoneField.getText().trim();
        String customerGender = genderComboBox.getValue();
        String treatmentName = treatmentComboBox.getValue();
        String employeeName = employeeComboBox.getValue();
        LocalDate appointmentDate = appointmentDatePicker.getValue();
        String appointmentTime = appointmentTimeField.getText().trim();

        if (customerName.isEmpty() || customerPhone.isEmpty() || customerGender == null ||
                treatmentName == null || employeeName == null || appointmentDate == null || appointmentTime.isEmpty()) {
            showAlert("Input Error", "All fields must be filled.");
            return;
        }

        LocalTime time;
        try {
            time = LocalTime.parse(appointmentTime);
        } catch (Exception e) {
            showAlert("Time Format Error", "Time must be in format HH:mm");
            return;
        }
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, time);

        double extraCost = 0.0;
        int extraTime = 0;
        if (!extraCostField.getText().trim().isEmpty()) {
            try {
                extraCost = Double.parseDouble(extraCostField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Extra cost must be a number.");
                return;
            }
        }
        if (!extraTimeField.getText().trim().isEmpty()) {
            try {
                extraTime = Integer.parseInt(extraTimeField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Extra time must be an integer.");
                return;
            }
        }

        CreateDBController dbController = new CreateDBController();
        int treatmentId = dbController.getTreatmentIdByName(treatmentName);
        int employeeId = dbController.getEmployeeIdByName(employeeName);

        Create appointment = new Create(customerName, customerPhone, customerGender, treatmentId, appointmentDateTime, employeeId, "open", extraTime, extraCost);

        boolean success = createAppointmentUseCase.execute(appointment);
        if (success) {
            showAlert("Success", "Appointment created successfully.");
            clearFields();
            try {
                sceneManager.switchTo("table");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Error creating appointment.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        customerNameField.clear();
        customerPhoneField.clear();
        genderComboBox.setValue(null);
        treatmentComboBox.setValue(null);
        employeeComboBox.setValue(null);
        appointmentDatePicker.setValue(null);
        appointmentTimeField.clear();
        basePriceField.clear();
        extraCostField.clear();
        extraTimeField.clear();
    }

    @FXML
    private void switchToScene(ActionEvent event) throws IOException {
        if (sceneManager != null) {
            sceneManager.switchTo("table");
        } else {
            System.out.println("SceneManager is not initialized!");
        }
    }
}
