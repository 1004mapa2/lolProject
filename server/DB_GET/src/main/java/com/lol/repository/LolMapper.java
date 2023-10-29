package com.lol.repository;

import com.lol.dto.AllTierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.ReceiveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {
    public List<AllTierDto> getAllTierDtos(ReceiveDto receiveDto);

    public List<ChampionNameDto> getChampionNameDtos(String input);
}
