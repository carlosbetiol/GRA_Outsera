package com.outsera.goldenraspberryawards.core.security.authorizationserver;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.outsera.goldenraspberryawards.domain.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")
public class UserPrincipal extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = -4183337686018457878L;

    private final Long userId;
    private final String fullName;
    private final String salt;

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), user.getIsActive(), true, true, true, authorities);
        this.fullName = user.getName();
        this.userId = user.getId();
        this.salt = user.getSalt();
    }


    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSalt() {
        return salt;
    }


}
