package edu.pucmm.smarthit.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public class SensorModel {
    private StringProperty time = new SimpleStringProperty(this, "time", "");
    private StringProperty date = new SimpleStringProperty(this, "date", "");
    private StringProperty acceleration = new SimpleStringProperty(this, "acceleration", "0.00,0.00,0.00");
    private StringProperty angularVelocity = new SimpleStringProperty(this, "angularVelocity", "0.00,0.00,0.00");
    private StringProperty angle = new SimpleStringProperty(this, "angle", "0.00,0.00,0.00");

    public SensorModel() {
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getAcceleration() {
        if (this.acceleration == null)
            this.acceleration = new SimpleStringProperty("0.00,0.00,0.00");

        return acceleration.get();
    }

    public StringProperty accelerationProperty() {
        return acceleration;
    }

    public void setAcceleration(String acceleration) {
        this.acceleration.set(acceleration);
    }

    public String getAngularVelocity() {
        return angularVelocity.get();
    }

    public StringProperty angularVelocityProperty() {
        if (this.angularVelocity == null)
            this.angularVelocity = new SimpleStringProperty("0.00,0.00,0.00");

        return angularVelocity;
    }

    public void setAngularVelocity(String angularVelocity) {
        this.angularVelocity.set(angularVelocity);
    }

    public String getAngle() {
        return angle.get();
    }

    public StringProperty angleProperty() {
        if (this.angle == null)
            this.angle = new SimpleStringProperty("0.00,0.00,0.00");

        return angle;
    }

    public void setAngle(String angle) {
        this.angle.set(angle);
    }

    @Override
    public String toString() {
        return "SensorModel{" +
                "time=" + time.getValue() +
                ", date=" + date.getValue() +
                ", acceleration=" + acceleration.getValue() +
                ", angularVelocity=" + angularVelocity.getValue() +
                ", angle=" + angle.getValue() +
                '}';
    }

    public boolean canSend() {
        return Float.parseFloat(this.angularVelocity.get().split(",")[0]) > 120.0f && Float.parseFloat(this.acceleration.get().split(",")[0]) > 10f;
    }
}
