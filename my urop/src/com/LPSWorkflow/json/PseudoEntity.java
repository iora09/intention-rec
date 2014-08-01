package com.LPSWorkflow.json;

import com.LPSWorkflow.common.EntityType;
import com.LPSWorkflow.model.abstractComponent.Entity;

import java.util.List;

/**
 * Pseudo class used for Serialization
 */
public class PseudoEntity extends Entity {
    private EntityType type;

    public PseudoEntity(String name, List<String> terms, String id, EntityType type) {
        super(name, terms);
        this.type = type;
        this.id = id;
    }

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public Entity clone() {
        return null;
    }
}
