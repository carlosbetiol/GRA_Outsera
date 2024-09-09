package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.v1.model.response.PersistenceLogResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.UserResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.PersistenceLog;
import com.outsera.goldenraspberryawards.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.outsera.goldenraspberryawards.api.v1.mapper.UserMapper.USER_MAPPER;


@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface PersistenceLogMapper {

    static PersistenceLogMapper PERSISTENCE_LOG_MAPPER = Mappers.getMapper( PersistenceLogMapper.class );

    @Mapping(target = "user", expression = "java( toResponseModelUser(entity.getUser()) )")
    PersistenceLogResponseDTO toResponseModel(PersistenceLog entity);

    default UserResponseDTO toResponseModelUser(User user) {
        return USER_MAPPER.toMinimalResponseModel(user);
    }

    default List<PersistenceLogResponseDTO> toResponseCollectionModel(List<PersistenceLog> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }

}
