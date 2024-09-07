package com.outsera.goldenraspberryawards.core.security;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.core.security.authorizationserver.UserPrincipal;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.outsera.goldenraspberryawards.core.helper.JsonHelper.toJsonObject;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final ContextualRequest contextualRequest;
    private final HttpServletResponse response;


    public AuthenticationSuccessListener(ContextualRequest contextualRequest, HttpServletResponse response) {
        this.contextualRequest = contextualRequest;
        this.response = response;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        if (contextualRequest.getRequestLog().isPresent()) {

            Long userId = ((UserPrincipal) event.getAuthentication().getPrincipal()).getUserId();

            RequestLog requestLog = contextualRequest.getRequestLog().get();

            Map<String, String> headersMap = new HashMap<>();
            Collection<String> headerNames = response.getHeaderNames();

            headerNames.forEach(headerName -> headersMap.put(headerName, response.getHeader(headerName)));

            requestLog.setResponseHeaders(toJsonObject(headersMap));
            requestLog.setResponseStatus(response.getStatus());
            requestLog.setResponseTime(OffsetDateTime.now());

            contextualRequest.triggerRegisterRequestLog(userId);

        }

    }
}
