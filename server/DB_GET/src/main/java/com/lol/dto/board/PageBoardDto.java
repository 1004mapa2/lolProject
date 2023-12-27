package com.lol.dto.board;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageBoardDto {

    @ApiModelProperty(example = "2")
    private int maxPage;

    private List<BoardListAllInfoDto> boardList;
}
