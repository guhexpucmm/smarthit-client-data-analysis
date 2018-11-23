package edu.pucmm.smarthit.binding;

import edu.pucmm.smarthit.model.PlayerModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerModelBinding {
    private static PlayerModelBinding instance = new PlayerModelBinding();

    private ObjectProperty<PlayerModel> playerModelObjectProperty = new SimpleObjectProperty<>(this, "playerModelObjectProperty", new PlayerModel());
    private ListProperty<PlayerModel> listProperty = new SimpleListProperty<>(this, "listProperty", FXCollections.observableArrayList());

    private PlayerModelBinding() {

    }

    public static PlayerModelBinding getInstance() {
        return instance;
    }

    public PlayerModel getPlayerModelObjectProperty() {
        return playerModelObjectProperty.get();
    }

    public ObjectProperty<PlayerModel> playerModelObjectPropertyProperty() {
        return playerModelObjectProperty;
    }

    public void setPlayerModelObjectProperty(PlayerModel playerModelObjectProperty) {
        this.playerModelObjectProperty.set(playerModelObjectProperty);
    }

    public ObservableList<PlayerModel> getListProperty() {
        return listProperty.get();
    }

    public ListProperty<PlayerModel> listPropertyProperty() {
        return listProperty;
    }

    public void setListProperty(ObservableList<PlayerModel> listProperty) {
        this.listProperty.set(listProperty);
    }
}
