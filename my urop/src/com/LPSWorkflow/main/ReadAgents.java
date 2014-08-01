package com.LPSWorkflow.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iora on 30/07/14.
 */
public class ReadAgents {
    private  static List<myAgent> agents = new ArrayList<>();
    // NB: Our observed agent is always our first agent !
    // So add him first
    public static List<myAgent> addAgents() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Add your agents, when you finish type 'stop' : ");
        int noAgents = 0;
        try{
            System.out.println("Agent's name:");
            String line = br.readLine();
            while(!line.equals("stop")){

                String name = line;
                System.out.println("Agent's rank(high,medium,low):");
                String rank = br.readLine();
                agents.add(new myAgent(name,rank));
                noAgents ++;
                System.out.println("Agent's knowledge(add in one line)");
                String knowledge = br.readLine();
                agents.get(noAgents-1).addKnowledge(knowledge.split(" "));
                System.out.println("Agent's name:");
                line = br.readLine();
            }
        }catch (UnsupportedEncodingException uee){
            System.err.println("Couldn't read your data!");
        }
        return agents;
    }
}
