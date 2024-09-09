package com.outsera.goldenraspberryawards.domain.mapper;

import com.outsera.goldenraspberryawards.domain.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

@Mapper(
        imports = {
                OffsetDateTime.class
        }
)
public interface DomainStudioMapper {

    static DomainStudioMapper DOMAIN_STUDIO_MAPPER = Mappers.getMapper( DomainStudioMapper.class );

    @Mapping(target = "name", source = "name")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Studio createEntity(String name);

}
