package com.outsera.goldenraspberryawards.api.v1.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardIntervalResponseDTO {

    @Schema(example = "Felipe Beline", description = "Producer name")
    private String producer;

    @Schema(example = "3", description = "Interval in years")
    private Integer interval;

    @Schema(example = "1990", description = "Previous win year")
    private Integer previousWin;

    @Schema(example = "1993", description = "Following win year")
    private Integer followingWin;


}
