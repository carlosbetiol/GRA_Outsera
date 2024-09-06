package com.outsera.goldenraspberryawards.api.v1.model.criteriafilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StudioCriteria extends SearchCriteria {

    private Boolean justAwardedYears;

}
