package com.LPSWorkflow.main;

import com.LPSWorkflow.json.EntityDeserializer;
import com.LPSWorkflow.json.StatesDeserializer;
import com.LPSWorkflow.json.TokenDeserializer;
import com.LPSWorkflow.model.execution.ExecutionStep;
import com.LPSWorkflow.model.history.State;
import com.LPSWorkflow.model.abstractComponent.*;
import com.LPSWorkflow.model.execution.Token;
import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Created by iora on 09/07/14.
 */
public class jsonReader {

    public static List<myState> jsonR() throws Exception {
        // Using Eli's LPS json deserialiser get
        // the states (recommended: look at a json file)
        // also check GSON documentation online
        Type collectionType;
        collectionType = new TypeToken<Collection<State>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(collectionType, new StatesDeserializer()).create();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityDeserializer()).create();
        gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer()).create();
        Gson gson = gsonBuilder.create();

        // if you want to read from another json file change path here
        Reader reader =
                new InputStreamReader(jsonReader.class.getResourceAsStream("train.json"), "UTF-8");

        // get the LPS states
        Collection<State> states = gson.fromJson(reader, collectionType);
        int stateno = 0;

        Set<String> allFacts = new HashSet<>();
        Set<String> allEvents = new HashSet<>();
        Set<String> allActions = new HashSet<>();
        // Final list with my states( I only need the facts, actions and events)
        List<myState> myStates = new ArrayList<>();

        for (State state : states) {
            // because the way the LPS states are constructed, I only want the states
            // that Execute events or process a reactive rule

            if (state.getStep() == ExecutionStep.EXECUTE_EVENTS ||
                    state.getStep() == ExecutionStep.PROCESS_REACTIVE_RULE) {

                Set<String> facts = getSet(state.getFacts());
                // get the new facts
                Set<String> diffFacts = diff(allFacts, facts);

                Set<String> events = getSet(state.getObservedEvents());
                // get new events
                Set<String> diffEvents = diff(allEvents, events);

                Set<String> actions = getSet(state.getExecutedActions());
                // get new actions
                Set<String> diffActions = diff(allActions, actions);

                // register the state only if there has been a development:
                // either a new fact, event or action
                if (!diffFacts.isEmpty() || !diffEvents.isEmpty() || !diffActions.isEmpty()) {

                    allFacts.addAll(facts);
                    allActions.addAll(actions);
                    allEvents.addAll(events);
                    // create a new state :
                    // all the facts
                    // the new actions or events
                    myStates.add(new myState(allFacts, diffEvents, diffActions));
                    stateno++;

                    // print the states
                    System.out.println("State no : " + stateno);
                    System.out.println("FACTS: ");
                    for (String fact : allFacts) {
                        System.out.print(fact + " ");
                    }
                    System.out.println();
                    System.out.println("EVENTS: ");

                    for (String event : diffEvents) {
                        System.out.print(event + " ");
                    }
                    System.out.println();
                    ;
                    System.out.println("ACTIONS");
                    for (String action : diffActions) {
                        System.out.print(action + " ");
                    }
                    System.out.println();

                }
            }
        }

        return myStates;
    }


    // Gets a set of facts from a single line of facts(String)
    public static Set<String> getSet(String list) {
        return new HashSet<>(Arrays.asList(list.split(" ")));
    }

    // Gets the diference between two sets : A - B
    public static Set<String> diff(Set<String> all, Set<String> curr) {
        Set<String> diff = new HashSet<>();
        for (String s : curr) {
            if (all != null && !all.contains(s)) diff.add(s);
        }
        return diff;
    }



}
