package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MovieAwardCriteria extends SearchCriteria {

    @Schema(example = "2023", description = "Filter the movies by awarded year")
    private List<Integer> awardYear;

    @Schema(example = "30", description = "Movie id to filter")
    private List<Integer> movieId;

    @Schema(example = "3", description = "Studio id to filter")
    private List<Integer> studioId;

    @Schema(example = "7", description = "Producer id to filter")
    private List<Integer> producerId;

}
