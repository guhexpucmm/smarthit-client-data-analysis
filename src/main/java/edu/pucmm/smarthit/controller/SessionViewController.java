package edu.pucmm.smarthit.controller;

import edu.pucmm.smarthit.Axis;
import edu.pucmm.smarthit.Metrics;
import edu.pucmm.smarthit.binding.DataModelBinding;
import edu.pucmm.smarthit.model.DataModel;
import edu.pucmm.smarthit.model.SensorModel;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class SessionViewController implements Initializable {

    private DataModel dataModel = new DataModel();
    private NumberFormat df;

    private ObjectProperty<Metrics> currentMetric = new SimpleObjectProperty<>(this, "currentMetric", Metrics.ACCELERATION);
    private ObjectProperty<Axis> currentAxis = new SimpleObjectProperty<>(this, "currentAxis", Axis.X);
    private IntegerProperty counter = new SimpleIntegerProperty(this, "counter", 0);

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox vBox;

    @FXML
    private Label lblTitle;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField txtFielDate;

    @FXML
    private TextField txtFieldTotalData;

    @FXML
    private TextField txtFieldEstimatedImpactTime;

    @FXML
    private TextField txtFieldVelocity;

    @FXML
    private TextField txtFieldDisplacement;

    @FXML
    private TextField txtFieldAvgAcceleration;

    @FXML
    private TextField txtFieldAvgAngle;

    @FXML
    private TextField txtFieldAvgAngularVelocity;

    @FXML
    private TableView<SensorModel> tableView;

    @FXML
    private TableColumn<SensorModel, String> colTime;

    @FXML
    private TableColumn<SensorModel, String> colAcceleration;

    @FXML
    private TableColumn<SensorModel, String> colAngularVelocity;

    @FXML
    private TableColumn<SensorModel, String> colAngle;

    @FXML
    private Button btnMetricType;

    @FXML
    private LineChart<SensorModel, String> lineChart;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private ToggleButton toggleButtonXAxis;

    @FXML
    private ToggleGroup group;

    @FXML
    private ToggleButton toggleButtonYAxis;

    @FXML
    private ToggleButton toggleButtonZAxis;

    @FXML
    private ToggleButton toggleButtonAll;

    @FXML
    private Button btnClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);

        btnClose.setOnAction(e -> {
            DataModelBinding.getInstance().dataModelObjectPropertyProperty().set(new DataModel());
            ((Stage) btnClose.getScene().getWindow()).close();
        });

        colTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTime()));
        colAcceleration.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAcceleration().split(",")[0]));
        colAngularVelocity.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAngularVelocity().split(",")[0]));
        colAngle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAngle().split(",")[0]));

        tableView.itemsProperty().bindBidirectional(DataModelBinding.getInstance().dataModelObjectPropertyProperty().get().listPropertyProperty());

        categoryAxis.setTickLabelsVisible(false);
        lineChart.setLegendVisible(true);
        lineChart.setLegendSide(Side.BOTTOM);

        counter.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > new Integer(3).intValue()) {
                counter.set(1);
            }

            switch (counter.intValue()) {
                case 1:
                    currentMetric.set(Metrics.ACCELERATION);
                    btnMetricType.setText("Aceleración");
                    break;

                case 2:
                    currentMetric.set(Metrics.ANGULAR_VELOCITY);
                    btnMetricType.setText("Velocidad angular");
                    break;

                case 3:
                    currentMetric.set(Metrics.ANGLE);
                    btnMetricType.setText("Ángulo");
                    break;
            }

            switch (currentAxis.get()) {
                case X:
                    initXAxis();
                    break;

                case Y:
                    initYAxis();
                    break;

                case Z:
                    initZAxis();
                    break;

                case ALL:
                    initAllAxis();
                    break;
            }
        });

        btnMetricType.setOnAction(event -> {
            counter.set(counter.intValue() + 1);
        });

        toggleButtonXAxis.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initXAxis();
                currentAxis.set(Axis.X);
            }
        });

        toggleButtonYAxis.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initYAxis();
                currentAxis.set(Axis.Y);
            }
        });

        toggleButtonZAxis.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initZAxis();
                currentAxis.set(Axis.Z);
            }
        });

        toggleButtonAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                initAllAxis();
                currentAxis.set(Axis.ALL);
            }
        });

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(toggleButtonXAxis)) {
                toggleButtonXAxis.selectedProperty().setValue(true);
                initXAxis();
                currentAxis.set(Axis.X);
            }

            if (newValue.equals(toggleButtonYAxis)) {
                toggleButtonYAxis.selectedProperty().setValue(true);
                initYAxis();
                currentAxis.set(Axis.Y);
            }

            if (newValue.equals(toggleButtonZAxis)) {
                toggleButtonZAxis.selectedProperty().setValue(true);
                initZAxis();
                currentAxis.set(Axis.Z);
            }
        });

        currentMetric.addListener((observable, oldValue, newValue) -> {

        });

        init();

        counter.set(counter.intValue() + 1);
        toggleButtonXAxis.selectedProperty().setValue(true);
    }

    private void init() {
        this.dataModel = DataModelBinding.getInstance().dataModelObjectPropertyProperty().get();

        txtFielDate.setText(this.dataModel.getListProperty().get(0).getDate());
        txtFieldEstimatedImpactTime.setText(df.format(Double.parseDouble(this.dataModel.estimatedImpactTimeProperty().getValue())));
        txtFieldVelocity.setText(df.format(Double.parseDouble(this.dataModel.velocityProperty().getValue())));
        txtFieldAvgAcceleration.setText(df.format(Double.parseDouble(this.dataModel.accelerationAvgProperty().get().split(",")[0])));
        txtFieldAvgAngularVelocity.setText(df.format(Double.parseDouble(this.dataModel.angularVelocityAvgProperty().get().split(",")[0])));
        txtFieldAvgAngle.setText(df.format(Double.parseDouble(this.dataModel.angleAvgProperty().get().split(",")[0])));
        txtFieldTotalData.setText(String.valueOf(this.dataModel.listPropertyProperty().size()));
    }

    private void initXAxis() {
        XYChart.Series series = new XYChart.Series();

        switch (currentMetric.get()) {
            case ACCELERATION:
                lineChart.setTitle("Aceleración en el eje x");
                break;

            case ANGULAR_VELOCITY:
                lineChart.setTitle("Velocidad angular en el eje x");
                break;

            case ANGLE:
                lineChart.setTitle("Ángulo en el eje x");
                break;
        }

        for (SensorModel sensorModel : this.dataModel.listPropertyProperty().get()) {
            switch (currentMetric.get()) {
                case ACCELERATION:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[0])));
                    break;

                case ANGULAR_VELOCITY:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[0])));
                    break;

                case ANGLE:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[0])));
                    break;
            }
        }

        lineChart.getData().clear();
        lineChart.getData().setAll(series);
    }

    private void initYAxis() {
        XYChart.Series series = new XYChart.Series();

        switch (currentMetric.get()) {
            case ACCELERATION:
                lineChart.setTitle("Aceleración en el eje y");
                break;

            case ANGULAR_VELOCITY:
                lineChart.setTitle("Velocidad angular en el eje y");
                break;

            case ANGLE:
                lineChart.setTitle("Ángulo en el eje y");
                break;
        }

        for (SensorModel sensorModel : this.dataModel.listPropertyProperty().get()) {
            switch (currentMetric.get()) {
                case ACCELERATION:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[1])));
                    break;

                case ANGULAR_VELOCITY:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[1])));
                    break;

                case ANGLE:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[1])));
                    break;
            }
        }

        lineChart.getData().clear();
        lineChart.getData().setAll(series);
    }

    private void initZAxis() {
        XYChart.Series series = new XYChart.Series();

        switch (currentMetric.get()) {
            case ACCELERATION:
                lineChart.setTitle("Aceleración en el eje z");
                break;

            case ANGULAR_VELOCITY:
                lineChart.setTitle("Velocidad angular en el eje z");
                break;

            case ANGLE:
                lineChart.setTitle("Ángulo en el eje z");
                break;
        }

        for (SensorModel sensorModel : this.dataModel.listPropertyProperty().get()) {
            switch (currentMetric.get()) {
                case ACCELERATION:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[2])));
                    break;

                case ANGULAR_VELOCITY:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[2])));
                    break;

                case ANGLE:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[2])));
                    break;
            }
        }

        lineChart.getData().clear();
        lineChart.getData().setAll(series);
    }

    private void initAllAxis() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Eje X");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Eje Y");

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Eje Z");

        switch (currentMetric.get()) {
            case ACCELERATION:
                lineChart.setTitle("Aceleración en todos los ejes");
                break;

            case ANGULAR_VELOCITY:
                lineChart.setTitle("Velocidad angular en todos los ejes");
                break;

            case ANGLE:
                lineChart.setTitle("Ángulo en todos los ejes");
                break;
        }

        for (SensorModel sensorModel : this.dataModel.listPropertyProperty().get()) {
            switch (currentMetric.get()) {
                case ACCELERATION:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[0])));
                    series2.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[1])));
                    series3.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAcceleration().split(",")[2])));
                    break;

                case ANGULAR_VELOCITY:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[0])));
                    series2.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[1])));
                    series3.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngularVelocity().split(",")[2])));
                    break;

                case ANGLE:
                    series.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[0])));
                    series2.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[1])));
                    series3.getData().add(new XYChart.Data<>(sensorModel.getTime(), Double.parseDouble(sensorModel.getAngle().split(",")[2])));
                    break;
            }
        }

        lineChart.getData().clear();
        lineChart.getData().setAll(series, series2, series3);
    }
}
