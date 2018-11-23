package edu.pucmm.smarthit.util;

import edu.pucmm.smarthit.Views;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BatStageUtil {
    public BatStageUtil() {
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Animación de bateo");
        stage.setMinWidth(300);
        stage.setMinHeight(300);
        stage.setWidth(300);
        stage.setHeight(300);
        stage.setMaxWidth(300);
        stage.setMaxHeight(300);
        stage.toFront();
        stage.setX(0);
        stage.setY(0);
        stage.setScene(new SceneUtil(Views.BAT_VIEW).loadScene());
        stage.show();
    }
}
