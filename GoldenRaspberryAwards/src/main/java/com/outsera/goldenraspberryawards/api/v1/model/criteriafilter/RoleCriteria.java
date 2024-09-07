package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import com.outsera.goldenraspberryawards.core.validation.EnumValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleCriteria extends SearchCriteria {

    @Schema(example = "ALL", description = "Status of the role: ALL, ACTIVE, INACTIVE")
    @EnumValidation(enumClass = CriteriaStatusEnum.class, valuesValid = "ALL,ACTIVE,INACTIVE", message = "Invalid status, must be one of: ALL, ACTIVE, INACTIVE")
    private CriteriaStatusEnum status;

    @Schema(example = "admin", description = "Role identifier")
    private List<String> identifier;

}
