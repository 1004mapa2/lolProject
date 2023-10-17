package com.lol.repository;

import com.lol.dto.ChampionIdDto;
import com.lol.dto.CombinationDto;
import com.lol.dto.OriginalDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LolMapper {

    public void insertObj(OriginalDto originalDto);

    public Optional<CombinationDto> checkCombination(CombinationDto combinationDto);

    public void insertCombination(CombinationDto combinationDto);

    public void insertChampionIdDto(ChampionIdDto championIdDto);
}
