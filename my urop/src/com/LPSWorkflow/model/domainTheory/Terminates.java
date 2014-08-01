package com.LPSWorkflow.model.domainTheory;

import java.util.List;

/**
 * Post-condition (terminates)
 */
public class Terminates extends Postcondition {
    public Terminates(String head, List<String> body) {
        super(head, body);
    }

    @Override
    public PostconditionType getType() {
        return PostconditionType.TERMINATES;
    }

    @Override
    public String getTypeName() {
        return "terminates";
    }
}
