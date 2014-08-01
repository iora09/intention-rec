package com.LPSWorkflow.model.execution.strategy;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.execution.Strategy;
import com.LPSWorkflow.model.execution.Token;
import com.LPSWorkflow.model.preference.Preference;

import java.util.Collection;
import java.util.Random;

/**
 * Random strategy that executes random actions
 */
public class RandomStrategy implements Strategy {
    private Random random;

    public RandomStrategy() {
        random = new Random();
    }

    @Override
    public String toString() {
        return "Random Strategy";
    }

    @Override
    public void choose(Collection<Token> tokens, Collection<Entity> candidates,
                       Collection<String> futureFacts, Collection<String> futureClauses,
                       Collection<Preference> preferences, int limit) {
        tokens.forEach(t -> {
            if(!t.isSelected() && candidates.contains(t.getCurrentEntity())){
                t.setSelected(random.nextBoolean()); // select at random
            }
        });
    }
}
