package com.outsera.goldenraspberryawards.api.v1.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class StudioRequestDTO extends GenericTrackableRequestDTO {

    @Schema(example = "Lorimar Productions", requiredProperties = "true")
    @NotBlank(message = "Name is required")
    @Size(min=2,max=150, message = "Name must have between 2 and 150 characters")
    private String name;

}
