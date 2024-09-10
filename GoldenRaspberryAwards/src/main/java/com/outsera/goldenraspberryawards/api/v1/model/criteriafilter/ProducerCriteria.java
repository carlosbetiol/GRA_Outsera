package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProducerCriteria extends SearchCriteria {

    @Schema(example = "true", description = "To list just the awarded movies")
    private Boolean justAwardedYears;

}
