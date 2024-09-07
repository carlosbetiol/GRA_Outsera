package com.outsera.goldenraspberryawards.api.v1.model.request;

public interface TrackableRequestDTO {
    default String getRequestUUID() {
        return null;
    };

    default void setRequestUUID(String requestUUID) {
        return;
    };

}