package edu.pucmm.smarthit.util;

import edu.pucmm.smarthit.Views;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SessionStageUtil {
    public SessionStageUtil() {
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Visualización de métrica");
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setMaxWidth(500);
        stage.setMaxHeight(500);
        stage.toFront();
        stage.setScene(new SceneUtil(Views.SESSION_VIEW).loadScene());
        stage.show();
    }
}
