package com.lol.repository;

import com.lol.dto.ChampionIdDto;
import com.lol.dto.CombinationDto;
import com.lol.dto.OriginalDto;
import com.lol.dto.SummonerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LolMapper {

    public void insertOriginalDto(OriginalDto originalDto);

    public Optional<CombinationDto> checkCombination(CombinationDto combinationDto);

    public void insertCombinationDto(CombinationDto combinationDto);

    public void insertChampionIdDto(ChampionIdDto championIdDto);

    public void insertSummonerDto(SummonerDto summonerDto);

    public Optional<SummonerDto> getSummonerDto(int id);

    public void updateSummonerStatus(SummonerDto summonerDto);
}
