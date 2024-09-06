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

    @Schema(example = "2018", description = "Year of the award")
    private List<Integer> awardYear;

}
