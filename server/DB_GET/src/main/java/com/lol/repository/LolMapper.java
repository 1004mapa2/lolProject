package com.lol.repository;

import com.lol.dto.Combination_Comment;
import com.lol.dto.TierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.ReceiveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {
    public List<TierDto> getAllTierDtos(ReceiveDto receiveDto);

    public List<TierDto> getEachTierDtos(ReceiveDto receiveDto);

    public List<ChampionNameDto> getChampionNameDtos(String input);

    public List<TierDto> getAlltierLoseComsave(String comsaveId);

    public TierDto getAlltierSelectComsave(String comsaveId);

    public List<TierDto> getLoseComsave(String tier, String comsaveId);

    public TierDto getSelectComsave(String tier, String comsaveId);

    public void saveComment(Combination_Comment combinationComment);

    public List<Combination_Comment> getComment(int comsaveId);
}
