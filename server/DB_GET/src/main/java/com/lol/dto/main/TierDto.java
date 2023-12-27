package com.lol.dto.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TierDto {

    @ApiModelProperty(example = "4480")
    private int comsaveId;

    @ApiModelProperty(example = "top 포지션 영어 이름: (Aatrox)")
    private String topName;

    @ApiModelProperty(example = "jungle 포지션 영어 이름: (LeeSin)")
    private String jungleName;

    @ApiModelProperty(example = "middle 포지션 영어 이름: (Akali)")
    private String middleName;

    @ApiModelProperty(example = "bottom 포지션 영어 이름: (Xayah)")
    private String bottomName;

    @ApiModelProperty(example = "utility 포지션 영어 이름: (Rakan)")
    private String utilityName;

    @ApiModelProperty(example = "0.67")
    private double winRate;

    @ApiModelProperty(example = "6")
    private int pickCount;

    @ApiModelProperty(example = "챔피언 한글 이름 리스트: ()")
    private List<String> championKorNames;
}
