package com.outsera.goldenraspberryawards.api.contextual;

import com.outsera.goldenraspberryawards.core.security.SecurityRules;
import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import com.outsera.goldenraspberryawards.domain.listener.PersistenceLogRegisterEvent;
import com.outsera.goldenraspberryawards.domain.listener.RequestLogRegisterEvent;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import com.outsera.goldenraspberryawards.domain.model.SysEntity;
import com.outsera.goldenraspberryawards.domain.model.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ContextualRequestImpl implements ContextualRequest {

    private final SecurityRules securityRules;

    private String requestUUID;
    private RequestLog requestLog;

    private final ApplicationEventPublisher eventPublisher;

    public ContextualRequestImpl(SecurityRules securityRules, ApplicationEventPublisher eventPublisher) {
        this.securityRules = securityRules;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String getRequestUUID() {
        return requestUUID;
    }

    @Override
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    @Override
    public Optional<RequestLog> getRequestLog() {
        return Optional.ofNullable(requestLog);
    }

    @Override
    public void setRequestLog(RequestLog requestLog) {
        this.requestLog = requestLog;
    }

    @Override
    public void triggerRegisterRequestLog() {
        Long userId = 0L;
        try {
            userId = Long.valueOf(securityRules.getUserId().userId());
        } catch (Exception ignored) {
        }
        triggerRegisterRequestLog(userId);
    }

    @Override
    public void triggerRegisterRequestLog(Long userId) {
        User user = new User().setId(userId);

        if(nonNull(requestLog)) {
            requestLog.setUser(user);
            requestLog.setCreatedAt(OffsetDateTime.now());
            eventPublisher.publishEvent(new RequestLogRegisterEvent(requestLog));
        }
    }

    @Override
    public SysEntity triggerRegisterPersistenceLog(SysEntity entity) {

        Long userId = null;

        try {
            userId = Long.valueOf(securityRules.getUserId().userId());
        } catch (Exception ignored) {
            userId = 0L;
        }

        SyslogActionEnum action = null;
        if (requestLog != null && requestLog.getMethod() != null) {
            action = SyslogActionEnum.toEnum(requestLog.getMethod());
        }

        eventPublisher.publishEvent(new PersistenceLogRegisterEvent(
                new PersistenceLog()
                        .setUser(new User().setId(userId))
                        .setEntityName(entity.getEntityName())
                        .setEntityId(entity.getId())
                        .setJsonContent(entity.toJSONObject())
                        .setRequestUUID(requestUUID)
                        .setCreatedAt(OffsetDateTime.now())
                        .setAction(action)
                        )
        );

        return entity;

    }

}
