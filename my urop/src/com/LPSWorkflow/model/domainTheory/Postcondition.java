package com.LPSWorkflow.model.domainTheory;

import com.LPSWorkflow.common.FactsHelper;

import java.util.List;
import java.util.Map;

/**
 * Postcondition (for initiates and terminates)
 */
public abstract class Postcondition {
    private String head;
    private List<String> body;

    public Postcondition(String head, List<String> body) {
        this.head = head;
        this.body = body;
    }

    public abstract PostconditionType getType();
    public abstract String getTypeName();

    public String getHead() {
        return head;
    }

    public String getBoundHead(Map<String, String> binding) {
        return FactsHelper.substituteTerms(head, binding);
    }

    public List<String> getBody() {
        return body;
    }

    public List<String> getBoundBody(String boundName) {
        return FactsHelper.getBoundNames(boundName, body);
    }

    @Override
    public String toString() {
        String bodyStr = "";
        for(String n : body){
            bodyStr = bodyStr.concat(n + " & ");
        }
        if(body.size() > 0) {
            bodyStr = bodyStr.substring(0, bodyStr.length() - 2); // remove trailing &
        }
        return String.format("%s(%s) ← %s", getTypeName(), head, bodyStr);
    }
}
