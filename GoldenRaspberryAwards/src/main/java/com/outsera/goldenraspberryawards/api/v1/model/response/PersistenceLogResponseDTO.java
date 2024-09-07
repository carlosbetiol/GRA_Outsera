package com.outsera.goldenraspberryawards.api.v1.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistenceLogResponseDTO {

    @Schema(example = "12340")
    private Long id;

    @Schema(example = "da39c608-905a-45df-8a9d-b6b0054a5e98")
    private String requestUUID;

    @Schema(example = "Producer")
    private String entityName;

    @Schema(example = "7")
    private Long entityId;

    @Schema(example = "POST")
    private SyslogActionEnum action;

    private JSONObject jsonContent;

    @Schema(example = "2024-09-06T10:35:00Z")
    private OffsetDateTime createdAt;

    private UserResponseDTO user;

}
