package com.outsera.goldenraspberryawards.domain.model.virtual;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AwardBorder {
    private List<AwardInterval> min;
    private List<AwardInterval> max;
}
