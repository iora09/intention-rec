package com.LPSWorkflow.model.history;

import com.LPSWorkflow.model.execution.ExecutionStep;
import com.LPSWorkflow.model.execution.Token;
import com.google.gson.annotations.Expose;

import java.util.Collection;

/**
 * A single state of LPS (for each step)
 */
public class State {
    @Expose private String facts;
    @Expose private String observedEvents;
    @Expose private String executedActions;
    @Expose private ExecutionStep step;
    @Expose private int cycle;
    @Expose private Collection<Token> tokens;
    @Expose private int index;

    public State(String facts, String observedEvents, String executedActions, ExecutionStep step, int cycle, Collection<Token> tokens) {
        this.facts = facts;
        this.observedEvents = observedEvents;
        this.executedActions = executedActions;
        this.step = step;
        this.cycle = cycle;
        this.tokens = tokens;
    }

    public String getFacts() {
        return facts;
    }

    public String getObservedEvents() {
        return observedEvents;
    }

    public String getExecutedActions() {
        return executedActions;
    }

    public ExecutionStep getStep() {
        return step;
    }

    public int getCycle() {
        return cycle;
    }

    public Collection<Token> getTokens() {
        return tokens;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
