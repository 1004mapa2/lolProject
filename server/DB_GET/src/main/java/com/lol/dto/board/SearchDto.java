package com.lol.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchDto {

    @ApiModelProperty(example = "1")
    private int page;

    @ApiModelProperty(example = "검색 내용")
    private String keyword;

    @ApiModelProperty(example = "정렬 기준")
    private String sort;

    @ApiModelProperty(example = "검색 종류")
    private String searchSort;

    @ApiModelProperty(example = "10")
    private int numberOfPage;

    @ApiModelProperty(example = "1")
    private int startNumber;
}
