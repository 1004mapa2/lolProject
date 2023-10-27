package com.lol.repository;

import com.lol.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LolMapper {

    public void insertSummonerDto(SummonerDto summonerDto);

    public Optional<SummonerDto> checkSummonerList(int id);

    public Optional<OriginalDto> checkMatchId(String matchId);

    public void updateSummonerStatus(SummonerDto summonerDto);

    public void moveTier();

    public Optional<CombinationDto> checkCombination(CombinationDto combinationDto);

    public void insertCombinationDto(CombinationDto combinationDto);

    public void insertOriginalDto(OriginalDto originalDto);

    public void insertChampionIdDto(ChampionIdDto championIdDto);


}
