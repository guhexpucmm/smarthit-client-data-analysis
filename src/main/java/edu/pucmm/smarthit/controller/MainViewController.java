package edu.pucmm.smarthit.controller;

import edu.pucmm.smarthit.binding.DataModelBinding;
import edu.pucmm.smarthit.binding.MainViewBinding;
import edu.pucmm.smarthit.binding.PlayerModelBinding;
import edu.pucmm.smarthit.binding.SensorModelBinding;
import edu.pucmm.smarthit.model.DataModel;
import edu.pucmm.smarthit.model.SensorModel;
import edu.pucmm.smarthit.util.BatStageUtil;
import edu.pucmm.smarthit.util.FileUtil;
import edu.pucmm.smarthit.util.PlayerStageUtil;
import edu.pucmm.smarthit.util.SessionStageUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static edu.pucmm.smarthit.util.FileUtil.PLAYERS_PATH;

public class MainViewController implements Initializable {

    private XYChart.Series testAccelerationSeries, testAngularVelocitySeries, testAngleSeries;

    private final int CHART_MAX_WINDOW_DATA = 100;

    private BooleanProperty canPrintDateTime = new SimpleBooleanProperty(this, "canPrintDateTime", true);

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Label lblPlayerSelected;

    @FXML
    private Tab testTab;

    @FXML
    private TextField txtFieldTestTabDate;

    @FXML
    private TextField txtFieldTestTabTime;

    @FXML
    private TextField txtFieldTestTabAccelerationX;

    @FXML
    private TextField txtFieldTestTabAngularVelocityX;

    @FXML
    private TextField txtFieldTestTabAngleX;

    @FXML
    private TextField txtFieldTestTabAccelerationY;

    @FXML
    private TextField txtFieldTestTabAngularVelocityY;

    @FXML
    private TextField txtFieldTestTabAngleY;

    @FXML
    private TextField txtFieldTestTabAccelerationZ;

    @FXML
    private TextField txtFieldTestTabAngularVelocityZ;

    @FXML
    private TextField txtFieldTestTabAngleZ;


    @FXML
    private LineChart<String, Number> testTabLineChartAngle;

    @FXML
    private CategoryAxis testTabLineChartAngleCategoryAxis;

    @FXML
    private NumberAxis testTabLineChartAngleNumberAxis;

    @FXML
    private LineChart<String, Number> testTabLineChartAcceleration;

    @FXML
    private CategoryAxis testTabLineChartAccelerationCategoryAxis;

    @FXML
    private NumberAxis testTabLineChartAccelerationNumberAxis;

    @FXML
    private LineChart<String, Number> testTabLineChartAngularVelocity;

    @FXML
    private CategoryAxis testTabLineChartAngularVelocityCategoryAxis;

    @FXML
    private NumberAxis testTabLineChartAngularVelocityNumberAxis;

    @FXML
    private Tab dataCapturedTab;

    @FXML
    private TableView<SensorModel> tableViewCapturedData;

    @FXML
    private TableColumn<SensorModel, String> capturedDataCol1;

    @FXML
    private TableColumn<SensorModel, String> capturedDataCol2;

    @FXML
    private TableColumn<SensorModel, String> capturedDataCol3;

    @FXML
    private TableColumn<SensorModel, String> capturedDataCol4;

    @FXML
    private TableColumn<SensorModel, String> capturedDataCol5;

    @FXML
    private Tab tabSessionsCaptured;

    @FXML
    private ListView<String> listViewYears;

    @FXML
    private ListView<String> listViewMonths;

    @FXML
    private ListView<String> listViewDays;

    @FXML
    private ListView<String> listViewSessions;

    @FXML
    private ToggleButton toggleButtonCapture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new BatStageUtil();
        new PlayerStageUtil();

        dateTimeProcess();

        MainViewBinding.getInstance().labelTitleProperty().set("NO EXISTE UN JUGADOR SELECCIONADO. SELECCIONE O REGISTRE UNO.");

        lblPlayerSelected.textProperty().bindBidirectional(MainViewBinding.getInstance().labelTitleProperty());

        toggleButtonCapture.disableProperty().bind(MainViewBinding.getInstance().playerSelectedProperty().isEqualTo(new SimpleBooleanProperty(false)));

