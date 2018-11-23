package edu.pucmm.smarthit.examples;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

public class ChartUpdate extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        NumberAxis xAxis = new NumberAxis(0, 50_000, 5000);
        xAxis.setAutoRanging(false);
        NumberAxis yAxis = new NumberAxis(-1, 1, 25);
        yAxis.setAutoRanging(false);
        LineChart<Number, Number> graph = new LineChart<>(xAxis, yAxis);
        graph.setAnimated(false);
        graph.setCreateSymbols(false);
        graph.setLegendVisible(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        stage.setScene(new Scene(graph));

        GeometryFactory gf = new GeometryFactory();

        long t0 = System.nanoTime();
        Coordinate[] coordinates = new Coordinate[1000];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = new Coordinate(i, Math.sin(Math.toRadians(i / 100)));
        }
        Geometry geom = new LineString(new CoordinateArraySequence(coordinates), gf);
        Geometry simplified = DouglasPeuckerSimplifier.simplify(geom, 0.00001);
        List<XYChart.Data<Number, Number>> update = new ArrayList<XYChart.Data<Number, Number>>();
        for (Coordinate each : simplified.getCoordinates()) {
            update.add(new XYChart.Data<>(each.x, each.y));
        }
        long t1 = System.nanoTime();

        System.out.println(String.format("Reduces points from %d to %d in %.1f ms", coordinates.length, update.size(),
                (t1 - t0) / 1e6));
        ObservableList<XYChart.Data<Number, Number>> list = FXCollections.observableArrayList(update);
        series.setData(list);
        graph.getData().add(series);

        stage.show();
    }
}
