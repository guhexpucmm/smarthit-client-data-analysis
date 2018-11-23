package edu.pucmm.smarthit.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class PlayerModel {
    private StringProperty code = new SimpleStringProperty(this, "code", "");
    private StringProperty names = new SimpleStringProperty(this, "names", "");
    private StringProperty lastNames = new SimpleStringProperty(this, "lastNames", "");
    private ObjectProperty<LocalDate> birthdate = new SimpleObjectProperty<>(this, "birthdate", LocalDate.of(1995, 02, 17));
    private StringProperty weight = new SimpleStringProperty(this, "weight", "");
    private StringProperty height = new SimpleStringProperty(this, "height", "");
    private ObjectProperty<DataModel> dataModelObjectProperty = new SimpleObjectProperty<>(this, "dataModelObjectProperty", new DataModel());

    public PlayerModel() {
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getNames() {
        return names.get();
    }

    public StringProperty namesProperty() {
        return names;
    }

    public void setNames(String names) {
        this.names.set(names);
    }

    public String getLastNames() {
        return lastNames.get();
    }

    public StringProperty lastNamesProperty() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames.set(lastNames);
    }

    public LocalDate getBirthdate() {
        return birthdate.get();
    }

    public ObjectProperty<LocalDate> birthdateProperty() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate.set(birthdate);
    }

    public String getWeight() {
        return weight.get();
    }

    public StringProperty weightProperty() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight.set(weight);
    }

    public String getHeight() {
        return height.get();
    }

    public StringProperty heightProperty() {
        return height;
    }

    public void setHeight(String height) {
        this.height.set(height);
    }

    public DataModel getDataModelObjectProperty() {
        return dataModelObjectProperty.get();
    }

    public ObjectProperty<DataModel> dataModelObjectPropertyProperty() {
        return dataModelObjectProperty;
    }

    public void setDataModelObjectProperty(DataModel dataModelObjectProperty) {
        this.dataModelObjectProperty.set(dataModelObjectProperty);
    }

    @Override
    public String toString() {
        return "PlayerModel{" +
                "code=" + code +
                ", names=" + names +
                ", lastNames=" + lastNames +
                ", birthdate=" + birthdate +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }
}
