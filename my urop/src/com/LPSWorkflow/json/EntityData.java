package com.LPSWorkflow.json;

import com.LPSWorkflow.model.abstractComponent.Entity;

import java.util.HashSet;
import java.util.Set;

/**
 * parsed entity data from history file
 */
public class EntityData {
    private static EntityData instance = null;

    public static EntityData getInstance() {
        if (EntityData.instance == null) {
            synchronized (EntityData.class) {
                if (EntityData.instance == null) {
                    EntityData.instance = new EntityData();
                }
            }
        }
        return instance;
    }

    private EntityData() {
        this.parsedEntities = new HashSet<>();
    }

    private Set<Entity> parsedEntities;

    public Set<Entity> getParsedEntities() {
        return parsedEntities;
    }
}
