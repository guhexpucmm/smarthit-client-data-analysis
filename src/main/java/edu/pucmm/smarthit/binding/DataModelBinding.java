package edu.pucmm.smarthit.binding;

import edu.pucmm.smarthit.model.DataModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DataModelBinding {
    private static DataModelBinding instance = new DataModelBinding();

    private ObjectProperty<DataModel> dataModelObjectProperty = new SimpleObjectProperty<>(this, "dataModelObjectProperty", new DataModel());

    private DataModelBinding() {

    }

    public static DataModelBinding getInstance() {
        return instance;
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
}
