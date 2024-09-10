package com.outsera.goldenraspberryawards.domain.mapper;

import com.outsera.goldenraspberryawards.domain.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

@Mapper(
        imports = {
                OffsetDateTime.class
        }
)
public interface DomainProducerMapper {

    static DomainProducerMapper DOMAIN_PRODUCER_MAPPER = Mappers.getMapper( DomainProducerMapper.class );

    @Mapping(target = "name", source = "name")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Producer createEntity(String name);

}
