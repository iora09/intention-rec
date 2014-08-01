package com.LPSWorkflow.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.beans.property.IntegerProperty;

import java.lang.reflect.Type;

/**
 * Serialises Entity object into Json format
 */
public class IntegerPropertySerializer implements JsonSerializer<IntegerProperty> {
    @Override
    public JsonElement serialize(IntegerProperty integerProperty, Type type, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", integerProperty.get());
        return jsonObject;
    }
}
