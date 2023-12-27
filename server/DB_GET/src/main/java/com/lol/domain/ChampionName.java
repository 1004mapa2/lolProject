package com.lol.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChampionName {

    @ApiModelProperty(example = "1")
    private int id;

    @ApiModelProperty(example = "Garen")
    private String ChampionEngName;

    @ApiModelProperty(example = "가렌")
    private String ChampionKorName;
}
