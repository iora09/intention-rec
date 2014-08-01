package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PartialOrder entity that groups together partially ordered actions/conditions
 */
public class PartialOrder extends MultiChildEntity {
    public PartialOrder(List<Entity> entities) {
        super("||", entities);
    }

    @Override
    public EntityType getType() {
        return EntityType.PARTIAL_ORDER;
    }

    @Override
    public Entity clone() {
        // clone all next entities and the next entity
        List<Entity> nextEntities = getNextEntities();
        PartialOrder clone = new PartialOrder(nextEntities.stream().map(Entity::clone).collect(Collectors.toList()));
        if(getNext() != null){
            clone.setNext(getNext().clone());
        }
        return clone;
    }
}
