package com.example.haarsalonprojekt.usecase;

import com.example.haarsalonprojekt.entity.Edit;
import com.example.haarsalonprojekt.dbcontroller.EditDBController;
import javafx.collections.ObservableList;

public class EditTreatmentUseCase {
    private EditDBController editDBController;

    public EditTreatmentUseCase(EditDBController editDBController) {
        this.editDBController = editDBController;
    }

    public ObservableList<String> getHairPeople() {
        return editDBController.gettingHairPeople();
    }

    public ObservableList<String> getTreatments() {
        return editDBController.gettingTreatments();
    }

    public Edit getTreatmentDetails(String treatmentName) {
        return editDBController.getTreatmentDetails(treatmentName);
    }
}
