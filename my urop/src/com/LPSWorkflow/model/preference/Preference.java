package com.LPSWorkflow.model.preference;

import java.util.List;

/**
 * preference information
 */
public class Preference {
    private PreferenceHead head;
    private List<String> body;

    public Preference(String preferred, String other, List<String> body) {
        this.head = new PreferenceHead(preferred, other);
        this.body = body;
    }

    public PreferenceHead getHead() {
        return head;
    }

    public String getPreferred() {
        return head.getPreferred();
    }

    public String getOther() {
        return head.getOther();
    }

    public List<String> getBody() {
        return body;
    }
}
