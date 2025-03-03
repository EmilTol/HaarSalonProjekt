package com.example.haarsalonprojekt.ui;

import com.example.haarsalonprojekt.entity.Login;
import com.example.haarsalonprojekt.dbcontroller.LoginDBController;
import com.example.haarsalonprojekt.usecase.LoginUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController {
    private LoginUseCase loginUseCase;

    public LoginController() {
        this.loginUseCase = new LoginUseCase(new LoginDBController());
    }

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Additional initialization if needed
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Login login = new Login(username, password);
        boolean isValid = loginUseCase.execute(login);

        if (isValid) {
            try {
                sceneManager.switchTo("table");
                usernameField.clear();
                passwordField.clear();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("An error occurred.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        }
    }
}
