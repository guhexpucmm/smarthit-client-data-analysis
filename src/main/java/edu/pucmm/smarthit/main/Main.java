package edu.pucmm.smarthit.main;

import edu.pucmm.smarthit.Views;
import edu.pucmm.smarthit.sensor.Sensor;
import edu.pucmm.smarthit.util.FileUtil;
import edu.pucmm.smarthit.util.SceneUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final String APPLICATION_TITLE = "Smarthit";
    private final int APPLICATION_WIDTH = 1200;
    private final int APPLICATION_HEIGHT = 650;

    public static void main(String[] args) {
        Thread thread = new Thread(Sensor::new);
        thread.setName("sensor");
        thread.start();
        Application.launch(args);
    }

    public Main() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setMinWidth(APPLICATION_WIDTH);
        primaryStage.setWidth(APPLICATION_WIDTH);
        primaryStage.setMaxWidth(APPLICATION_WIDTH);
        primaryStage.setMinHeight(APPLICATION_HEIGHT);
        primaryStage.setHeight(APPLICATION_HEIGHT);
        primaryStage.setMaxHeight(APPLICATION_HEIGHT);
        primaryStage.setMaximized(true);
        primaryStage.toFront();
        primaryStage.setScene(new SceneUtil(Views.MAIN_VIEW).loadScene());
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        new FileUtil().initDataDirectory();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
