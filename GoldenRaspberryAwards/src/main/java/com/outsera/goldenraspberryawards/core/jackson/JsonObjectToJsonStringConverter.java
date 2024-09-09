package com.outsera.goldenraspberryawards.core.jackson;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.text.StringEscapeUtils;
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

        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            String trimmedJsonString = jsonString;
            if (trimmedJsonString.startsWith("\"") && trimmedJsonString.endsWith("\"")) {
                trimmedJsonString = trimmedJsonString.substring(1, trimmedJsonString.length() - 1);
            }

            String unescapedJson = StringEscapeUtils.unescapeJson(trimmedJsonString);
            return new JSONObject(unescapedJson);
        } catch (JSONException e) {
            return null;
        }
    }
}
