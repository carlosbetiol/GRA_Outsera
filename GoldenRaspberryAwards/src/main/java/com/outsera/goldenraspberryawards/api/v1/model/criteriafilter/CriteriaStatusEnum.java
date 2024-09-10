package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

public enum CriteriaStatusEnum {

    ALL("ALL"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String value;

    CriteriaStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CriteriaStatusEnum fromValue(String value) {
        for (CriteriaStatusEnum status : CriteriaStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
