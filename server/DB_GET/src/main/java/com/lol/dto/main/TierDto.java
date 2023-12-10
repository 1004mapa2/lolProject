package com.lol.dto.main;

import lombok.Data;

import java.util.List;

@Data
public class TierDto {

    private int comsaveId;
    private String topName;
    private String jungleName;
    private String middleName;
    private String bottomName;
    private String utilityName;
    private double winRate;
    private int pickCount;
    private List<String> championKorNames;
}
