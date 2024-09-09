package com.outsera.goldenraspberryawards.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSucessHandler extends SimpleUrlLogoutSuccessHandler {

    private final SecurityProperties securityProperties;
    private final SecurityRules securityRules;

    public CustomLogoutSucessHandler(SecurityProperties securityProperties, SecurityRules securityRules) {
        this.securityProperties = securityProperties;
        this.securityRules = securityRules;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String returnTo = request.getParameter("returnTo");

        response.setStatus(302);
        response.sendRedirect(returnTo);

        super.onLogoutSuccess(request, response, authentication);

    }
}
