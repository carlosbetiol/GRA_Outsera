package com.outsera.goldenraspberryawards.core.security;

import com.outsera.goldenraspberryawards.api.contextual.ContextualRequest;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.outsera.goldenraspberryawards.core.helper.JsonHelper.toJsonObject;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ContextualRequest contextualRequest;

    public CustomAuthenticationFailureHandler(ContextualRequest contextualRequest) {
        this.contextualRequest = contextualRequest;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

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

        if (exception instanceof DisabledException) {
            request.getSession().setAttribute("loginError", "Blocked account.");
        } else {
            request.getSession().setAttribute("loginError", "Invalid user or password.");
        }

        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
