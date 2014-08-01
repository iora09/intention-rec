package com.LPSWorkflow.model.execution;

import com.LPSWorkflow.model.abstractComponent.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * information about the backtrack destination used for execution
 */
public class BacktrackInfo {
    private Entity entity;
    private List<Map<String,String>> checkedBindings;
    private Map<String,String> prevBinding;

    public BacktrackInfo(Entity entity, Map<String, String> prevBinding, List<Map<String, String>> checkedBindings) {
        this.entity = entity;
        this.prevBinding = new HashMap<>(prevBinding);
        this.checkedBindings = new ArrayList<>(checkedBindings);
    }

    public boolean isAlreadyChecked(Map<String, String> binding){
        return checkedBindings.stream()
                .anyMatch(b -> b.keySet().stream().allMatch(key -> b.get(key).equals(binding.get(key))));
    }

    public Map<String, String> getPrevBinding() {
        return prevBinding;
    }

    public Entity getEntity() {
        return entity;
    }

    public List<Map<String, String>> getCheckedBindings() {
        return checkedBindings;
    }

    @Override
    public BacktrackInfo clone(){
        return new BacktrackInfo(entity, prevBinding, new ArrayList<>(checkedBindings));
    }

}
