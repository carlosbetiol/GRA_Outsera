package com.outsera.goldenraspberryawards.api.v1.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GenericTrackableRequestDTO implements TrackableRequestDTO {

    private String requestUUID;

    @Override
    public String getRequestUUID() {
        return requestUUID;
    }

    @Override
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

}
