package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.request.IdRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.request.MovieAwardRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieAwardResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.outsera.goldenraspberryawards.api.v1.mapper.MovieMapper.MOVIE_MAPPER;

@Mapper(
        imports = {
                OffsetDateTime.class,
                ArrayList.class
        }
)
public interface MovieAwardMapper {

    static MovieAwardMapper MOVIE_AWARD_MAPPER = Mappers.getMapper( MovieAwardMapper.class );

    @Mapping(target = "movieWinner", expression = "java( entity.getMovieWinner() == null ? null : toResponseModelMovie(entity.getMovieWinner()) )")
    MovieAwardResponseDTO toResponseModel(MovieAward entity);

    @Mapping(target = "movieWinner", ignore = true)
    @MinimalResponseModel
    MovieAwardResponseDTO toMinimalResponseModel(MovieAward entity);

    @MinimalResponseModel
    default List<MovieAwardResponseDTO> toMinimalResponseCollectionModel(List<MovieAward> entities) {
        return entities.stream()
                .map(this::toMinimalResponseModel)
                .toList();
    }

    default List<MovieAwardResponseDTO> toResponseCollectionModel(List<MovieAward> entities) {
        return entities.stream()
                .map(this::toResponseModel)
                .toList();
    }

    default MovieResponseDTO toResponseModelMovie(Movie movie) {
        return MOVIE_MAPPER.toMinimalResponseModel(movie);
    }

    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    MovieAward toEntity(MovieAwardRequestDTO dto);

    Movie toMovie(IdRequestDTO movie);

    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    MovieAward mergeEntity(MovieAwardRequestDTO dto, @MappingTarget MovieAward entity);

}
