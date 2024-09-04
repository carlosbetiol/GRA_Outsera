package com.outsera.goldenraspberryawards.domain.model;

import com.outsera.goldenraspberryawards.core.helper.JsonHelper;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public abstract class AbstractEntity implements SysEntity {

    public JSONObject toJSONObject() {
        return JsonHelper.toJSONObject(this);
    }

    public String getEntityName() {
        return this.getClass().getSimpleName();
    }

}
