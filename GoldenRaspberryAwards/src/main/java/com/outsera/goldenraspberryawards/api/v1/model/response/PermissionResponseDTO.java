package com.outsera.goldenraspberryawards.api.v1.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "PRC_VIEW")
    private String identifier;

    @Schema(example = "View producer")
    private String description;

    @Schema(example = "2024-09-10T10:00:00Z")
    private OffsetDateTime createdAt;

    @Schema(example = "2024-09-10T10:00:00Z")
    private OffsetDateTime updatedAt;

    private List<RoleResponseDTO> roles;

}
