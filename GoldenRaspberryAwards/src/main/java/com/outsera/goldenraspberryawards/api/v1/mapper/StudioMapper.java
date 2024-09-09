package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.request.StudioRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.StudioResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.Studio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.outsera.goldenraspberryawards.api.v1.mapper.MovieMapper.MOVIE_MAPPER;


@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface StudioMapper {

    static StudioMapper STUDIO_MAPPER = Mappers.getMapper( StudioMapper.class );

//    @Mapping(target = "movies.producers", ignore = true)
//    @Mapping(target = "movies.studios", ignore = true)
    @Mapping(target = "movies", expression = "java( entity.getMovies() == null ? null : toResponseCollectionModelMovies(new ArrayList<>(entity.getMovies())) )")
    StudioResponseDTO toResponseModel(Studio entity);

    default List<MovieResponseDTO> toResponseCollectionModelMovies(List<Movie> movies) {
        return MOVIE_MAPPER.toMinimalResponseCollectionModel(movies);
    }

    @Mapping(target = "movies", ignore = true)
    @MinimalResponseModel
    StudioResponseDTO toMinimalResponseModel(Studio entity);

    default List<StudioResponseDTO> toResponseCollectionModel(List<Studio> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Studio toEntity(StudioRequestDTO dto);

    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Studio mergeEntity(StudioRequestDTO dto, @MappingTarget Studio entity);

}
