package com.LPSWorkflow.model.preference;

/**
 * Head of a Preference object
 */
public class PreferenceHead {
    private String preferred;
    private String other;

    public PreferenceHead(String preferred, String other) {
        this.preferred = preferred;
        this.other = other;
    }

    public String getPreferred() {
        return preferred;
    }

    public String getOther() {
        return other;
    }
}