        initTab1();
        initTab2();
        initTab3();
    }

    private void dateTimeProcess() {
        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (canPrintDateTime.getValue().equals(Boolean.TRUE)) {
                        Platform.runLater(() -> {
                            txtFieldTestTabTime.setText(LocalTime.now().toString());
                            txtFieldTestTabDate.setText(LocalDate.now().toString());
                        });
                    }
                }
            }, 0, 100);
        }).start();
    }

    private void initTab1() {
        mainTabPane.disableProperty().bind(MainViewBinding.getInstance().playerSelectedProperty().isEqualTo(new SimpleBooleanProperty(Boolean.FALSE)));

        testTabLineChartAccelerationCategoryAxis.setTickLabelsVisible(false);
        testTabLineChartAngularVelocityCategoryAxis.setTickLabelsVisible(false);
        testTabLineChartAngleCategoryAxis.setTickLabelsVisible(false);

        testAccelerationSeries = new XYChart.Series();
        testAccelerationSeries.setName("Aceleración");

        testAngularVelocitySeries = new XYChart.Series();
        testAngularVelocitySeries.setName("Velocidad angular");

        testAngleSeries = new XYChart.Series();
        testAngleSeries.setName("Ángulo");

        testTabLineChartAcceleration.getData().addAll(testAccelerationSeries);
        testTabLineChartAngularVelocity.getData().addAll(testAngularVelocitySeries);
        testTabLineChartAngle.getData().addAll(testAngleSeries);

        SensorModelBinding.getInstance().sensorModelPropertyProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                txtFieldTestTabTime.setText(newValue.timeProperty().getValue());
                txtFieldTestTabDate.setText(newValue.dateProperty().getValue());
                txtFieldTestTabAccelerationX.setText(newValue.accelerationProperty().getValue().split(",")[0]);
                txtFieldTestTabAccelerationY.setText(newValue.accelerationProperty().getValue().split(",")[1]);
                txtFieldTestTabAccelerationZ.setText(newValue.accelerationProperty().getValue().split(",")[2]);
                txtFieldTestTabAngularVelocityX.setText(newValue.angularVelocityProperty().getValue().split(",")[0]);
                txtFieldTestTabAngularVelocityY.setText(newValue.angularVelocityProperty().getValue().split(",")[1]);
                txtFieldTestTabAngularVelocityZ.setText(newValue.angularVelocityProperty().getValue().split(",")[2]);
                txtFieldTestTabAngleX.setText(newValue.angleProperty().getValue().split(",")[0]);
                txtFieldTestTabAngleY.setText(newValue.angleProperty().getValue().split(",")[1]);
                txtFieldTestTabAngleZ.setText(newValue.angleProperty().getValue().split(",")[2]);

                testAccelerationSeries.getData().add(new XYChart.Data<>(newValue.timeProperty().get(), Double.parseDouble(newValue.accelerationProperty().get().split(",")[0])));
                testAngularVelocitySeries.getData().add(new XYChart.Data<>(newValue.timeProperty().get(), Double.parseDouble(newValue.angularVelocityProperty().get().split(",")[0])));
                testAngleSeries.getData().add(new XYChart.Data<>(newValue.timeProperty().get(), Double.parseDouble(newValue.angleProperty().get().split(",")[0])));
            });
        });

        DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty().sizeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (newValue.intValue() == 0) {
                    testAccelerationSeries.getData().clear();
                    testAngularVelocitySeries.getData().clear();
                    testAngleSeries.getData().clear();
                }
            });
        });

        PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getNames().equals(""))
                canPrintDateTime.setValue(Boolean.TRUE);
            else
                canPrintDateTime.setValue(Boolean.FALSE);
        });

        toggleButtonCapture.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                SensorModelBinding.getInstance().canReadPropertyProperty().setValue(Boolean.TRUE);
                toggleButtonCapture.setText("Detener");
                canPrintDateTime.setValue(Boolean.FALSE);
            } else {
                SensorModelBinding.getInstance().canReadPropertyProperty().setValue(Boolean.FALSE);
                toggleButtonCapture.setText("Capturar");
                canPrintDateTime.setValue(Boolean.TRUE);
            }
        });
    }

    private void initTab2() {
        capturedDataCol1.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().timeProperty().getValue()));
        capturedDataCol2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().dateProperty().getValue()));
        capturedDataCol3.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().accelerationProperty().getValue().split(",")[0]));
        capturedDataCol4.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().angularVelocityProperty().getValue().split(",")[0]));
        capturedDataCol5.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().angleProperty().getValue().split(",")[0]));

        tableViewCapturedData.itemsProperty().bindBidirectional(DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty());
        tableViewCapturedData.itemsProperty().addListener((observable, oldValue, newValue) -> {
            tableViewCapturedData.scrollTo(newValue.size());
        });
    }

    private void initTab3() {
        PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().addListener((observable, oldValue, newValue) -> {
            cleanListViews();
        });

        tabSessionsCaptured.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cleanListViews();
        });

        PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().addListener((observable, oldValue, newValue) -> {
            listViewYears.getItems().clear();

            String parent = PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode();

            File file = new File(parent);
            File[] parents = file.listFiles();

            for (File p : parents) {
                if (p.isDirectory())
                    listViewYears.getItems().add(p.getName());
            }
        });

        listViewYears.selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listViewMonths.getItems().clear();

            if (newValue != null && newValue != "") {
                String parent = PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + newValue;

                File file = new File(parent);
                File[] parents = file.listFiles();

                if (parents != null) {
                    for (File p : parents) {
                        if (p.isDirectory())
                            listViewMonths.getItems().add(p.getName());
                    }
                }
            }
        });

        listViewMonths.selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listViewDays.getItems().clear();

            if (newValue != null && newValue != "") {
                String parent = PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + listViewYears.selectionModelProperty().get().selectedItemProperty().get() + "/" + newValue;

                File file = new File(parent);
                File[] parents = file.listFiles();

                if (parents != null) {
                    for (File p : parents) {
                        if (p.isDirectory())
                            listViewDays.getItems().add(p.getName());
                    }
                }
            }
        });

        listViewDays.selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listViewSessions.getItems().clear();

            if (newValue != null && newValue != "") {
                String parent = PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + listViewYears.selectionModelProperty().get().selectedItemProperty().get() + "/" + listViewMonths.selectionModelProperty().get().selectedItemProperty().get() + "/" + newValue;

                File file = new File(parent);
                File[] parents = file.listFiles();

                if (parents != null) {
                    for (File p : parents) {
                        if (FilenameUtils.getExtension(p.getName()).equals("json"))
                            listViewSessions.getItems().add(p.getName() + " (Actualizado en: " + Instant.ofEpochMilli(p.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss a")) + ")");
                    }
                }
            }
        });

        listViewSessions.setOnMouseClicked(event -> {
            String path = PLAYERS_PATH + "/" + PlayerModelBinding.getInstance().playerModelObjectPropertyProperty().get().getCode() + "/" + listViewYears.selectionModelProperty().get().selectedItemProperty().get() + "/" + listViewMonths.selectionModelProperty().get().selectedItemProperty().get() + "/" + listViewDays.selectionModelProperty().get().selectedItemProperty().get() + "/";

            if (event.getClickCount() == 2 && event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                String current = path + listViewSessions.selectionModelProperty().getValue().getSelectedItem().split(" ")[0];

                if (current != "") {
                    try {
                        JSONObject jsonObject = new JSONObject(new JSONTokener(new FileInputStream(new File(current))));

                        DataModel dataModel = new DataModel();
                        dataModel.estimatedImpactTimeProperty().setValue(jsonObject.getString("impact"));
                        dataModel.velocityProperty().set(String.valueOf(jsonObject.getDouble("velocity")));

                        JSONArray jsonArray = jsonObject.getJSONArray("metrics");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);

                            SensorModel sensorModel = new SensorModel();

                            String accX = String.valueOf(object.getDouble("accX"));
                            String accY = String.valueOf(object.getDouble("accY"));
                            String accZ = String.valueOf(object.getDouble("accZ"));

                            sensorModel.accelerationProperty().setValue(String.join(",", accX, accY, accZ));

                            String velX = String.valueOf(object.getDouble("velX"));
                            String velY = String.valueOf(object.getDouble("velY"));
                            String velZ = String.valueOf(object.getDouble("velZ"));

                            sensorModel.angularVelocityProperty().setValue(String.join(",", velX, velY, velZ));

                            String angX = String.valueOf(object.getDouble("angX"));
                            String angY = String.valueOf(object.getDouble("angY"));
                            String angZ = String.valueOf(object.getDouble("angZ"));

                            sensorModel.angleProperty().setValue(String.join(",", angX, angY, angZ));

                            sensorModel.timeProperty().setValue(object.getString("time"));
                            sensorModel.dateProperty().setValue(object.getString("date"));

                            dataModel.listPropertyProperty().add(sensorModel);
                        }

                        DataModelBinding.getInstance().dataModelObjectPropertyProperty().set(dataModel);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    new SessionStageUtil();
                }
            }
        });
    }

    private void cleanListViews() {
        listViewYears.selectionModelProperty().get().clearSelection();

        listViewMonths.selectionModelProperty().get().clearSelection();
        listViewMonths.getItems().clear();

        listViewDays.selectionModelProperty().get().clearSelection();
        listViewDays.getItems().clear();

        listViewSessions.selectionModelProperty().get().clearSelection();
        listViewSessions.getItems().clear();
    }

    private void resetTestDataFields() {
        txtFieldTestTabDate.clear();
        txtFieldTestTabTime.clear();
        txtFieldTestTabAccelerationX.clear();
        txtFieldTestTabAccelerationY.clear();
        txtFieldTestTabAccelerationZ.clear();
        txtFieldTestTabAngularVelocityX.clear();
        txtFieldTestTabAngularVelocityY.clear();
        txtFieldTestTabAngularVelocityZ.clear();
        txtFieldTestTabAngleX.clear();
        txtFieldTestTabAngleY.clear();
        txtFieldTestTabAngleZ.clear();
        testAccelerationSeries.getData().clear();
        testAngularVelocitySeries.getData().clear();
        testAngleSeries.getData().clear();
    }
}
