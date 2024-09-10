package com.outsera.goldenraspberryawards.domain.model.virtual;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AwardInterval {

    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;

}
