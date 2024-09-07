package com.outsera.goldenraspberryawards.api.contextual;

import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;

import java.util.Optional;

public interface ContextualRequest {
    String getRequestUUID();
    void setRequestUUID(String requestUUID);
    Optional<RequestLog> getRequestLog();
    void setRequestLog(RequestLog requestLog);
    void triggerRegisterRequestLog();
    void triggerRegisterRequestLog(Long userId);
    SysEntity triggerRegisterPersistenceLog(SysEntity entity);

}
