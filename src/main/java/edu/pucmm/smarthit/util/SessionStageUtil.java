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
        stage.setMinWidth(700);
        stage.setMinHeight(720);
        stage.setWidth(700);
        stage.setHeight(720);
        stage.setMaxWidth(700);
        stage.setMaxHeight(720);
        stage.toFront();
        stage.setScene(new SceneUtil(Views.SESSION_VIEW).loadScene());
        stage.show();
    }
}
