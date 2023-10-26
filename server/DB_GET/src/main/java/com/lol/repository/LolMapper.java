package com.lol.repository;

import com.lol.dto.AllTierDto;
import com.lol.dto.ReceiveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LolMapper {

    public List<AllTierDto> getAllTier(ReceiveDto receiveDto);
    public String topToName(String topId);
    public String jungleToName(String jungleId);
    public String middleToName(String middleId);
    public String bottomToName(String bottomId);
    public String utilityToName(String utilityId);
}
