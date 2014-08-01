package com.LPSWorkflow.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by iora on 30/07/14.
 */
public class Backtrack {
    private static List<AEC> traces = new ArrayList<>();

    public static List<AEC> trace(List<myState> myStates, ActionReader aecs, String outcome){
        // you can trace back any fluent(outcome)
        traceBack(myStates, aecs, outcome);
        return traces;
    }

    private static void traceBack(List<myState> myStates, ActionReader aecs, String outcome) {

        for (AEC aec : aecs.getAEC()) { //go through the database of ACTION-EFFECT-CONDS
            if (contains(aec.getEffect(), outcome)) { // If the outcome matches the effect

                AEC boundAec = bind(aec, aec.getEffect(), outcome); // bind the variables of the outcome
                // example : outcome : do(throwSwitch)
                // AEC: action: orders(X,Y) , effect: do(Y), cond: head(X)
                // boundAec: orders(X,throwSwitch) , effect : do(throwSwitch) , cond:head(X)


                for (int i = myStates.size() - 1; i >= 0; i--) { // go through the states

                    myState state = myStates.get(i);

                    // check if the bindings are correct for the actions/events
                    // reboundAec = null when the bindings are not correct
                    // example : outcome: do(throwSwitch)
                    // boundAec:  orders(X,throwSwitch) , effect : do(throwSwitch) , cond:head(X)
                    // reboundAec: orders(tom, throwSwitch), effect: do(throwSwitch), cond:head(tom)
                    AEC reboundAec = checkBindings(state, boundAec, outcome);

                    // 1st case: if what we are looking for is a fact(fluent)
                    if (containsAll(state.getFacts(), outcome)) {

                        // we are looking for the action that caused the fluent in the same state
                        // due to how the LPS program works
                        // we also check that the conditions are in the state's facts
                        // and that the reboundAec is good

                        if (containsAll(state.getExecutedActions(), boundAec.getAction()) &&
                            checkConds(state.getFacts(), boundAec.getConditions()) &&
                            reboundAec != null) {
                            traces.add(reboundAec); // add the trigger to the traces
                            traceBack(myStates, aecs, reboundAec.getAction()); // backtrack on the next trigger

                        }
                    }

                    // 2nd case : we are looking for an action

                    if (containsAll(state.getExecutedActions(), outcome) && i > 0) {
                        // if it is an action we are looking for it has to be an observed event
                        // that triggered it from a previous state
                        myState prevState = myStates.get(i - 1);
                        reboundAec = checkBindings(prevState, boundAec, outcome);

                        if (containsAll(prevState.getObservedEvents(), boundAec.getAction()) &&
                            checkConds(prevState.getFacts(), boundAec.getConditions()) &&
                            reboundAec != null) {
                            traces.add(reboundAec);
                            traceBack(myStates, aecs, reboundAec.getAction());

                        }
                    }

                }
            }

        }
    }

    // Finds if two strings start the same
    // ex do(Y) and do(throwSwitch) are the same action
    // compares the substrings till the "(" character
    private static boolean contains(String elem, String toFind) {
        if(toFind == null || elem == null ) return true;
        if (elem.contains("(") && toFind.contains("(")) {
            if (elem.substring(0, elem.lastIndexOf('(')).equals(toFind.substring(0, toFind.lastIndexOf('(')))) {
                return true;
            }
        } else{
            return elem.equals(toFind);
        }
        return false;
    }

    // If a Collection of Strings has a certain action
    private static boolean containsAll(Collection<String> col, String toFind) {
        for (String elem : col) {
            if (contains(elem, toFind)) return true;
        }
        return false;
    }

    // Replaces the variables from var in the AEC with the ones in the outcome
    private static AEC bind(AEC aec, String var, String outcome) {
        if(var == null || outcome == null) return aec;
        String boundAction = aec.getAction();
        String boundEffect = aec.getEffect();
        List<String> boundConditions = new ArrayList<>();

        if (outcome.contains("(")) {
            // getting the variables between the brackets
            String[] variables = (var.substring(var.lastIndexOf("(") + 1,
                    var.lastIndexOf(")"))).split(",");

            // getting the replacements between the brackets
            String[] replace = outcome.substring(outcome.lastIndexOf("(") + 1,
                    outcome.lastIndexOf(")")).split(",");

            // replaces all the variables that are of length one so : X,Y,Z etc..
            // in the Action, Effect, Conditions
            if (variables.length != 0) {
                if (variables[0].length() == 1) {
                    if(boundAction != null) boundAction = boundAction.replace(variables[0], replace[0]);
                    if(boundEffect != null) boundEffect= boundEffect.replace(variables[0], replace[0]);
                }
                for (int i = 1; i < variables.length; i++) {
                    if (variables[i].length() == 1) {
                        if(boundAction!=null) boundAction = boundAction.replace(variables[i], replace[i]);
                        if(boundEffect!=null) boundEffect = boundEffect.replace(variables[i], replace[i]);
                    }
                }
                String boundCond;
                for (String cond : aec.getConditions()) {
                    if (variables[0].length() == 1) {
                        boundCond = cond;
                        boundCond = boundCond.replace(variables[0], replace[0]);
                    } else boundCond = cond;
                    for (int i = 1; i < variables.length; i++) {
                        if (variables[i].length() == 1) {
                            boundCond = boundCond.replace(variables[i], replace[i]);
                        }
                    }
                    boundConditions.add(boundCond);
                }
            }
            return new AEC(boundAction, boundEffect, boundConditions); // returns the bound AEC
        } else return aec; // in case there is nothing to replace
    }


    private static boolean checkConds(Collection<String> conds1, Collection<String> conds2) {
        //checks if all the conds2 are in conds1
        for (String cond : conds2) {
            if (!containsAll(conds1, cond)) return false;
        }
        return true;
    }

    // ATM : the code supports only bindings for actions/events , not for conditions
    private static AEC checkBindings(myState state, AEC aec, String outcome) {

        // 1st case: Executed actions
        for (String action : state.getExecutedActions()) {
            if (contains(action, aec.getAction())) {
                AEC boundAec = bind(aec, aec.getAction(), action); // bind just action's vars

                // checks that the state contains the fully bound AEC
                if ((boundAec== null || state.getExecutedActions().contains(boundAec.getAction()))&&
                     state.getFacts().containsAll(boundAec.getConditions()) &&
                     aec.getEffect().equals(outcome)) {
                    return boundAec;
                }
            }
        }

        // 2nd case: Observed Events
        for (String event : state.getObservedEvents()) {
            if (contains(event, aec.getAction())) {
                AEC boundAec = bind(aec, aec.getAction(), event); // atm bind only for action's variables
                if ((boundAec.getAction()==null ||state.getObservedEvents().contains(boundAec.getAction()))  &&
                     state.getFacts().containsAll(boundAec.getConditions()) &&
                     aec.getEffect().equals(outcome)) {
                    return boundAec;
                }
            }
        }


        return null;

    }

}
