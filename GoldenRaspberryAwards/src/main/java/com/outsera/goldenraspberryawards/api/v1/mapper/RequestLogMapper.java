package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.v1.model.response.RequestLogResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.UserResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.RequestLog;
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
public interface RequestLogMapper {

    static RequestLogMapper REQUEST_LOG_MAPPER = Mappers.getMapper( RequestLogMapper.class );

    @Mapping(target = "user", expression = "java( toResponseModelUser(entity.getUser()) )")
    RequestLogResponseDTO toResponseModel(RequestLog entity);

    default UserResponseDTO toResponseModelUser(User user) {
        return USER_MAPPER.toMinimalResponseModel(user);
    }

    default List<RequestLogResponseDTO> toResponseCollectionModel(List<RequestLog> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }

}
