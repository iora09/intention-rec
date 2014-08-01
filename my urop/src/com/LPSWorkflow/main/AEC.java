package com.LPSWorkflow.main;

import java.util.List;

/**
 * Created by iora on 21/07/14.
 */
public class AEC {

    private String action;
    private String effect;
    private List<String> conditions;


    public AEC(String action, String effect, List<String> conditions) {
        this.action = action;
        this.effect = effect;
        this.conditions = conditions;
    }

    public String getAction() {
        return action;
    }

    public String getEffect() {
        return effect;
    }

    public List<String> getConditions() {
        return conditions;
    }
}
