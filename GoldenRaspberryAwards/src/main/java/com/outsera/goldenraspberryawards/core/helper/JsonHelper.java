package com.outsera.goldenraspberryawards.core.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Map;
import java.util.TimeZone;

public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

    public static String toJsonString(Long[] array) {
        try {
            return objectMapper.writeValueAsString(array);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String toJsonString(Map<String,String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static JSONObject toJsonObject(Map<String,String> map) {
        return new JSONObject(map);
    }

    public static JSONObject toJSONObject(Object obj) {
        try {
            Map<String, Object> map = objectMapper.convertValue(obj, Map.class);
            return new JSONObject(map);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
}
