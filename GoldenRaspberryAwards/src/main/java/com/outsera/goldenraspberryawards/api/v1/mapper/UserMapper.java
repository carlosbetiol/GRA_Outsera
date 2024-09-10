package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.response.RoleResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.UserResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Role;
import com.outsera.goldenraspberryawards.domain.model.User;
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
public interface UserMapper {

    static UserMapper USER_MAPPER = Mappers.getMapper( UserMapper.class );

    @Mapping(target = "roles", expression = "java( toResponseCollectionModelRoles(getRoles(entity)) )")
    UserResponseDTO toResponseModel(User entity);

    default List<RoleResponseDTO> toResponseCollectionModelRoles(List<Role> roles) {
        return ROLE_MAPPER.toMinimalResponseCollectionModel(roles);
    }

    @Mapping(target = "roles", ignore = true)
    @MinimalResponseModel
    UserResponseDTO toMinimalResponseModel(User entity);

    default List<UserResponseDTO> toResponseMinimalCollectionModel(List<User> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    default List<UserResponseDTO> toResponseCollectionModel(List<User> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }


    default List<Role> getRoles(User entity) {

        if( isNull(entity.getRoles()) )
            return null;

        return new ArrayList<>(entity.getRoles());

    }

}
