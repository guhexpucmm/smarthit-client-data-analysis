package edu.pucmm.smarthit.binding;

import edu.pucmm.smarthit.model.SessionModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SessionModelBinding {
    private static SessionModelBinding instance = new SessionModelBinding();

    private ObjectProperty<SessionModel> sessionModelObjectProperty = new SimpleObjectProperty<>(this, "sessionModelObjectProperty", new SessionModel());

    private SessionModelBinding() {

    }

    public static SessionModelBinding getInstance() {
        return instance;
    }

    public SessionModel getSessionModelObjectProperty() {
        return sessionModelObjectProperty.get();
    }

    public ObjectProperty<SessionModel> sessionModelObjectPropertyProperty() {
        return sessionModelObjectProperty;
    }

    public void setSessionModelObjectProperty(SessionModel sessionModelObjectProperty) {
        this.sessionModelObjectProperty.set(sessionModelObjectProperty);
    }
}
