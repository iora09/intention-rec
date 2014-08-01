package com.LPSWorkflow.model.execution.strategy;

import com.LPSWorkflow.common.FactsHelper;
import com.LPSWorkflow.common.SimulationHelper;
import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.execution.Strategy;
import com.LPSWorkflow.model.execution.Token;
import com.LPSWorkflow.model.preference.Preference;
import com.LPSWorkflow.model.preference.PreferenceHead;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Strategy that looks ahead in the future to decide which actions to select (based on preferences)
 */
public class LookAheadStrategy implements Strategy {
    private Random random;
    private Set<PreferenceHead> heads;

    public LookAheadStrategy() {
        random = new Random();
    }

    @Override
    public String toString() {
        return "Look-ahead Strategy";
    }

    @Override
    public void choose(Collection<Token> tokens, Collection<Entity> candidates,
                       Collection<String> futureFacts, Collection<String> futureClauses,
                       Collection<Preference> preferences, int givenLimit) {
        Collection<Collection<Token>> searchedSelections = new ArrayList<>();
        List<Token> selectableTokens = tokens.stream()
                .filter(t -> !t.isSelected() && candidates.contains(t.getCurrentEntity()))
                .collect(Collectors.toList());

        // there are 2^n possible combinations for n selectable tokens
        int limit = Math.min(givenLimit, (int)Math.pow(2,selectableTokens.size()));

        Set<String> currentFacts = new HashSet<>(futureFacts);
        currentFacts.addAll(futureClauses); // futureFacts & futureClauses is initially the same as current facts
        //evaluate which future states are preferred.
        heads = SimulationHelper.getSatisfiedBoundPreferenceHeads(
                SimulationHelper.getSatisfiedPreferences(preferences, currentFacts),
                currentFacts);

        // keeps track of the optimal selection made so far. Initially 'no selection'
        List<Token> optimalSelection = new ArrayList<>();
        List<String> optimalFacts = new ArrayList<>(currentFacts); // initially current facts

        List<Token> selectedTokens = new ArrayList<>();
        currentFacts.clear();
        for (int i = 0; i < limit; i++) {
            selectableTokens.forEach(t -> {
                t.setSelected(random.nextBoolean()); // select at random
                if (t.isSelected()) {
                    selectedTokens.add(t);
                }
            });

            if(searchedSelections.stream().anyMatch(selection ->
                    selection.containsAll(selectedTokens) && selectedTokens.containsAll(selection))){
                // already evaluated before;
                i--;
            } else {
                searchedSelections.add(new ArrayList<>(selectedTokens)); // save evaluated option

                currentFacts.addAll(futureFacts);
                currentFacts.addAll(futureClauses);

                if(!isPreferred(optimalFacts, currentFacts)){
                    optimalSelection.clear();
                    optimalSelection.addAll(selectedTokens); // update optimal selection
                    optimalFacts.clear();
                    optimalFacts.addAll(currentFacts);
                }
            }

            selectedTokens.forEach(t -> t.setSelected(false)); // reset selection
            selectedTokens.clear();
            currentFacts.clear();
        }

        // select the optimal result
        optimalSelection.forEach(t -> t.setSelected(true));
    }

    private boolean isPreferred(List<String> source, Set<String> target) {
        if(source.containsAll(target) && target.containsAll(source)){
            return false; // they are the same
        }
        int sourcePreferred = 0;
        int targetPreferred = 0;
        for(PreferenceHead head : heads){
            if(FactsHelper.holds(head.getPreferred(), source, true) && FactsHelper.holds(head.getOther(), target, true)){
                sourcePreferred++;
            }
            if(FactsHelper.holds(head.getPreferred(), target, true) && FactsHelper.holds(head.getOther(), source, true)){
                targetPreferred++;
            }
        }
        return sourcePreferred > targetPreferred;
    }
}
