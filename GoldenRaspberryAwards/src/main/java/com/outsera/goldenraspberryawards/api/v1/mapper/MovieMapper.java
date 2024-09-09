package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.request.IdRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.request.MovieRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.ProducerResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.StudioResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.outsera.goldenraspberryawards.api.v1.mapper.ProducerMapper.PRODUCER_MAPPER;
import static com.outsera.goldenraspberryawards.api.v1.mapper.StudioMapper.STUDIO_MAPPER;

@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface MovieMapper {

    static MovieMapper MOVIE_MAPPER = Mappers.getMapper( MovieMapper.class );

    @Mapping(target = "producers", expression = "java( entity.getProducers() == null ? null : toResponseCollectionModelProducers(new ArrayList<>(entity.getProducers())) )")
    @Mapping(target = "studios", expression = "java( entity.getStudios() == null ? null : toResponseCollectionModelStudios(new ArrayList<>(entity.getStudios())) )")
    MovieResponseDTO toResponseModel(Movie entity);

    @Mapping(target = "producers", expression = "java( entity.getProducers() == null ? null : toResponseCollectionModelProducers(new ArrayList<>(entity.getProducers())) )")
    @Mapping(target = "studios", expression = "java( entity.getStudios() == null ? null : toResponseCollectionModelStudios(new ArrayList<>(entity.getStudios())) )")
    @Mapping(target = "awards", ignore = true)
    MovieResponseDTO toMediumResponseModel(Movie entity);

    @Mapping(target = "studios", ignore = true)
    @Mapping(target = "producers", ignore = true)
    @MinimalResponseModel
    MovieResponseDTO toMinimalResponseModel(Movie entity);

    @MinimalResponseModel
    default List<MovieResponseDTO> toMinimalResponseCollectionModel(List<Movie> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    default List<MovieResponseDTO> toResponseCollectionModel(List<Movie> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }

    default List<ProducerResponseDTO> toResponseCollectionModelProducers(List<Producer> producers) {
        return PRODUCER_MAPPER.toResponseCollectionModel(producers);
    }

    default List<StudioResponseDTO> toResponseCollectionModelStudios(List<Studio> studios) {
        return STUDIO_MAPPER.toResponseCollectionModel(studios);
    }

    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Movie toEntity(MovieRequestDTO dto);

    Set<Producer> toProducers(List<IdRequestDTO> producers);

    Set<Studio> toStudios(List<IdRequestDTO> studios);

    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Movie mergeEntity(MovieRequestDTO dto, @MappingTarget Movie entity);


}
