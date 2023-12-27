package com.lol.dto.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceiveDto {

    @ApiModelProperty(example = "top 포지션 영어 이름: (Aatrox)")
    private String championName1;

    @ApiModelProperty(example = "jungle 포지션 영어 이름: (LeeSin)")
    private String championName2;

    @ApiModelProperty(example = "middle 포지션 영어 이름: (Akali)")
    private String championName3;

    @ApiModelProperty(example = "bottom 포지션 영어 이름: (Xayah)")
    private String championName4;

    @ApiModelProperty(example = "utility 포지션 영어 이름: (Rakan)")
    private String championName5;

    @ApiModelProperty(example = "티어 영어 명칭: (CHALLENGER)")
    private String tier;

    @ApiModelProperty(example = "픽 횟수 순: (PICKCOUNT) or 승률 순: (WINRATE)")
    private String sort;
}
