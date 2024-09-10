package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import com.outsera.goldenraspberryawards.core.validation.EnumValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

import static java.util.Objects.isNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RoleCriteria extends SearchCriteria {

    @Schema(example = "ALL", description = "Status of the role: ALL, ACTIVE, INACTIVE")
    @EnumValidation(enumClass = CriteriaStatusEnum.class, valuesValid = "ALL,ACTIVE,INACTIVE", message = "Invalid status, must be one of: ALL, ACTIVE, INACTIVE")
    private CriteriaStatusEnum status;

    @Schema(example = "PRC_VIEW", description = "Permission identifier")
    private List<String> permissionIdentifier;

    @Schema(example = "2", description = "User id")
    private List<Integer> userId;

    @Schema(example = "admin@gra.com", description = "User email")
    private List<String> userEmail;

    public void resolveMandatoryData() {

        if(isNull(status)) {
            status = CriteriaStatusEnum.ALL;
        }

    }

}
