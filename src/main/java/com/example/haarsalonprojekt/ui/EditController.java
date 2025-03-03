package com.example.haarsalonprojekt.ui;

import com.example.haarsalonprojekt.entity.Edit;
import com.example.haarsalonprojekt.dbcontroller.EditDBController;
import com.example.haarsalonprojekt.usecase.EditTreatmentUseCase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditController extends BaseController implements Initializable {

    @FXML private ComboBox<String> genderBox;
    @FXML private ComboBox<String> treatmentBox;
    @FXML private ComboBox<String> hairPeopleBox;
    @FXML private TextField price;
    @FXML private TextField duration;

    private EditTreatmentUseCase editUseCase;

    public EditController() {
        this.editUseCase = new EditTreatmentUseCase(new EditDBController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderBox.setItems(FXCollections.observableArrayList("M", "F"));
        hairPeopleBox.setItems(editUseCase.getHairPeople());
        treatmentBox.setItems(editUseCase.getTreatments());

        treatmentBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Edit selectedTreatment = editUseCase.getTreatmentDetails(newValue);
                if (selectedTreatment != null) {
                    price.setText(String.valueOf(selectedTreatment.getStandardPrice()));
                    duration.setText(String.valueOf(selectedTreatment.getStandardDuration()));
                }
            }
        });
    }

    public void switchBack() throws IOException {
        sceneManager.switchTo("table");
    }
}
