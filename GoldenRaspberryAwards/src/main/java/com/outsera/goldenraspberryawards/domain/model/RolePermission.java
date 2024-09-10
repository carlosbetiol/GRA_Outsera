package com.outsera.goldenraspberryawards.domain.model;

import com.outsera.goldenraspberryawards.domain.enums.PermissionActionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"role_permission\"")
@Getter
@Setter
@Accessors(chain = true)
public class RolePermission extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"id\"")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "\"role_id\"", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "\"permission_id\"", nullable = false)
    private Permission permission;

    @Column(name = "\"action\"", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionActionEnum action;

    @Column(name = "\"created_at\"", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "\"updated_at\"", nullable = false)
    private OffsetDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermission rolePermission = (RolePermission) o;
        return Objects.equals(id, rolePermission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
