package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity to represent multiple paths which all must hold
 */
public class And extends MultiChildEntity {
    public And(List<Entity> entities) {
        super("AND", entities);
    }

    @Override
    public EntityType getType() {
        return EntityType.AND;
    }

    @Override
    public Entity clone() {
        // clone all next entities and the next entity
        List<Entity> nextEntities = getNextEntities();
        And clone = new And(nextEntities.stream().map(Entity::clone).collect(Collectors.toList()));
        if(getNext() != null){
            clone.setNext(getNext().clone());
        }
        return clone;
    }
}
