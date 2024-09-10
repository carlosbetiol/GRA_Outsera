package com.outsera.goldenraspberryawards.api.v1.mapper;

import com.outsera.goldenraspberryawards.api.qualifier.MinimalResponseModel;
import com.outsera.goldenraspberryawards.api.v1.model.request.ProducerRequestDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.AwardBorderResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.MovieResponseDTO;
import com.outsera.goldenraspberryawards.api.v1.model.response.ProducerResponseDTO;
import com.outsera.goldenraspberryawards.domain.model.Movie;
import com.outsera.goldenraspberryawards.domain.model.Producer;
import com.outsera.goldenraspberryawards.domain.model.virtual.AwardBorder;
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
public interface ProducerMapper {

    static ProducerMapper PRODUCER_MAPPER = Mappers.getMapper( ProducerMapper.class );

    AwardBorderResponseDTO toResponseModel(AwardBorder entity);

    @Mapping(target = "movies", expression = "java( entity.getMovies() == null ? null : toResponseCollectionModelMovies(new ArrayList<>(entity.getMovies())) )")
    ProducerResponseDTO toResponseModel(Producer entity);

    default List<MovieResponseDTO> toResponseCollectionModelMovies(List<Movie> movies) {
        return MOVIE_MAPPER.toMinimalResponseCollectionModel(movies);
    }

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
