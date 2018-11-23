package edu.pucmm.smarthit.controller;

import edu.pucmm.smarthit.binding.SensorModelBinding;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Box;

import java.net.URL;
import java.util.ResourceBundle;

public class BatViewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private PerspectiveCamera camera;

    @FXML
    private Box batBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SensorModelBinding.getInstance().sensorModelPropertyProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                batBox.rotateProperty().set(Double.parseDouble(newValue.angleProperty().get().split(",")[0]));
            });
        });
    }
}
