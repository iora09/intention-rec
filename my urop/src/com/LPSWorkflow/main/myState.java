package com.LPSWorkflow.main;

import com.google.gson.annotations.Expose;

import java.util.Set;


/**
 * A single state of LPS (for each step)
 */
public class myState {
    @Expose
    private Set<String> facts;
    @Expose
    private Set<String> observedEvents;
    @Expose
    private Set<String> executedActions;


    public myState(Set<String> facts, Set<String> observedEvents, Set<String> executedActions) {
        this.facts = facts;
        this.observedEvents = observedEvents;
        this.executedActions = executedActions;
    }

    public Set<String> getFacts() {
        return facts;
    }

    public Set<String> getObservedEvents() {
        return observedEvents;
    }

    public Set<String> getExecutedActions() {
        return executedActions;
    }


}
