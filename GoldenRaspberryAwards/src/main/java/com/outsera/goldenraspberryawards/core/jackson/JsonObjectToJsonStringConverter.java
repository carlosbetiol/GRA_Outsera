package com.outsera.goldenraspberryawards.core.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JsonObjectToJsonStringConverter implements AttributeConverter<JSONObject, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JSONObject attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, JSONObject.class);
        } catch (Exception e) {
            return null;
        }
    }
}
