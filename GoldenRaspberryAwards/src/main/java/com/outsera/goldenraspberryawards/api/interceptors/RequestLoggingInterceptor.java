package com.outsera.goldenraspberryawards.api.interceptors;


import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.outsera.goldenraspberryawards.core.helper.JsonHelper.toJsonObject;
import static java.util.Objects.nonNull;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final ContextualRequest contextualRequest;

    public RequestLoggingInterceptor(ContextualRequest contextualRequest) {
        this.contextualRequest = contextualRequest;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (contextualRequest.getRequestLog().isPresent()) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
                Class<?> dto = Arrays.stream(parameterTypes).filter(c -> c.getSimpleName().contains("DTO")).findFirst().orElse(null);
                if(nonNull(dto)) {
                    RequestLog requestLog = contextualRequest.getRequestLog().get();
                    requestLog.setRequestDto(dto.getSimpleName());
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if(contextualRequest.getRequestLog().isPresent()) {
            RequestLog requestLog = contextualRequest.getRequestLog().get();

            Map<String, String> headersMap = new HashMap<>();
            Collection<String> headerNames = response.getHeaderNames();

            headerNames.forEach(headerName -> headersMap.put(headerName, response.getHeader(headerName)));

            requestLog.setResponseHeaders(toJsonObject(headersMap));
            requestLog.setResponseStatus(response.getStatus());
            requestLog.setResponseTime(OffsetDateTime.now());

            contextualRequest.triggerRegisterRequestLog();

        }

    }
}