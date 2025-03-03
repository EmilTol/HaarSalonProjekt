package com.example.haarsalonprojekt.usecase;

import com.example.haarsalonprojekt.entity.Create;
import com.example.haarsalonprojekt.dbcontroller.CreateDBController;

public class CreateAppointmentUseCase {
    private CreateDBController createDBController;

    public CreateAppointmentUseCase(CreateDBController createDBController) {
        this.createDBController = createDBController;
    }

    public boolean execute(Create appointment) {
        int treatmentDuration = createDBController.getTreatmentDurationById(appointment.getTreatmentId());
        if (treatmentDuration == -1) {
            return false;
        }
        boolean isAvailable = createDBController.isTimeSlotAvailable(appointment.getEmployeeId(), appointment.getAppointmentDatetime(), treatmentDuration + appointment.getExtraTime());
        if (!isAvailable) {
            return false;
        }
        return createDBController.insertAppointment(appointment);
    }
}
