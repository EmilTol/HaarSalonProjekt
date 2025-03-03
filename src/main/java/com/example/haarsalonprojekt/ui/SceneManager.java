package com.example.haarsalonprojekt.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String name, String fxmlPath, int width, int height) throws IOException {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("Could not find FXML file: " + fxmlPath);
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);
            scenes.put(name, scene);

            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setSceneManager(this);
            } else {
                System.out.println("Controller is not an instance of BaseController: " + controller.getClass().getSimpleName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load scene: " + name);
        }
    }


    public void switchTo(String sceneName) throws IOException {
        if (scenes.containsKey(sceneName)) {
            stage.setScene(scenes.get(sceneName));
            stage.show();
        } else {
            System.out.println("Scene " + sceneName + " does not exist!");
        }
    }
}

