package com.outsera.goldenraspberryawards.domain.listener;

import com.outsera.goldenraspberryawards.domain.model.RequestLog;

public class RequestLogRegisterEvent {

    private final RequestLog requestLog;

    public RequestLogRegisterEvent(RequestLog requestLog) {
        this.requestLog = requestLog;
    }

    public RequestLog getRequestLog() {
        return requestLog;
    }

}
