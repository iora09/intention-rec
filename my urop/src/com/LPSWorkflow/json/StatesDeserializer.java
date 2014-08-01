package com.LPSWorkflow.json;

import com.LPSWorkflow.model.history.State;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class StatesDeserializer implements JsonDeserializer<Collection<State>>{
    @Override
    public Collection<State> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final JsonArray jsonStatesArray = jsonObject.get("states").getAsJsonArray();
        Collection<State> result = new ArrayList<>();
        for (int i = 0; i < jsonStatesArray.size(); i++) {
            final JsonElement jsonState = jsonStatesArray.get(i);
            result.add(context.deserialize(jsonState, State.class));
        }

        return result;
    }
}
