package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

/**
 * Action entity (a box)
 */
public class Exit extends Entity {
    public Exit() {
        super("Exit", null);
    }

    @Override
    public EntityType getType() {
        return EntityType.EXIT;
    }

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public Entity clone() {
        return new Exit();
    }
}
