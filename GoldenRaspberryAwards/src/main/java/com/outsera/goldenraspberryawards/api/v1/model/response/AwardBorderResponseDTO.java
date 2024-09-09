package com.outsera.goldenraspberryawards.api.v1.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardBorderResponseDTO {

    @Schema(description = "List of producers with the minimum interval between awards")
    private List<AwardIntervalResponseDTO> min;

    @Schema(description = "List of producers with the maximum interval between awards")
    private List<AwardIntervalResponseDTO> max;


}
