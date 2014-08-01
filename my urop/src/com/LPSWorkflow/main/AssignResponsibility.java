package com.LPSWorkflow.main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iora on 30/07/14.
 */
public class AssignResponsibility {

    // To assign resposibility we take into account 3 factors :
    // 1. Physical Causation // who took the ultimate action for the outcome
    // 2. Intention
    // 3. Coercion
    // I added some dummy numbers to the responsibility field of the agent
    // This could obviously be manipulated as we like
    public static void assignResponsibility(List<AEC> traces, List<myAgent> agents) {

        physical(traces, agents);
        intention(traces, agents);
        coercion(traces, agents);
        printResponsibility(agents);

    }

    private static void physical(List<AEC> traces, List<myAgent> agents){

        // finds the agent who triggered the last action in the chain of events
        // the one who physically produces the ultimate outcome
        myAgent agent = findAgent(traces.get(0).getAction(), agents);
        // adds responsibility to that agent
        agent.addResponsibility(0.4);
    }

    // Intention is defined as
    // knows(outcome(action)) & does(action) -> intends(action)
    // The way I defined the knows(outcome(action)) in this case is
    // If the agent had the knowledge of all the conditions for all the subsequent actions
    // that will come from his action.

    private static void intention(List<AEC> traces, List<myAgent> agents) {
        List<String> conds = new ArrayList<>();
        // for each link in the chain of events we find the agent who caused the action
        // and we question his knowledge
        for(AEC trace: traces){
            myAgent agent = findAgent(trace.getAction(), agents);
            // We add all the conditions together for the next agent
            conds.addAll(trace.getConditions());
            if(agent.getKnowledge().containsAll(conds)){
                agent.addResponsibility(0.5);
                // We add intention to the agent( will help with coercion)
                agent.addIntention(trace.getAction());
            }
        }

    }

    // Function that finds which agent did and action

    private static myAgent findAgent(String action, List<myAgent> agents){
        // if it doesn't start with do
        // we get the first variable in the action
        // ex : orders(tom,....)
        // NB: when adding more actions , add the agent at the beginning
        if(action.startsWith("orders")){
            String name = action.substring(action.lastIndexOf("(") + 1,
                    action.lastIndexOf(")")).split(",")[0];
            for(myAgent agent : agents){
                if(agent.getName().equals(name)) return agent;
            }
            return null;
        }
        // if it start with do : it is our observed agent
        // The observed agent must be the first one in our list of agents ALWAYS
        return agents.get(0);
    }

    private static void coercion(List<AEC> traces, List<myAgent> agents) {

        // orders(X,Y) ^ !intends(Y) -> coerced(ObservedAgent)
        // Also we take into account the rank of the agents
        // For an agent to be coerced he has to have a lower rank
        for(AEC trace: traces){
            if(trace.getAction().startsWith("orders")){
                myAgent agent = findAgent(trace.getAction(), agents);
                if(!agents.get(0).getIntentions().contains(trace.getEffect()) &&
                        agents.get(0).getRank() < agent.getRank()){

                    agent.addResponsibility(0.3);
                    agents.get(0).subtractResponsibility(0.1);
                }
            }
        }
    }

    private static void printResponsibility(List<myAgent> agents) {
        for(myAgent agent : agents){
            System.out.print(agent.getName());
            System.out.println(" ");
            System.out.printf("%.2f\n", agent.getResponsibility());
        }
    }

}
