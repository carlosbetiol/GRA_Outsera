package com.outsera.goldenraspberryawards.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;

public class JSONObjectSerializer extends JsonSerializer<JSONObject> {
    @Override
    public void serialize(JSONObject jsonObject, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeString(jsonObject.toString());
    }
}
