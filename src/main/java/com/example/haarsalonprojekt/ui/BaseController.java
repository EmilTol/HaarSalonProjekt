package com.example.haarsalonprojekt.ui;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        System.out.println("SceneManager set to " + sceneManager);
    }
}
