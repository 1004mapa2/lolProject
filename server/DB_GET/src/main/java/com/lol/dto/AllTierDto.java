package com.lol.dto;

import lombok.Data;

@Data
public class AllTierDto {

    private String topId;
    private String jungleId;
    private String middleId;
    private String bottomId;
    private String utilityId;
    private double winRate;
    private int pickCount;

}
