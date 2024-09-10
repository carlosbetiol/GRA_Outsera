package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import com.outsera.goldenraspberryawards.core.validation.EnumValidation;
import com.outsera.goldenraspberryawards.domain.enums.SyslogActionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Accessors(chain = true)
public class PersistenceLogCriteria {

    @Schema(example = "d9b2d63d-8f8a-4b6c-8f5f-9d5d7e9c7a3b", description = "UUID of the request that did the action")
    private List<String> requestUUID;

    @Schema(example = "producer", description = "Entity name")
    private List<String> entityName;

    @Schema(example = "12", description = "Entity id related with entity name")
    private List<Long> entityId;

    @Schema(example = "1", description = "User id")
    private List<Long> userId;

    @Schema(example = "admin@gra.com", description = "User email address")
    private List<String> email;

    @Schema(example = "POST", description = "Create, update or delete actions (POST, PUT, DELETE)")
    @EnumValidation(enumClass = SyslogActionEnum.class, valuesValid = "POST,PUT,DELETE", message = "Invalid action, must be one of: POST, PUT, DELETE")
    private List<SyslogActionEnum> action;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2024-09-04", description = "Start date of the log")
    private LocalDate startDate ;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2024-09-30", description = "End date of the log")
    private LocalDate endDate;

    public void resolveMandatoryData() {

        if(isNull(startDate)) {
            startDate = LocalDate.now().minusDays(30);
        }

        if(isNull(endDate)) {
            endDate = LocalDate.now();
        }
    }

}
