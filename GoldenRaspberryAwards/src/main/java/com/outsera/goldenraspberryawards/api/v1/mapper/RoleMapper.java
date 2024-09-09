package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.response.PermissionResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.RoleResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Permission;
import com.outsera.goldenraspberryawards.domain.model.Role;
import com.outsera.goldenraspberryawards.domain.model.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.outsera.goldenraspberryawards.api.v1.mapper.PermissionMapper.PERMISSION_MAPPER;
import static java.util.Objects.isNull;

@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface RoleMapper {

    static RoleMapper ROLE_MAPPER = Mappers.getMapper( RoleMapper.class );

    @Mapping(target = "permissions", expression = "java( toResponseCollectionModelPermissions(getPermissions(entity)) )")
    RoleResponseDTO toResponseModel(Role entity);

    @Mapping(target = "permissions", ignore = true)
    @MinimalResponseModel
    RoleResponseDTO toMinimalResponseModel(Role entity);

    @MinimalResponseModel
    default List<RoleResponseDTO> toMinimalResponseCollectionModel(List<Role> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    default List<RoleResponseDTO> toResponseCollectionModel(List<Role> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }

    default List<PermissionResponseDTO> toResponseCollectionModelPermissions(List<Permission> permission) {
        return PERMISSION_MAPPER.toResponseMinimalCollectionModel(permission);
    }
    default List<Permission> getPermissions(Role entity) {

        if( isNull(entity.getRolePermissions()) )
            return null;

        return entity.getRolePermissions().stream()
                .map(RolePermission::getPermission)
                .toList();

    }

}
