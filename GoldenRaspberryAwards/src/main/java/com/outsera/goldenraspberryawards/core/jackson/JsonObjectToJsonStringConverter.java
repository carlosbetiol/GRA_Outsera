package com.outsera.goldenraspberryawards.core.jackson;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;


@Converter
public class JsonObjectToJsonStringConverter implements AttributeConverter<JSONObject, String> {

    @Override
    public String convertToDatabaseColumn(JSONObject attribute) {
        if(attribute == null)
            return null;

        return attribute.toString();
    }

    @Override
    public JSONObject convertToEntityAttribute(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        }
    }
}
