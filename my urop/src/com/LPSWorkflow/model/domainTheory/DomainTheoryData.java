package com.LPSWorkflow.model.domainTheory;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Data on domain theory (preconditions and postconditions)
 */
public class DomainTheoryData {
    private static DomainTheoryData instance = null;

    public final static DomainTheoryData getInstance() {
        if (DomainTheoryData.instance == null) {
            synchronized (DomainTheoryData.class) {
                if (DomainTheoryData.instance == null) {
                    DomainTheoryData.instance = new DomainTheoryData();
                }
            }
        }
        return instance;
    }

    /* Pre-condition property */
    private ListProperty<Precondition> preconditions = new SimpleListProperty<>(FXCollections.<Precondition>observableArrayList());
    public ListProperty<Precondition> preconditionsProperty(){
        return preconditions;
    }
    public final List<Precondition> getPreconditions(){
        return preconditions.get();
    }
    public void setPreconditions(ObservableList<Precondition> preconditions) {
        this.preconditions.set(preconditions);
    }

    /* Post-condition property */
    private ListProperty<Postcondition> postconditions = new SimpleListProperty<>(FXCollections.<Postcondition>observableArrayList());
    public ListProperty<Postcondition> postconditionsProperty(){
        return postconditions;
    }
    public final List<Postcondition> getPostconditions(){
        return postconditions.get();
    }
    public void setPostconditions(ObservableList<Postcondition> postconditions) {
        this.postconditions.set(postconditions);
    }

    public void init() {
        getPreconditions().clear();
        getPostconditions().clear();
    }
}
