package com.outsera.goldenraspberryawards.api.v1.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class IdRequestDTO extends GenericTrackableRequestDTO {

    @Schema(example = "1", requiredProperties = "true")
    @NotNull(message = "Id is required")
    private String id;

}
