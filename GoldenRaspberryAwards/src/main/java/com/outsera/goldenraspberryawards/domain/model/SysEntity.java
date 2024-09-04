package com.outsera.goldenraspberryawards.domain.model;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface SysEntity {
    Long getId();
    JSONObject toJSONObject();
    String getEntityName();
}
