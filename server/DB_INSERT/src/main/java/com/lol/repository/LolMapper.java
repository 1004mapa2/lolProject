package com.lol.repository;

import com.lol.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LolMapper {

    public void insertSummonerDto(SummonerDto summonerDto);

    public Optional<SummonerDto> checkSummonerList(int id);

    public Optional<OriginalDto> checkMatchId(String matchId);

    public void updateSummonerStatus(SummonerDto summonerDto);


    //EACH_TIER_TOTAL 삭제 생성 시작
    public void delete_each_tier_total();

    public void delete_each_tier_total_sequence();

    public void create_each_tier_total();

    public void create_each_tier_total_sequence();

    public void move_each_tier_total(String tierName);
    //EACH_TIER_TOTAL 삭제 생성 끝

    //ALL_TIER_TOTAL 삭제 생성 시작
    public void delete_all_tier_total();

    public void delete_all_tier_total_sequence();

    public void create_all_tier_total();

    public void create_all_tier_total_sequence();

    public void move_all_tier_total();
    //ALL_TIER_TOTAL 삭제 생성 끝


    public Optional<Integer> checkChampionId(int championId);

    public void insertChampionNameDto(ChampionNameDto championNameDto);

    public Optional<List<SummonerDto>> checkSummonerStatus();

    public void resetSummonerStatus();

    public Optional<CombinationDto> checkCombination(CombinationDto combinationDto);

    public void insertCombinationDto(CombinationDto combinationDto);

    public void insertOriginalDto(OriginalDto originalDto);
}
