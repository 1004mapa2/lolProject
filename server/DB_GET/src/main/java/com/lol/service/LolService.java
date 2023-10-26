package com.lol.service;

import com.lol.dto.AllTierDto;
import com.lol.dto.ReceiveDto;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LolService {

    private final LolMapper mapper;

    public List<AllTierDto> getAllTierInfo(ReceiveDto receiveDto){
        List<AllTierDto> allTier = mapper.getAllTier(receiveDto);

        for(AllTierDto data : allTier){
            data.setTopId(mapper.topToName(data.getTopId()));
            data.setJungleId(mapper.jungleToName(data.getJungleId()));
            data.setMiddleId(mapper.middleToName(data.getMiddleId()));
            data.setBottomId(mapper.bottomToName(data.getBottomId()));
            data.setUtilityId(mapper.utilityToName(data.getUtilityId()));
        }

        return allTier;
    }
}
