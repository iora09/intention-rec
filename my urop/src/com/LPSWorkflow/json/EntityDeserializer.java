package com.LPSWorkflow.json;

import com.LPSWorkflow.common.EntityType;
import com.LPSWorkflow.model.abstractComponent.Entity;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Deserialises Json into integerProperty
 */
public class EntityDeserializer implements JsonDeserializer<Entity> {
    @Override
    public Entity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        String id = json.get("id").getAsString();
        String name = json.get("name").getAsString();
        EntityType entityType = EntityType.valueOf(json.get("type").getAsString());

        PseudoEntity pseudoEntity = new PseudoEntity(name, null, id, entityType);
        EntityData.getInstance().getParsedEntities().add(pseudoEntity);
        return pseudoEntity;
    }
}
