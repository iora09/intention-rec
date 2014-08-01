package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

import java.util.List;

/**
 * Action entity (a box)
 */
public class Action extends Entity {
    public Action(String name, List<String> terms) {
        super(name, terms);
    }

    @Override
    public EntityType getType() {
        return EntityType.ACTION;
    }

    @Override
    public Entity clone() {
        Action clone = new Action(getName(), getTermsClone());
        if(getNext() != null){
            clone.setNext(getNext().clone());
        }
        if(hasDefinition()){
            clone.setDefinition(getDefinition().clone());
        }
        return clone;
    }
}
