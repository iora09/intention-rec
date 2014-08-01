package com.LPSWorkflow.model.execution;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.google.gson.annotations.Expose;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.Map;

/**
 * Agent responsible for execution along a single path
 */
public class Token {
    @Expose private Entity currentEntity;
    @Expose private String id;

    @Expose
    /* Cycles Waited property : number of cycles it has waited on current entity.  */
    private IntegerProperty cyclesWaited = new SimpleIntegerProperty(0);
    public int getCyclesWaited() {
        return cyclesWaited.get();
    }
    public IntegerProperty cyclesWaitedProperty() {
        return cyclesWaited;
    }
    public void setCyclesWaited(int cyclesWaited) {
        this.cyclesWaited.set(cyclesWaited);
    }

    /* selected property */
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    public boolean isSelected() {
        return selected.get();
    }
    public BooleanProperty selectedProperty() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    /* candidate property */
    private BooleanProperty candidate = new SimpleBooleanProperty(false);
    public boolean isCandidate() {
        return candidate.get();
    }
    public BooleanProperty candidateProperty() {
        return candidate;
    }
    public void setCandidate(boolean candidate) {
        this.candidate.set(candidate);
    }

    /* inactive property */
    private BooleanProperty inactive = new SimpleBooleanProperty(false);
    public boolean isInactive() {
        return inactive.get();
    }
    public BooleanProperty inactiveProperty() {
        return inactive;
    }
    public void setInactive(boolean inactive) {
        this.inactive.set(inactive);
    }

    @Expose
    /* Instantiations property */
    private MapProperty<String, String> instantiations = new SimpleMapProperty<>(FXCollections.<String, String>observableHashMap());
    public Map<String, String> getInstantiations() {
        return instantiations;
    }
    public MapProperty<String, String> instantiationsProperty() {
        return instantiations;
    }
    public void setInstantiations(ObservableMap<String, String> instantiations) {
        this.instantiations.set(instantiations);
    }


    public Token(String id, Entity currentEntity) {
        this.id = id;
        this.currentEntity = currentEntity;

        candidate.addListener((v, b, isCandidate) -> {
            if(!isCandidate && isSelected()){ // if it is no longer a candidate, should not be selected
                selected.set(false);
            }
        });
    }

    public void increment(){
        setCyclesWaited(getCyclesWaited() + 1);
    }

    public Entity getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(Entity currentEntity) {
        if(!this.currentEntity.equals(currentEntity)){
            setCyclesWaited(0);
        }
        this.currentEntity = currentEntity;
    }

    public void setInstantiation(String var, String instantiation){
        instantiations.put(var, instantiation);
    }

    public String getId() {
        return id;
    }

    @Override
    public Token clone(){
        Token clone = new Token(id, currentEntity);
        instantiations.forEach(clone::setInstantiation);
        clone.setCyclesWaited(getCyclesWaited());
        return clone;
    }
}
