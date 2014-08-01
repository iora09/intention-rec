package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;
import com.LPSWorkflow.common.FactsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An abstract entity in the workflow diagram
 */
public abstract class Entity {
    private static AtomicInteger idCounter = new AtomicInteger(0);
    private static String createID()
    { // creates a unique id for the life of the application
        return String.valueOf(idCounter.getAndIncrement());
    }
    public static void resetID() {
        idCounter.set(0);
    }

    protected String id;
    private String name;
    private List<String> terms;
    private Entity next;
    private Entity definition;

    public Entity getDefinition() {
        return definition;
    }

    public void setDefinition(Entity definition) {
        this.definition = definition;
    }

    public boolean hasDefinition(){
        return definition != null;
    }

    public Entity(String name, List<String> terms) {
        this.name = name;
        this.terms = terms;
        this.id = createID();
    }

    public String getId() {
        return id;
    }

    public boolean hasNext() {
        return next != null;
    }

    public Entity getNext() {
        return next;
    }

    public void setNext(Entity next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getFullName() {
        if(terms == null){
            return name;
        }
        return FactsHelper.buildFullName(name, terms);
    }

    public List<String> getTerms() {
        return terms;
    }

    public List<String> getTermsClone() {
        if(terms == null){
            return null;
        }
        return new ArrayList<>(terms);
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public abstract EntityType getType();

    public String toString(){
        return name;
    }

    public boolean hasSingleChild(){
        return true;
    }

    public abstract Entity clone();

    @Override
    public int hashCode() {
        return Integer.parseInt(id);
    }

    @Override
    public boolean equals(Object obj) {
        //null instanceof Object will always return false
        if (obj == null || !(obj instanceof Entity)){
            return false;
        }
        return obj == this
                || this.id.equals(((Entity) obj).getId());
    }
}