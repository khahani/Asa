package com.example.khahani.asa.model.roomkinds;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<Message> {
    private final String mKey;

    public MessageDeserializer(String key) {
        mKey = key;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return new Gson().fromJson(jsonElement, Message.class);
    }
}
