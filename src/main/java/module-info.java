module com.example.haarsalonprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens com.example.haarsalonprojekt.ui to javafx.fxml;

    exports com.example.haarsalonprojekt.ui;
    opens com.example.haarsalonprojekt.entity to javafx.base;
    exports com.example.haarsalonprojekt;
    opens com.example.haarsalonprojekt to javafx.fxml;
}
