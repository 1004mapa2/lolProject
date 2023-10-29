package com.lol.controller;

import com.google.gson.Gson;
import com.lol.dto.AllTierDto;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.ReceiveDto;
import com.lol.service.LolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class LolController {

    private final LolService lolService;
    @PostMapping("/1")
    public String getAllTierInfo(@RequestBody ReceiveDto data){
        List<AllTierDto> allTierInfo = lolService.getAllTierInfo(data);
        Gson gson = new Gson();
        String json = gson.toJson(allTierInfo);

        return json;
    }

    @PostMapping("/2")
    public String getChampionNameInfo(@RequestBody String data){
        Gson gson = new Gson();
        String reData = gson.fromJson(data, String.class);
        List<ChampionNameDto> championNameDtos = lolService.getChampionNameInfo(reData);
        String json = gson.toJson(championNameDtos);

        return json;
    }
}
