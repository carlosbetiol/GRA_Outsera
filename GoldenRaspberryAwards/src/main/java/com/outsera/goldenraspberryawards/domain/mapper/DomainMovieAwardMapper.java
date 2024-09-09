package com.outsera.goldenraspberryawards.domain.mapper;

import com.outsera.goldenraspberryawards.domain.model.MovieAward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;

@Mapper(
        imports = {
                OffsetDateTime.class
        }
)
public interface DomainMovieAwardMapper {

    static DomainMovieAwardMapper DOMAIN_MOVIE_AWARD_MAPPER = Mappers.getMapper( DomainMovieAwardMapper.class );

    @Mapping(target = "awardYear", source = "year")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    MovieAward createEntity(Integer year);
    
}
