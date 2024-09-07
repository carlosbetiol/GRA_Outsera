package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import com.outsera.goldenraspberryawards.core.validation.EnumValidation;
import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Accessors(chain = true)
public class RequestLogCriteria {

    @Schema(example = "d9b2d63d-8f8a-4b6c-8f5f-9d5d7e9c7a3b", description = "UUID of the request that did the action")
    private List<String> requestUUID;

    @Schema(example = "1", description = "User id")
    private List<Long> userId;

    @Schema(example = "admin@gra.com", description = "User email address")
    private List<String> email;

    @Schema(example = "/v1/producers", description = "Entire or part of the API URL target")
    private String apiUrl;

    @Schema(example = "POST", description = "API method (GET, POST, PUT, PATCH, DELETE)")
    @EnumValidation(enumClass = SyslogActionEnum.class, valuesValid = "GET, POST, PUT, PATCH, DELETE", message = "Invalid method, must be one of: GET, POST, PUT, PATCH, DELETE")
    private List<SyslogActionEnum> method;

    @Schema(example = "producerRequestDTO", description = "Request DTO class name")
    private List<String> requestDTO;

    @Schema(example = "producerResponseDTO", description = "Response DTO class name")
    private List<String> responseDTO;

    @Schema(example = "200", description = "Response status code")
    private List<Integer> responseStatusCode;

    @Schema(example = "2024-09-04T10:00:00Z", description = "Start date of the log in formato yyyy-MM-dd'T'HH:mm:ss'Z' format (UTC)")
    private OffsetDateTime startAt;

    @Schema(example = "2024-09-04T12:00:00Z", description = "Start date of the log in formato yyyy-MM-dd'T'HH:mm:ss'Z' format (UTC)")
    private OffsetDateTime endAt;

    public void resolveMandatoryData() {

        if(isNull(startAt)) {
            startAt = OffsetDateTime.now().toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        if(isNull(endAt)) {
            endAt = OffsetDateTime.now();
        }
    }

}
