package com.LPSWorkflow.main;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Read the stated from the json file
        // If you want to read from another json file check jsonReader class
        // Getting the History of actions, observations and states
        List<myState> myStates = jsonReader.jsonR();

        // Read the Action-Effect-Conditions Database from a specified file
        // ie: Make a table representing the Domain Theory and the Reactive Rules
        ActionReader aecs = new ActionReader("/home/iora/Desktop/my urop/src/com/LPSWorkflow/main/train.txt");

        // Print the AEC database
        aecs.printAEC();

        // Backtrack the actions/observed events that caused the provided outcome(here "dead(bob)")
        // Using an observed outcome find explanations from the Domain Theory and the History
        List<AEC> traces = Backtrack.trace(myStates, aecs, "dead(bob)");

        // Read from the console the agents' names, rank and knowledge of the situation
        List<myAgent> agents = ReadAgents.addAgents();

        // Assign Responsibility
        AssignResponsibility.assignResponsibility(traces, agents);
    }
}

