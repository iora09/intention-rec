package com.LPSWorkflow.model.history;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * deals with strategies for automatic execution
 */
public class HistoryData {
    private static HistoryData instance = null;

    public static HistoryData getInstance() {
        if (HistoryData.instance == null) {
            synchronized (HistoryData.class) {
                if (HistoryData.instance == null) {
                    HistoryData.instance = new HistoryData();
                }
            }
        }
        return instance;
    }

    private ListProperty<State> states = new SimpleListProperty<>(FXCollections.<State>observableArrayList());
    public ObservableList<State> getStates() {
        return states.get();
    }
    public ListProperty<State> statesProperty() {
        return states;
    }
    public void setStates(ObservableList<State> states) {
        this.states.set(states);
    }
}
