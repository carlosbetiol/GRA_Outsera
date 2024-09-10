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

import static com.outsera.goldenraspberryawards.api.v1.mapper.RoleMapper.ROLE_MAPPER;
import static java.util.Objects.isNull;


@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface PermissionMapper {

    static PermissionMapper PERMISSION_MAPPER = Mappers.getMapper( PermissionMapper.class );

    @Mapping(target = "roles", expression = "java( toResponseCollectionModelRoles(getRoles(entity)) )")
    PermissionResponseDTO toResponseModel(Permission entity);

    default List<RoleResponseDTO> toResponseCollectionModelRoles(List<Role> roles) {
        return ROLE_MAPPER.toMinimalResponseCollectionModel(roles);
    }

    @Mapping(target = "roles", ignore = true)
    @MinimalResponseModel
    PermissionResponseDTO toMinimalResponseModel(Permission entity);

    default List<PermissionResponseDTO> toResponseMinimalCollectionModel(List<Permission> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    default List<PermissionResponseDTO> toResponseCollectionModel(List<Permission> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }


    default List<Role> getRoles(Permission entity) {

        if( isNull(entity.getPermissionRoles()) )
            return new ArrayList<>();

        return entity.getPermissionRoles().stream()
                .map(RolePermission::getRole)
                .toList();

    }

}
