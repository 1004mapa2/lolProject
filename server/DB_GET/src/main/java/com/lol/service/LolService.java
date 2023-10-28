package com.lol.service;

import com.lol.dto.AllTierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.FindChampionNameDto;
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
        List<AllTierDto> allTier = mapper.getAllTier(receiveDto);

        return allTier;
    }

    public List<ChampionNameDto> getChampionNameInfo(String data) {
        String sort = "CHAMPIONKORNAME";
        if (data.isEmpty()) {
            return mapper.getAllChampionName(sort);
        } else {
            FindChampionNameDto findChampionNameDto = new FindChampionNameDto();
            findChampionNameDto.setSort(sort);
            findChampionNameDto.setInput(data);
            return mapper.getChampionName(findChampionNameDto);
        }

    }
}
