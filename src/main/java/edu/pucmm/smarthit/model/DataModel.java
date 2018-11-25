package edu.pucmm.smarthit.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {
    private ListProperty<SensorModel> listProperty = new SimpleListProperty<>(this, "listProperty", FXCollections.observableArrayList());
    private StringProperty velocity = new SimpleStringProperty(this, "velocity", "0.00");
    private StringProperty accelerationAvg = new SimpleStringProperty(this, "accelerationAvg", "0.00");
    private StringProperty angularVelocityAvg = new SimpleStringProperty(this, "angularVelocityAvg", "0.00");
    private StringProperty angleAvg = new SimpleStringProperty(this, "angleAvg", "0.00");
    private StringProperty estimatedImpactTime = new SimpleStringProperty(this, "estimatedImpactTime", "0.00");
    //Duracion del swing

    public DataModel() {
    }

    public ObservableList<SensorModel> getListProperty() {
        return listProperty.get();
    }

    public ListProperty<SensorModel> listPropertyProperty() {
        return listProperty;
    }

    public void setListProperty(ObservableList<SensorModel> listProperty) {
        this.listProperty.set(listProperty);
    }

    public String getVelocity() {
        return velocity.get();
    }

    public StringProperty velocityProperty() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity.set(velocity);
    }

    public String getAccelerationAvg() {
        return accelerationAvg.get();
    }

    public StringProperty accelerationAvgProperty() {
        Float resultX = new Float(0.0f);
        Float resultY = new Float(0.0f);
        Float resultZ = new Float(0.0f);

        for (SensorModel sensorModel : this.listPropertyProperty().get()) {
            resultX += Float.parseFloat(sensorModel.getAcceleration().split(",")[0]);
            resultY += Float.parseFloat(sensorModel.getAcceleration().split(",")[1]);
            resultZ += Float.parseFloat(sensorModel.getAcceleration().split(",")[2]);
        }

        resultX = resultX / this.listPropertyProperty().size();
        resultY = resultY / this.listPropertyProperty().size();
        resultZ = resultZ / this.listPropertyProperty().size();

        accelerationAvg.setValue(String.join(",", resultX.toString(), resultY.toString(), resultZ.toString()));

        return accelerationAvg;
    }

    public void setAccelerationAvg(String accelerationAvg) {
        this.accelerationAvg.set(accelerationAvg);
    }

    public String getAngularVelocityAvg() {
        return angularVelocityAvg.get();
    }

    public StringProperty angularVelocityAvgProperty() {
        Float resultX = new Float(0.0f);
        Float resultY = new Float(0.0f);
        Float resultZ = new Float(0.0f);

        for (SensorModel sensorModel : this.listPropertyProperty().get()) {
            resultX += Float.parseFloat(sensorModel.getAngularVelocity().split(",")[0]);
            resultY += Float.parseFloat(sensorModel.getAngularVelocity().split(",")[1]);
            resultZ += Float.parseFloat(sensorModel.getAngularVelocity().split(",")[2]);
        }

        resultX = resultX / this.listPropertyProperty().size();
        resultY = resultY / this.listPropertyProperty().size();
        resultZ = resultZ / this.listPropertyProperty().size();

        angularVelocityAvg.setValue(String.join(",", resultX.toString(), resultY.toString(), resultZ.toString()));

        return angularVelocityAvg;
    }

    public void setAngularVelocityAvg(String angularVelocityAvg) {
        this.angularVelocityAvg.set(angularVelocityAvg);
    }

    public String getAngleAvg() {
        return angleAvg.get();
    }

    public StringProperty angleAvgProperty() {
        Float resultX = new Float(0.0f);
        Float resultY = new Float(0.0f);
        Float resultZ = new Float(0.0f);

        for (SensorModel sensorModel : this.listPropertyProperty().get()) {
            resultX += Float.parseFloat(sensorModel.getAngle().split(",")[0]);
            resultY += Float.parseFloat(sensorModel.getAngle().split(",")[1]);
            resultZ += Float.parseFloat(sensorModel.getAngle().split(",")[2]);
        }

        resultX = resultX / this.listPropertyProperty().size();
        resultY = resultY / this.listPropertyProperty().size();
        resultZ = resultZ / this.listPropertyProperty().size();

        angleAvg.setValue(String.join(",", resultX.toString(), resultY.toString(), resultZ.toString()));

        return angleAvg;
    }

    public void setAngleAvg(String angleAvg) {
        this.angleAvg.set(angleAvg);
    }

    public String getEstimatedImpactTime() {
        return estimatedImpactTime.get();
    }

    public StringProperty estimatedImpactTimeProperty() {
        return estimatedImpactTime;
    }

    public void setEstimatedImpactTime(String estimatedImpactTime) {
        this.estimatedImpactTime.set(estimatedImpactTime);
    }
}
