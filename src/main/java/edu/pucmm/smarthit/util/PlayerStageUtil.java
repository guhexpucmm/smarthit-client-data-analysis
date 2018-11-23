package edu.pucmm.smarthit.util;

import edu.pucmm.smarthit.Views;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlayerStageUtil {
    public PlayerStageUtil() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setTitle("Jugadores registrados");
        stage.setMinWidth(300);
        stage.setMinHeight(650);
        stage.setWidth(300);
        stage.setHeight(650);
        stage.setMaxWidth(300);
        stage.setMaxHeight(650);
        stage.toFront();
        stage.setX(screenBounds.getWidth() - 300);
        stage.setY(0);
        stage.setScene(new SceneUtil(Views.PLAYER_VIEW).loadScene());
        stage.show();
    }
}
