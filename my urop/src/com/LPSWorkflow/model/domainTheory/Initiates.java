package com.LPSWorkflow.model.domainTheory;

import java.util.List;

/**
 * Post-condition (initates)
 */
public class Initiates extends Postcondition {
    public Initiates(String head, List<String> body) {
        super(head, body);
    }

    @Override
    public PostconditionType getType() {
        return PostconditionType.INITIATES;
    }

    @Override
    public String getTypeName() {
        return "initiates";
    }
}
