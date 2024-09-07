package com.outsera.goldenraspberryawards.core.security.authorizationserver;

import com.outsera.goldenraspberryawards.domain.enums.PermissionActionEnum;
import com.outsera.goldenraspberryawards.domain.exception.UserNotFoundException;
import com.outsera.goldenraspberryawards.domain.model.RolePermission;
import com.outsera.goldenraspberryawards.domain.model.User;
import com.outsera.goldenraspberryawards.domain.service.PermissionService;
import com.outsera.goldenraspberryawards.domain.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final PermissionService permissionService;

    public AppUserDetailsService(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findByEmailToAuthentication(username);
        } catch (UserNotFoundException e){
            throw new UsernameNotFoundException("Wrong username/password");
        }

        return new UserPrincipal(user, getPermissions(user));

    }

    private Collection<? extends GrantedAuthority> getPermissions(User user) {
        boolean isaAdmin = user.isAdmin();
        Map<String, RolePermission> rolePermissionMap = new HashMap();
        if(isaAdmin) {
            permissionService.findAll().stream().flatMap(p -> {
                RolePermission rolePermission = new RolePermission();
                        rolePermission.setPermission(p);
                        rolePermission.setAction(PermissionActionEnum.ALLOW);
                        return Stream.of(rolePermission);
                    }
            ).forEach(p -> rolePermissionMap.put(p.getPermission().getIdentifier(), p));
        } else {

            user.getRoles().forEach(g -> g.getRolePermissions()
                    .stream().filter(p -> p.getAction().equals(PermissionActionEnum.ALLOW))
                    .forEach(p -> rolePermissionMap.put(p.getPermission().getIdentifier(), p)));

            user.getRoles().forEach(g -> g.getRolePermissions()
                    .stream().filter(p -> p.getAction().equals(PermissionActionEnum.DENY))
                    .forEach(p -> rolePermissionMap.remove(p.getPermission().getIdentifier(), p)));

        }

        Set<SimpleGrantedAuthority> authorities = rolePermissionMap.values().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission().getIdentifier().toUpperCase()))
                .collect(Collectors.toSet());

        return authorities;
    }

}
