package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity to represent multiple choices or options
 */
public class Or extends MultiChildEntity {
    public Or(List<Entity> entities) {
        super("OR", entities);
    }

    @Override
    public EntityType getType() {
        return EntityType.OR;
    }

    @Override
    public Entity clone() {
        // clone all next entities and the next entity
        List<Entity> nextEntities = getNextEntities();
        Or clone = new Or(nextEntities.stream().map(Entity::clone).collect(Collectors.toList()));
        if(getNext() != null){
            clone.setNext(getNext().clone());
        }
        return clone;
    }
}
