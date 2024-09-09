package com.outsera.goldenraspberryawards.domain.mapper;

import com.outsera.goldenraspberryawards.domain.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.HashSet;

@Mapper(
        imports = {
                OffsetDateTime.class,
                HashSet.class
        }
)
public interface DomainMovieMapper {

    static DomainMovieMapper DOMAIN_MOVIE_MAPPER = Mappers.getMapper( DomainMovieMapper.class );

    @Mapping(target = "name", source = "name")
    @Mapping(target = "studios", expression = "java(new HashSet<>())")
    @Mapping(target = "producers", expression = "java(new HashSet<>())")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(OffsetDateTime.now())")
    Movie createEntity(String name);
    
}
