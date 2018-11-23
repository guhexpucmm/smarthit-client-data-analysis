package edu.pucmm.smarthit.binding;

import edu.pucmm.smarthit.model.SensorModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SensorModelBinding {

    private static SensorModelBinding instance = new SensorModelBinding();
    private BooleanProperty canReadProperty = new SimpleBooleanProperty(this, "canReadProperty", false);
    private ObjectProperty<SensorModel> sensorModelProperty = new SimpleObjectProperty<>(this, "sensorModelProperty", new SensorModel());

    private SensorModelBinding() {

    }

    public static SensorModelBinding getInstance() {
        return instance;
    }

    public SensorModel getSensorModelProperty() {
        return sensorModelProperty.get();
    }

    public ObjectProperty<SensorModel> sensorModelPropertyProperty() {
        return sensorModelProperty;
    }

    public void setSensorModelProperty(SensorModel sensorModelProperty) {
        this.sensorModelProperty.set(sensorModelProperty);
    }

    public boolean isCanReadProperty() {
        return canReadProperty.get();
    }

    public BooleanProperty canReadPropertyProperty() {
        return canReadProperty;
    }

    public void setCanReadProperty(boolean canReadProperty) {
        this.canReadProperty.set(canReadProperty);
    }
}
