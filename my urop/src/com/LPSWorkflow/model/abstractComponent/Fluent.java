package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;
import com.LPSWorkflow.common.FactsHelper;

import java.util.*;

/**
 * Fluent entity (diamond)
 */
public class Fluent extends Entity {
    private Entity FalseNext; // next path when the fluent is false (does not hold)
    private List<String> names;
    private Map<String, List<String>> termsMap;

    public Fluent(List<String> names, Map<String, List<String>> termsMap) {
        super(String.join(":", names), null);
        this.names = names;
        this.termsMap = termsMap;
    }

    public Entity getFalseNext() {
        return FalseNext;
    }

    public void setFalseNext(Entity falseNext) {
        FalseNext = falseNext;
    }

    public String getFullNameWithoutNeg(){
        if(names.size() > 1){
            return null; // it is Concurrent
        }
        String name = getFullName();
        if(name.contains("!")){
            return name.split("!")[1];
        } else {
            return name;
        }
    }

    public String getNameWithoutNeg(){
        if(names.size() > 1){
            return null; // it is Concurrent
        }
        String name = getName();
        if(name.contains("!")){
            return name.split("!")[1];
        } else {
            return name;
        }
    }

    @Override
    public List<String> getTerms(){
        // get all terms for the fluent
        List<String> result = new ArrayList<>();
        termsMap.values().forEach((terms) -> {
            if(terms != null){
                for(String term : terms){
                    if(!result.contains(term)){
                        result.add(term);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public List<String> getTermsClone(){
        return getTerms();
    }

    @Override
    public String getFullName() {
        return FactsHelper.buildFullName(this);
    }

    @Override
    public void setName(String name) { // only used for flipping negations
        super.setName(name);
        List<String> newNames = Arrays.asList(name.split(":"));

        names.stream().filter(n -> n.contains("!")).forEach(oldName -> {
            String newName = oldName.split("!")[1];
            if(newNames.contains(newName)){
                if(termsMap.containsKey(oldName)){
                    List<String> value = termsMap.get(oldName);
                    termsMap.remove(oldName);
                    termsMap.put(newName, value);
                }
            }
        });

        names = newNames;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getNamesClone() {
        return new ArrayList<>(names);
    }

    public Map<String, List<String>> getTermsMap() {
        return termsMap;
    }

    public Map<String, List<String>> getTermsMapClone() {
        Map<String, List<String>> clone = new HashMap<>();
        termsMap.forEach((name, terms) -> {
            if(terms == null){
                clone.put(name, null);
            } else {
                clone.put(name, new ArrayList<>(terms));
            }
        });
        return clone;
    }

    @Override
    public EntityType getType() {
        return EntityType.FLUENT;
    }

    @Override
    public Entity clone() {
        Fluent fluent = new Fluent(getNamesClone(), getTermsMapClone());
        if(getNext() != null){
            fluent.setNext(getNext().clone());
        }
        if(getFalseNext() != null){
            fluent.setFalseNext(getFalseNext().clone());
        }
        return fluent;
    }
}
