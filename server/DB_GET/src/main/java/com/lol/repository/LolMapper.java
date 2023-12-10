package com.lol.repository;

import com.lol.domain.ChampionName;
import com.lol.domain.Combination_Comment;
import com.lol.dto.detail.ChampionsDto;
import com.lol.dto.main.ReceiveDto;
import com.lol.dto.main.TierDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {
    public List<TierDto> getAllTierDtos(ReceiveDto receiveDto);

    public List<TierDto> getEachTierDtos(ReceiveDto receiveDto);

    public List<ChampionName> getChampionNameDtos(String input);

    public TierDto getDetailInfo(int comsaveId);

    public TierDto getDetailInfo_tier(int comsaveId, String tier);

    public ChampionsDto getChampionNames(int comsaveId);

    public String getChampionKorName(String championName);


    public void saveComment(Combination_Comment combinationComment);

    public List<Combination_Comment> getComment(int comsaveId, int showPage, int numberOfPage);

    public int getMaxPage(int comsaveId);
}
