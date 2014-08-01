package com.LPSWorkflow.model.execution.strategy;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.execution.Strategy;
import com.LPSWorkflow.model.execution.Token;
import com.LPSWorkflow.model.preference.Preference;

import java.util.Collection;

/**
 * Greedy strategy that executes all possible actions
 */
public class GreedyStrategy implements Strategy {
    @Override
    public String toString() {
        return "Greedy Strategy";
    }

    @Override
    public void choose(Collection<Token> tokens, Collection<Entity> candidates,
                       Collection<String> futureFacts, Collection<String> futureClauses,
                       Collection<Preference> preferences, int limit) {
        tokens.forEach(t -> {
            if(!t.isSelected() && candidates.contains(t.getCurrentEntity())){
                t.setSelected(true); // select all possible tokens
            }
        });
    }
}
