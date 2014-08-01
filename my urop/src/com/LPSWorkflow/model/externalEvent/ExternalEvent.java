package com.LPSWorkflow.model.externalEvent;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * External events holds information about the events observed from outside the system.
 */
public class ExternalEvent {
    private static ExternalEvent instance = null;

    public final static ExternalEvent getInstance() {
        if (ExternalEvent.instance == null) {
            synchronized (ExternalEvent.class) {
                if (ExternalEvent.instance == null) {
                    ExternalEvent.instance = new ExternalEvent();
                }
            }
        }
        return instance;
    }

    /* Events to happen */
    private StringProperty events = new SimpleStringProperty(this, "str", "");
    public StringProperty eventsProperty(){
        return events;
    }
    public final void setEvents(String str){
        events.set(str);
    }
    public final String getEvents(){
        return events.get();
    }


    private StringProperty observedEvents = new SimpleStringProperty(this, "str", "");
    public String getObservedEvents() {
        return observedEvents.get();
    }
    public StringProperty observedEventsProperty() {
        return observedEvents;
    }
    public void setObservedEvents(String observedEvents) {
        this.observedEvents.set(observedEvents);
    }
}
