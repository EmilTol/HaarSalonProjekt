package com.example.haarsalonprojekt;

import com.example.haarsalonprojekt.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager sceneManager = new SceneManager(stage);
        System.out.println("SceneManager initialized successfully.");

        sceneManager.addScene("login", "/com/example/haarsalonprojekt/salon1.fxml", 660, 440);
        sceneManager.addScene("table", "/com/example/haarsalonprojekt/salon2.fxml", 770, 470);
        sceneManager.addScene("create", "/com/example/haarsalonprojekt/salon3.fxml", 770, 470);
        sceneManager.addScene("edit", "/com/example/haarsalonprojekt/salon4.fxml", 770, 400);


        sceneManager.switchTo("login");
    }

    public static void main(String[] args) {
        launch();
    }
}
