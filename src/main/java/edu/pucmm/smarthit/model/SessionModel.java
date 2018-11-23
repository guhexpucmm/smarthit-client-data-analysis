package edu.pucmm.smarthit.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SessionModel {
    private ListProperty<DataModel> listProperty = new SimpleListProperty<>(this, "listProperty", FXCollections.observableArrayList());

    public SessionModel() {
    }

    public ObservableList<DataModel> getListProperty() {
        return listProperty.get();
    }

    public ListProperty<DataModel> listPropertyProperty() {
        return listProperty;
    }

    public void setListProperty(ObservableList<DataModel> listProperty) {
        this.listProperty.set(listProperty);
    }
}
