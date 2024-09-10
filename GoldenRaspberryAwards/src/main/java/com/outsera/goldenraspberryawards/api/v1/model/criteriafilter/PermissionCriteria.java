package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PermissionCriteria extends SearchCriteria {

    @Schema(example = "PRC_VIEW", description = "View producer")
    private List<String> permissionIdentifier;

    @Schema(example = "admin", description = "Role identifier")
    private List<String> roleIdentifier;

    @Schema(example = "2", description = "User id")
    private List<Integer> userId;

    @Schema(example = "admin@gra.com", description = "User email")
    private List<String> userEmail;

}
