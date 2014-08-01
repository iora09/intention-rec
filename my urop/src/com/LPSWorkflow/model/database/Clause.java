package com.LPSWorkflow.model.database;

import com.LPSWorkflow.common.FactsHelper;

import java.util.List;

/**
 * Rule for intensional fluents
 */
public class Clause {
    private String head;
    private List<String> body;

    public Clause(String head, List<String> body) {
        this.head = head;
        this.body = body;
    }

    public List<String> getBoundBody(String boundName) {
        return FactsHelper.getBoundNames(boundName, body);
    }

    public String getHead() {
        return head;
    }

    public List<String> getBody() {
        return body;
    }

    public String toString() {
        String bodyStr = "";
        for(String n : body){
            bodyStr = bodyStr.concat(n + " & ");
        }
        if(body.size() > 0) {
            bodyStr = bodyStr.substring(0, bodyStr.length() - 2); // remove trailing &
        }
        return String.format("%s ← %s", head, bodyStr);
    }
}
