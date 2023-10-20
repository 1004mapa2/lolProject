package com.lol.service;

import com.lol.dto.ChampionDto;
import com.lol.dto.ComsaveIdDto;
import com.lol.dto.CountDto;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LolService {

    private final LolMapper mapper;

    public Optional<CountDto> getCount(){

        ChampionDto championDto = new ChampionDto();
        championDto.setTopId("43");
        championDto.setJungleId("64");
        championDto.setMiddleId("517");
        championDto.setBottomId("145");
        championDto.setUtilityId("432");
        Optional<ChampionDto> comsaveInfo = mapper.getComsaveId(championDto);
        if(comsaveInfo.isPresent()){
            ComsaveIdDto comsaveIdDto = new ComsaveIdDto();
            int comsaveId = comsaveInfo.get().getId();
            comsaveIdDto.setId(comsaveId);

            int pickCount = mapper.getPickCount(comsaveIdDto);
            int winCount = mapper.getWinCount(comsaveIdDto);
            double winRate =(double) winCount / pickCount;

            CountDto countDto = new CountDto();
            countDto.setPickCount(pickCount);
            countDto.setWinRate(winRate);
            return Optional.of(countDto);
        } else {
            return null;
        }
    }
}
