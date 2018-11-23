package edu.pucmm.smarthit.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneUtil {
    private String path;
    private Scene scene;

    public SceneUtil(String path) {
        this.path = path;
    }

    public Scene loadScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.path));
            Parent parent = fxmlLoader.load();

            this.scene = new Scene(parent);

            return this.scene;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
