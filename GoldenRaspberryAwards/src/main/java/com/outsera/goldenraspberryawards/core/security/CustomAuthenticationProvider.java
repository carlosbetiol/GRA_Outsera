package com.outsera.goldenraspberryawards.core.security;

import com.outsera.goldenraspberryawards.core.security.authorizationserver.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    final private UserDetailsService userDetailsService;
    final private PasswordEncoder passwordEncoder;
    final private ApplicationEventPublisher eventPublisher;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String salt = ((UserPrincipal) userDetails).getSalt();

        if(! userDetails.isEnabled())
            throw new DisabledException("Disabled account");

        if (passwordEncoder.matches(password + salt, userDetails.getPassword())) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            eventPublisher.publishEvent(new AuthenticationSuccessEvent(auth));
            return auth;
        }

        throw new BadCredentialsException("Invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
