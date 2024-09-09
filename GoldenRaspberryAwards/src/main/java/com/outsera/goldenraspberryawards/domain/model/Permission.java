package com.outsera.goldenraspberryawards.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "\"permission\"")
@Getter
@Setter
@Accessors(chain = true)
public class Permission extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @Column(name = "\"identifier\"", nullable = false)
    private String identifier;

    @Column(name = "\"description\"", nullable = false)
    private String description;

    @Column(name = "\"created_at\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> permissionRoles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission user = (Permission) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
