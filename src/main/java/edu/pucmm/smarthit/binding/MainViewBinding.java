package edu.pucmm.smarthit.binding;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainViewBinding {
    private static MainViewBinding instance = new MainViewBinding();

    private StringProperty labelTitle = new SimpleStringProperty(this, "labelTitle", "");
    private BooleanProperty playerSelected = new SimpleBooleanProperty(this, "playerSelected", false);

    private MainViewBinding() {

    }

    public static MainViewBinding getInstance() {
        return instance;
    }

    public String getLabelTitle() {
        return labelTitle.get();
    }

    public StringProperty labelTitleProperty() {
        return labelTitle;
    }

    public void setLabelTitle(String labelTitle) {
        this.labelTitle.set(labelTitle);
    }

    public boolean isPlayerSelected() {
        return playerSelected.get();
    }

    public BooleanProperty playerSelectedProperty() {
        return playerSelected;
    }

    public void setPlayerSelected(boolean playerSelected) {
        this.playerSelected.set(playerSelected);
    }
}
