package com.outsera.goldenraspberryawards.api.v1.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class MovieRequestDTO extends GenericTrackableRequestDTO {

    @Schema(example = "Cruising", requiredProperties = "true")
    @NotBlank(message = "Name is required")
    @Size(min=2,max=150, message = "Name must have between 2 and 150 characters")
    private String name;

    @Size(min = 1, message = "Must be assign to at least one producer")
    @NotNull(message = "Producers is required")
    @Valid
    private List<IdRequestDTO> producers;

    @Size(min = 1, message = "Must be assign to at least one studio")
    @NotNull(message = "Studio is required")
    @Valid
    private List<IdRequestDTO> studios;

}
