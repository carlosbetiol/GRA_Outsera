package com.outsera.goldenraspberryawards.api.v1.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MovieAwardRequestDTO extends GenericTrackableRequestDTO {

    @Schema(example = "Cruising", requiredProperties = "true")
    @NotNull(message = "Name is required")
    @Min(value = 1, message = "Invalid year")
    private Integer awardYear;

    @NotNull(message = "Movie is required")
    @Valid
    private IdRequestDTO movieWinner;

}
