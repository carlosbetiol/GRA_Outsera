package com.outsera.goldenraspberryawards.domain.enums;

public enum SyslogActionEnum {

    CREATE("POST"),
    UPDATE("PUT"),
    DELETE("DELETE"),
    GET("GET"),
    PATCH("PATCH");

    private String method;

    private SyslogActionEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static SyslogActionEnum toEnum(String method) {

        if (method == null || method.isEmpty()) {
            return null;
        }

        for (SyslogActionEnum x : SyslogActionEnum.values()) {
            if (method.equals(x.getMethod())) {
                return x;
            }
        }

        return null;
    }
}
