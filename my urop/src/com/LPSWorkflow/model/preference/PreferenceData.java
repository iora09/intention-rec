package com.LPSWorkflow.model.preference;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * PreferenceData to be shared across the application
 */
public class PreferenceData {

    private static PreferenceData instance = null;

    public final static PreferenceData getInstance() {
        if (PreferenceData.instance == null) {
            synchronized (PreferenceData.class) {
                if (PreferenceData.instance == null) {
                    PreferenceData.instance = new PreferenceData();
                }
            }
        }
        return instance;
    }

    /* Preferences property */
    private ListProperty<Preference> preferences = new SimpleListProperty<>(FXCollections.<Preference>observableArrayList());
    public ListProperty<Preference> preferencesProperty(){
        return preferences;
    }
    public final List<Preference> getPreferences(){
        return preferences.get();
    }
    public void setPreferences(ObservableList<Preference> preferences) {
        this.preferences.set(preferences);
    }

}
