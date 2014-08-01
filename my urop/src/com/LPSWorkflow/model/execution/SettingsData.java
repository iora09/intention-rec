package com.LPSWorkflow.model.execution;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Backtrack data to be shared across the application
 */
public class SettingsData {
    private static SettingsData instance = null;

    public final static SettingsData getInstance() {
        if (SettingsData.instance == null) {
            synchronized (SettingsData.class) {
                if (SettingsData.instance == null) {
                    SettingsData.instance = new SettingsData();
                }
            }
        }
        return instance;
    }

    /* Backtrack cycle limit property : takes user input of how many cycles a token should wait before backtracking */
    private IntegerProperty backtrackCycleLimit = new SimpleIntegerProperty(50);
    public int getBacktrackCycleLimit() {
        return backtrackCycleLimit.get();
    }
    public IntegerProperty backtrackCycleLimitProperty() {
        return backtrackCycleLimit;
    }
    public void setBacktrackCycleLimit(int backtrackCycleLimit) {
        this.backtrackCycleLimit.set(backtrackCycleLimit);
    }

    /* Evaluation depth property : maximum number of evaluations to make for look-ahead strategy */
    private IntegerProperty evaluationLimit = new SimpleIntegerProperty(10);
    public int getEvaluationLimit() {
        return evaluationLimit.get();
    }
    public IntegerProperty evaluationLimitProperty() {
        return evaluationLimit;
    }
    public void setEvaluationLimit(int evaluationLimit) {
        this.evaluationLimit.set(evaluationLimit);
    }

    /* RecordStates property : if true, record each state of execution into HistoryData */
    private BooleanProperty recordStates = new SimpleBooleanProperty(false);
    public boolean isRecordStates() {
        return recordStates.get();
    }
    public BooleanProperty recordStatesProperty() {
        return recordStates;
    }
    public void setRecordStates(boolean recordStates) {
        this.recordStates.set(recordStates);
    }
}
