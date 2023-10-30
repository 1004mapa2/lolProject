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


    //ALLTIER 삭제 생성 시작
    public void deleteAlltier();

    public void deleteAlltierSeq();

    public void createAlltier();

    public void createAlltierSeq();
    //ALLTIER 삭제 생성 끝


    public void moveTier(String tierName);

    public Optional<Integer> checkChampionId(int championId);

    public void insertChampionNameDto(ChampionNameDto championNameDto);

    public void resetSummonerStatus();

    public Optional<CombinationDto> checkCombination(CombinationDto combinationDto);

    public void insertCombinationDto(CombinationDto combinationDto);

    public void insertOriginalDto(OriginalDto originalDto);
}
