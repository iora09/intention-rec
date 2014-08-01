package com.LPSWorkflow.json;

import com.LPSWorkflow.model.abstractComponent.Entity;
import com.LPSWorkflow.model.execution.Token;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Deserialises Json into integerProperty
 */
public class TokenDeserializer implements JsonDeserializer<Token> {
    @Override
    public Token deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String id = json.get("id").getAsString();

        Entity currentEntity = context.deserialize(json.get("currentEntity"), Entity.class);
        Token token = new Token(id, currentEntity);
        token.setCyclesWaited(json.get("cyclesWaited").getAsJsonObject().get("value").getAsInt());

        for(Map.Entry<String, JsonElement> entry : json.get("instantiations").getAsJsonObject().entrySet()){
            token.setInstantiation(entry.getKey(), entry.getValue().getAsString());
        }

        return token;
    }
}
