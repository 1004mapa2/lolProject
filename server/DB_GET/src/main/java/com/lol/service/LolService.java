package com.lol.service;

import com.lol.dto.AllTierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.ReceiveDto;
import com.lol.repository.LolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LolService {

    private final LolMapper mapper;

    public List<AllTierDto> getAllTierInfo(ReceiveDto receiveDto) {
        if (receiveDto.getChampionName1().equals("random") || receiveDto.getChampionName1().equals("emptyBox")) {
            receiveDto.setChampionName1(null);
        }
        if (receiveDto.getChampionName2().equals("random") || receiveDto.getChampionName2().equals("emptyBox")) {
            receiveDto.setChampionName2(null);
        }
        if (receiveDto.getChampionName3().equals("random") || receiveDto.getChampionName3().equals("emptyBox")) {
            receiveDto.setChampionName3(null);
        }
        if (receiveDto.getChampionName4().equals("random") || receiveDto.getChampionName4().equals("emptyBox")) {
            receiveDto.setChampionName4(null);
        }
        if (receiveDto.getChampionName5().equals("random") || receiveDto.getChampionName5().equals("emptyBox")) {
            receiveDto.setChampionName5(null);
        }

        return mapper.getAllTierDtos(receiveDto);
    }

    public List<ChampionNameDto> getChampionNameInfo(String data) {

        return mapper.getChampionNameDtos(data);
    }
}
