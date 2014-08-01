package com.LPSWorkflow.json;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Serialises Entity object into Json format
 */
public class EntitySerializer implements JsonSerializer<Entity> {
    @Override
    public JsonElement serialize(Entity entity, Type type, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", entity.getId());
        jsonObject.addProperty("name", entity.getFullName());
        jsonObject.addProperty("type", entity.getType().toString());

        return jsonObject;
    }
}
