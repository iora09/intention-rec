package com.LPSWorkflow.common;

import com.LPSWorkflow.model.database.Clause;
import com.LPSWorkflow.model.preference.Preference;
import com.LPSWorkflow.model.preference.PreferenceHead;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper methods for dealing with simulations
 */
public class SimulationHelper {
    public static List<Preference> getSatisfiedPreferences(Collection<Preference> preferences, Collection<String> givenFacts){
        return preferences.stream()
                .filter(p -> isPreferenceSatisfied(p, givenFacts))
                .collect(Collectors.toList());
    }

    public static Set<PreferenceHead> getSatisfiedBoundPreferenceHeads(Collection<Preference> satisfiedPreferences, Collection<String> facts) {
        Set<PreferenceHead> result = new HashSet<>();

        // clauses with empty body always holds.
        List<Preference> emptyBodyPrefs = satisfiedPreferences.stream()
                .filter(c -> c.getBody().isEmpty())
                .collect(Collectors.toList());
        emptyBodyPrefs.forEach(preference -> {
            // it should not have any variables. (otherwise evaluated later per token)
            List<String> preferredTerms = FactsHelper.extractTerms(preference.getPreferred());
            List<String> otherTerms = FactsHelper.extractTerms(preference.getOther());
            if (preferredTerms == null || otherTerms == null ||
                    (preferredTerms.stream().noneMatch(FactsHelper::isVariable)
                    && otherTerms.stream().noneMatch(FactsHelper::isVariable))) {
                result.add(preference.getHead());
            }
        });

        // clauses that are not formed of all-negative body (i.e. has at least one positive)
        List<Preference> positivePrefs = satisfiedPreferences.stream()
                .filter(p -> !p.getBody().stream().allMatch(body -> body.contains("!")))
                .collect(Collectors.toList());

        positivePrefs.forEach(p -> {
            List<String> body = p.getBody();
            List<Map<String, String>> bindings = FactsHelper.getValidBindings(body, facts);

            bindings.forEach(binding -> {
                PreferenceHead head = new PreferenceHead(FactsHelper.substituteTerms(p.getPreferred(), binding),
                        FactsHelper.substituteTerms(p.getOther(), binding));
                result.add(head);
            });
        });
        return result;
    }

    private static boolean isBodySatisfied(Collection<String> body, Collection<String> facts) {
        return FactsHelper.holdsAll(body, facts) // either it holds,
                || (!body.isEmpty()              // or are all negative (so must hold for some binding)
                && body.stream().allMatch(b -> b.contains("!")));
    }

    private static boolean isPreferenceSatisfied(Preference p, Collection<String> facts) {
        return isBodySatisfied(p.getBody(), facts);
    }


    public static boolean isClauseSatisfied(Clause clause, Collection<String> facts) {
        return isBodySatisfied(clause.getBody(), facts);
    }

    public static List<Clause> getSatisfiedClauses(Collection<Clause> clauses, Collection<String> givenFacts) {
        Set<String> facts = new HashSet<>(givenFacts);
        List<Clause> satisfiedClauses = new ArrayList<>();
        //highlight what became true because of the facts
        boolean updated;
        do {
            updated = false;
            facts.clear();
            facts.addAll(givenFacts);
            facts.addAll(satisfiedClauses.stream().map(Clause::getHead).collect(Collectors.toList()));
            for(Clause c : clauses){
                if(!satisfiedClauses.contains(c) // not already evaluated
                        && isClauseSatisfied(c, facts)){
                    updated = true;
                    satisfiedClauses.add(c);
                    facts.add(c.getHead());

                    // check if the new addition disables already-evaluated intensional fluents
                    List<Clause> toBeRemoved = new ArrayList<>();
                    satisfiedClauses.forEach(clause -> {
                        if (!isClauseSatisfied(clause, facts)) {
                            // now the clause c2 does not hold because of the change. Remove it.
                            toBeRemoved.add(clause);
                        }
                    });
                    satisfiedClauses.removeAll(toBeRemoved);
                }
            }
        } while(updated);
        return satisfiedClauses;
    }

    public static Set<String> getSatisfiedBoundClauseHeads(Collection<Clause> satisfiedClauses, Collection<String> facts) {
        Set<String> result = new HashSet<>();

        // clauses with empty body always holds.
        List<Clause> emptyBodyClauses = satisfiedClauses.stream()
                .filter(c -> c.getBody().isEmpty())
                .collect(Collectors.toList());
        emptyBodyClauses.forEach(clause -> {
            // it should not have any variables. (otherwise evaluated later per token)
            List<String> terms = FactsHelper.extractTerms(clause.getHead());
            if(terms == null || terms.stream().noneMatch(FactsHelper::isVariable)){
                result.add(clause.getHead());
            }
        });

        // clauses that are not formed of all-negative body (i.e. has at least one positive)
        List<Clause> positiveClauses = satisfiedClauses.stream()
                .filter(c -> !c.getBody().stream().allMatch(body -> body.contains("!")))
                .collect(Collectors.toList());

        positiveClauses.forEach(clause -> {
            List<String> body = clause.getBody();

            List<Map<String, String>> bindings = FactsHelper.getValidBindings(body, facts);
            bindings.forEach(binding ->
                    result.add(FactsHelper.substituteTerms(clause.getHead(), binding)));
        });
        return result;
    }
}
