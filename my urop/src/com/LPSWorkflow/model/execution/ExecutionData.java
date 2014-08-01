package com.LPSWorkflow.model.execution;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Execution cycle and step information
 */
public class ExecutionData {
    private static ExecutionData instance = null;

    public final static ExecutionData getInstance() {
        if (ExecutionData.instance == null) {
            synchronized (ExecutionData.class) {
                if (ExecutionData.instance == null) {
                    ExecutionData.instance = new ExecutionData();
                }
            }
        }
        return instance;
    }

    /* Cycle number property  */
    private IntegerProperty cycle = new SimpleIntegerProperty(0);
    public int getCycle() {
        return cycle.get();
    }
    public IntegerProperty cycleProperty() {
        return cycle;
    }
    public void setCycle(int cycle) {
        this.cycle.set(cycle);
    }

    /* Execution step property */
    private Property<ExecutionStep> step = new SimpleObjectProperty<>(ExecutionStep.NONE);
    public ExecutionStep getStep(){
        return step.getValue();
    }
    public Property<ExecutionStep> stepProperty() {
        return step;
    }
    public void setStep(ExecutionStep e){
        step.setValue(e);
    }
}
