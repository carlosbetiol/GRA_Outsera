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
public class RequestLogResponseDTO  {

    @Schema(example = "51321")
    private Long id;

    @Schema(example = "da39c608-905a-45df-8a9d-b6b0054a5e98")
    private String requestUUID;

    @Schema(example = "POST")
    private SyslogActionEnum method;

    @Schema(example = "/v1/producer")
    private String apiUrl;

    @Schema(example = "ProducerRequestDTO")
    private String requestDTO;

    private JSONObject requestHeaders;

    @Schema(example = "sort=name,asc&size=10&page=0")
    private String requestParameters;

    @Schema(example = "2024-09-06T10:00:00Z")
    private OffsetDateTime requestTime;

    private JSONObject requestContent;

    @Schema(example = "ProducerResponseDTO")
    private String responseDTO;

    private JSONObject responseHeaders;

    @Schema(example = "200")
    private Integer responseStatus;

    @Schema(example = "2024-09-06T10:00:02Z")
    private OffsetDateTime responseTime;

    private JSONObject responseContent;

    @Schema(example = "2024-09-06T10:00:02Z")
    private OffsetDateTime createdAt;

    private UserResponseDTO user;

}
