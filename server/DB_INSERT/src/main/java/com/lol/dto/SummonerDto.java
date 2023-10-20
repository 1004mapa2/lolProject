package com.lol.dto;

import lombok.Data;

@Data
public class SummonerDto {

    private int id;
    private String tier;
    private String summonerId;
    private String summonerName;
    private String status;
}
