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

        return allTier;
    }
}
