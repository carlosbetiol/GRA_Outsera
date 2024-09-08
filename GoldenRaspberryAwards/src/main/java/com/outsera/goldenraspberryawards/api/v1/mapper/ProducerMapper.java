package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.request.ProducerRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.ProducerResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(
        imports = {
                OffsetDateTime.class
        }
)
public interface ProducerMapper {

    static ProducerMapper PRODUCER_MAPPER = Mappers.getMapper( ProducerMapper.class );

    @Mapping(target = "movies.producers", ignore = true)
    @Mapping(target = "movies.studios", ignore = true)
    ProducerResponseDTO toResponseModel(Producer entity);

    @Mapping(target = "movies", ignore = true)
    @MinimalResponseModel
    ProducerResponseDTO toMinimalResponseModel(Producer entity);

    default List<ProducerResponseDTO> toResponseCollectionModel(List<Producer> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Producer toEntity(ProducerRequestDTO dto);

    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Producer mergeEntity(ProducerRequestDTO dto, @MappingTarget Producer entity);

}
