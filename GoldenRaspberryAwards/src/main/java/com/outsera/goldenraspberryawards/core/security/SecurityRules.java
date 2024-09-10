package com.outsera.goldenraspberryawards.core.security;


import com.outsera.goldenraspberryawards.core.security.authorizationserver.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Component
public class SecurityRules {

    private final Environment environment;

    public SecurityRules(Environment environment) {
        this.environment = environment;
    }

    public String getUriRequest() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        } else {
            return null;
        }
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    public UserId getUserId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();

        List<String> audList = jwt.getClaim("aud");

        return new UserId((audList != null && !audList.isEmpty()) ? audList.get(0) : null, Integer.valueOf(jwt.getClaim("userId")));
    }

    public UserPrincipal getUserPrincipal(Authentication authentication ) {
        return (UserPrincipal) authentication.getPrincipal();
    }

    private Boolean isDevOrTest() {
        return environment.acceptsProfiles(Profiles.of("dev")) || environment.acceptsProfiles(Profiles.of("test"));
    }

    public String getEmail() {

        if(isDevOrTest())
            return "admin@gra.com";

        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return jwt.getClaim("email");
    }

    public Boolean isAdmin() {

        if(isDevOrTest())
            return true;

        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return Optional.ofNullable(jwt.getClaim("isAdmin")).orElse("no").equals("yes");
    }

    public boolean isAuthenticatedUser(Integer userId) {
        return isDevOrTest() || (getUserId().userId() != null && userId != null && getUserId().userId().equals(userId));
    }

    public boolean hasAuthority(String authorityName) {
        return isDevOrTest() || getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(authorityName));
    }

    public boolean hasWriteScope() {
        return hasAuthority("SCOPE_WRITE");
    }

    public boolean hasReadScope() {
        return hasAuthority("SCOPE_READ");
    }

    public boolean canViewUsers() {
        return hasReadScope() && hasAuthority("USR_VIEW");
    }

    public boolean canViewRoles() {
        return hasReadScope() && hasAuthority("RLE_VIEW");
    }

    public boolean canViewPermissions() {
        return hasReadScope() && hasAuthority("PERM_VIEW");
    }

    public boolean canInsertProducers() {
        return hasWriteScope() && hasAuthority("PRC_INS");
    }

    public boolean canEditProducers() {
        return hasWriteScope() && hasAuthority("PRC_EDT");
    }

    public boolean canDeleteProducers() {
        return hasWriteScope() && hasAuthority("PRC_DEL");
    }

    public boolean canViewProducers() {
        return hasReadScope() && hasAuthority("PRC_VIEW");
    }

    public boolean canInsertStudios() {
        return hasWriteScope() && hasAuthority("STD_INS");
    }

    public boolean canEditStudios() {
        return hasWriteScope() && hasAuthority("STD_EDT");
    }

    public boolean canDeleteStudios() {
        return hasWriteScope() && hasAuthority("STD_DEL");
    }

    public boolean canViewStudios() {
        return hasReadScope() && hasAuthority("STD_VIEW");
    }

    public boolean canInsertMovies() {
        return hasWriteScope() && hasAuthority("MOV_INS");
    }

    public boolean canEditMovies() {
        return hasWriteScope() && hasAuthority("MOV_EDT");
    }

    public boolean canDeleteMovies() {
        return hasWriteScope() && hasAuthority("MOV_DEL");
    }

    public boolean canViewMovies() {
        return hasReadScope() && hasAuthority("MOV_VIEW");
    }

    public boolean canInsertAwards() {
        return hasWriteScope() && hasAuthority("AWD_INS");
    }

    public boolean canEditAwards() {
        return hasWriteScope() && hasAuthority("AWD_EDT");
    }

    public boolean canDeleteAwards() {
        return hasWriteScope() && hasAuthority("AWD_DEL");
    }

    public boolean canViewAwards() {
        return hasReadScope() && hasAuthority("AWD_VIEW");
    }

    public boolean canViewLogs() {
        return hasReadScope() && hasAuthority("LOG_VIEW");
    }

}
