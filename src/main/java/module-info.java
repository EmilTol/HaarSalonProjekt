module com.example.haarsalonprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.haarsalonprojekt to javafx.fxml;
    exports com.example.haarsalonprojekt;
}