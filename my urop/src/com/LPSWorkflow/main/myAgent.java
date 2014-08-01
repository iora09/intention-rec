package com.LPSWorkflow.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

/**
 * Created by iora on 16/07/14.
 */
public class myAgent {

    private String name;
    private Set<String> knowledge = new HashSet<>();
    private double responsibility = 0;
    private String rank;
    private Set<String> intentions = new HashSet<>();

    public myAgent(String name, String rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getKnowledge() {
        return knowledge;
    }

    public void addKnowledge(String[] know) {
        this.knowledge.addAll(Arrays.asList(know));
    }

    public double getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(double responsibility) {
        this.responsibility = responsibility;
    }

    public void addResponsibility(double responsibility) {
        this.responsibility += responsibility;
    }

    public void subtractResponsibility(double responsibility) {
        this.responsibility -= responsibility;
        if(this.responsibility < 0 ) this.responsibility = 0;
    }

    public int getRank() {
       if(this.rank.equals("high")) return 3;
       if(this.rank.equals("medium")) return 2;
       return 1;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void addIntention(String intention) {
        this.intentions.add(intention);
    }

    public Set<String> getIntentions() {
        return this.intentions;
    }
}

