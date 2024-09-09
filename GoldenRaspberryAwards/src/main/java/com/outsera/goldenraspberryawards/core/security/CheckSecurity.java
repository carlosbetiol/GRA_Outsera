package com.outsera.goldenraspberryawards.core.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

    public @interface Users {

        @PreAuthorize("@securityRules.canViewUsers()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Roles {

        @PreAuthorize("@securityRules.canViewRoles()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Permissions {

        @PreAuthorize("@securityRules.canViewPermissions()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Producers {

        @PreAuthorize("@securityRules.canInsertProducers()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanInsert{}

        @PreAuthorize("@securityRules.canEditProducers()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanEdit{}

        @PreAuthorize("@securityRules.canDeleteProducers()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanDelete{}

        @PreAuthorize("@securityRules.canViewProducers()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Studios {

        @PreAuthorize("@securityRules.canInsertStudios()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanInsert{}

        @PreAuthorize("@securityRules.canEditStudios()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanEdit{}

        @PreAuthorize("@securityRules.canDeleteStudios()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanDelete{}

        @PreAuthorize("@securityRules.canViewStudios()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Movies {

        @PreAuthorize("@securityRules.canInsertMovies()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanInsert{}

        @PreAuthorize("@securityRules.canEditMovies()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanEdit{}

        @PreAuthorize("@securityRules.canDeleteMovies()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanDelete{}

        @PreAuthorize("@securityRules.canViewMovies()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Awards {

        @PreAuthorize("@securityRules.canInsertAwards()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanInsert{}

        @PreAuthorize("@securityRules.canEditAwards()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanEdit{}

        @PreAuthorize("@securityRules.canDeleteAwards()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanDelete{}

        @PreAuthorize("@securityRules.canViewAwards()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

    public @interface Logs {

        @PreAuthorize("@securityRules.canViewLogs()")
        @Retention(RUNTIME)
        @Target(METHOD)
        public @interface CanView{}

    }

}
