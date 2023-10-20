package com.lol.repository;

import com.lol.dto.ChampionDto;
import com.lol.dto.ComsaveIdDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LolMapper {

    public Optional<ChampionDto> getComsaveId(ChampionDto championDto);
    public int getPickCount(ComsaveIdDto comsaveIdDto);
    public int getWinCount(ComsaveIdDto comsaveIdDto);
}
