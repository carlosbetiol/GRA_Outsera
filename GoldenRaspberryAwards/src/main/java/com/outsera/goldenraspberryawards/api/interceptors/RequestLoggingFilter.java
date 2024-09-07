package com.outsera.goldenraspberryawards.api.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

import static com.outsera.goldenraspberryawards.core.helper.JsonHelper.toJsonObject;
import static java.util.Objects.nonNull;

@Component
public class RequestLoggingFilter implements Filter {

    final private ContextualRequest contextualRequest;

    final private ObjectMapper objectMapper;

    public RequestLoggingFilter(ContextualRequest contextualRequest, ObjectMapper objectMapper) {
        this.contextualRequest = contextualRequest;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if(! List.of("POST", "PUT", "PATCH").contains(httpRequest.getMethod().toUpperCase())) {
            chain.doFilter(httpRequest, response);
            return;
        }

        contextualRequest.setRequestUUID(UUID.randomUUID().toString());

        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!"Authorization".equalsIgnoreCase(headerName))
                headersMap.put(headerName, httpRequest.getHeader(headerName));
        }

        RequestLog requestLog = new RequestLog();
        requestLog.setRequestUUID(contextualRequest.getRequestUUID());
        requestLog.setRequestTime(OffsetDateTime.now());
        requestLog.setMethod(httpRequest.getMethod());
        requestLog.setApiUrl(httpRequest.getRequestURI());
        requestLog.setRequestParameters(httpRequest.getQueryString());
        requestLog.setRequestHeaders(toJsonObject(headersMap));

        if (requestURI.startsWith("/oauth2/")) {
            contextualRequest.setRequestLog(requestLog);
            chain.doFilter(request, response);
            return;
        }

        if(List.of("POST", "PUT", "PATCH").contains(httpRequest.getMethod().toUpperCase())) {

            byte[] inputStreamBytes = StreamUtils.copyToByteArray(httpRequest.getInputStream());

            try {
                Object dto = null;
                dto = objectMapper.readValue(inputStreamBytes, Object.class);
                if(nonNull(dto) && dto instanceof LinkedHashMap<?,?>) {
                    LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) dto;
                    linkedHashMap.put("requestUUID", contextualRequest.getRequestUUID());
                    byte[] modifiedBytes = objectMapper.writeValueAsBytes(dto);
                    CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(httpRequest, modifiedBytes);
                    requestLog.setRequestContent(toJsonObject(linkedHashMap));
                    contextualRequest.setRequestLog(requestLog);
                    chain.doFilter(customRequest, response);
                    return;
                }
                CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(httpRequest, inputStreamBytes);
                contextualRequest.setRequestLog(requestLog);
                chain.doFilter(customRequest, response);
            } catch (IOException e) {
                CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(httpRequest, inputStreamBytes);
                contextualRequest.setRequestLog(requestLog);
                chain.doFilter(customRequest, response);
            }

            return;

        }

        contextualRequest.setRequestLog(requestLog);

        chain.doFilter(httpRequest, response);

    }
}
