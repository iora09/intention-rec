package com.LPSWorkflow.model.execution;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.preference.Preference;

import java.util.Collection;

/**
 * Execution strategy information
 */
public interface Strategy {
    public abstract void choose(Collection<Token> tokens,
                                Collection<Entity> candidates,
                                Collection<String> futureFacts,
                                Collection<String> futureClauses,
                                Collection<Preference> preferences, int limit);

}
