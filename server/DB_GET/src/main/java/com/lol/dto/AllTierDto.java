package com.lol.dto;

import lombok.Data;

@Data
public class AllTierDto {

    private String topName;
    private String jungleName;
    private String middleName;
    private String bottomName;
    private String utilityName;
    private double winRate;
    private int pickCount;

}
