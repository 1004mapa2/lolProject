package com.lol.repository;

import com.lol.dto.AllTierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.FindChampionNameDto;
import com.lol.dto.ReceiveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {

    public List<AllTierDto> getAllTier(ReceiveDto receiveDto);

    public List<ChampionNameDto> getAllChampionName(String sort);

    public List<ChampionNameDto> getChampionName(FindChampionNameDto findChampionNameDto);
}
