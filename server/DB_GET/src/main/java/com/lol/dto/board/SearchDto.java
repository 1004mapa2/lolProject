package com.lol.dto.board;

import lombok.Data;

@Data
public class SearchDto {

    private int page;
    private String keyword;
    private String sort;
    private String searchSort;
    private int numberOfPage;
    private int startNumber;
}
