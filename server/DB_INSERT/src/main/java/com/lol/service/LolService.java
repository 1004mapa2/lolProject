package com.lol.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lol.dto.ChampionIdDto;
import com.lol.dto.CombinationDto;
import com.lol.dto.OriginalDto;
import com.lol.dto.SummonerDto;
import com.lol.repository.LolMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LolService {

    private final String apiKey = "";
    private final LolMapper mapper;
    private final WebClient.Builder builder = WebClient.builder(); //url 호출할 때마다 필요해서 메모리 절약을 위해 생성
    private String startTime = "1696971600"; //10월11일 6시 유닉스 타임스탬프
    private String endTime = "1697058000"; //10월12일 6시 유닉스 타임스탬프

    public LolService(LolMapper mapper) {
        this.mapper = mapper;
    }

    public void insertDB_userInfo(String tier) {
        /**
         * summonerIdDto 얻기
         */
        Gson gson = new Gson();

        for (int i = 1; i <= 5; i++) {
            String summonerIdUrl = "https://kr.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/" + tier + "/I?page=" + i + "&api_key=" + apiKey; //page5면 1000명 정도
            String usersInfo = getGsonData(summonerIdUrl, builder);
            JsonArray dataToArray = gson.fromJson(usersInfo, JsonArray.class);

            for (int j = 0; j < dataToArray.size(); j++) {
                SummonerDto summonerDto = new SummonerDto();
                JsonObject userInfo = (JsonObject) dataToArray.get(j);
                String summonerId = userInfo.get("summonerId").toString().replaceAll("\"", "");
                String summonerName = userInfo.get("summonerName").toString().replaceAll("\"", "");
                summonerDto.setSummonerId(summonerId);
                summonerDto.setSummonerName(summonerName);
                summonerDto.setStatus("X");
                mapper.insertSummonerDto(summonerDto);
            } //end for
        } //end for
    } //end method

    public void insertDB_matchInfo(String tier) {
        /**
         * puuid 얻기
         */
        Gson gson = new Gson();

        int id = 1;
        int count = 0;
        while (true) {
            Optional<SummonerDto> userInfo = mapper.getSummonerDto(id);
            if (userInfo.isPresent()) {
                /**
                 * puuid 얻기
                 */
                String summonerId = userInfo.get().getSummonerId();
                String puuidUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/" + summonerId + "?api_key=" + apiKey;
                count = countCheck(count);
                String summonerData = getGsonData(puuidUrl, builder);
                JsonObject jsonObject = gson.fromJson(summonerData, JsonObject.class);
                String puuid = jsonObject.get("puuid").toString().replaceAll("\"", "");

                /**
                 * matchList 얻기
                 */
                String matchListUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" +
                        puuid +
                        "/ids?startTime=" + startTime + "&endTime=" + endTime + "&type=ranked&start=0&count=100&api_key=" + apiKey;
                count = countCheck(count);
                String matchListData = getGsonData(matchListUrl, builder);
                System.out.println(matchListData);
                List<String> matchList = gson.fromJson(matchListData, List.class);

                /**
                 * matchData 얻기
                 */
                for (String matchId : matchList) {
                    String matchUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;
                    count = countCheck(count);
                    String matchData = getGsonData(matchUrl, builder);
                    JsonObject infoData = (JsonObject) gson.fromJson(matchData, JsonObject.class).get("info");

                    //현재 시간
                    LocalDateTime now = LocalDateTime.now();
                    String formatNowTime = now.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));

                    //timestamp YYYY-MM-dd로 바꾸기
                    JsonElement endTimestamp = infoData.get("gameEndTimestamp");
                    long timestamp = Long.parseLong(endTimestamp.toString());
                    Date date = new Date();
                    date.setTime(timestamp);
                    SimpleDateFormat formatDate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    String formatGameTime = formatDate.format(date);

                    JsonArray participantsData = (JsonArray) infoData.get("participants");
                    OriginalDto obj = new OriginalDto(); //1팀 객체 생성
                    CombinationDto combinationDto = new CombinationDto(); //COMSAVE COLUMN 만들기

                    for (int j = 0; j < participantsData.size(); j++) {
                        JsonObject playerInfo = (JsonObject) participantsData.get(j);
                        String championId = playerInfo.get("championId").toString();
                        String teamPosition = playerInfo.get("teamPosition").toString().replaceAll("\"", "");
                        if (teamPosition.equals("TOP")) {
                            obj.setTopId(championId);
                            combinationDto.setTopId(championId);
                        } else if (teamPosition.equals("JUNGLE")) {
                            obj.setJungleId(championId);
                            combinationDto.setJungleId(championId);
                        } else if (teamPosition.equals("MIDDLE")) {
                            obj.setMiddleId(championId);
                            combinationDto.setMiddleId(championId);
                        } else if (teamPosition.equals("BOTTOM")) {
                            obj.setBottomId(championId);
                            combinationDto.setBottomId(championId);
                        } else if (teamPosition.equals("UTILITY")) {
                            obj.setUtilityId(championId);
                            combinationDto.setUtilityId(championId);
                        } else {
                            System.out.println("오류");
                            //try-catch 써보기
                        }

                        if (j == 4) {
                            setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, obj, combinationDto, playerInfo);
                        } else if (j == participantsData.size() - 1) {
                            setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, obj, combinationDto, playerInfo);
                        }
                    }
                    SummonerDto summonerDto = new SummonerDto();
                    summonerDto.setId(id);
                    summonerDto.setStatus("O");
                    mapper.updateSummonerStatus(summonerDto);
                }
            } else {
                break;
            } //end if
            id++;
        } //end while
    } //end method

    private static int countCheck(int count) {
        if (count == 100) { //2분 5초 기다리기
            try {
                count = 0;
                Thread.sleep(125000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            count++;
        }
        return count;
    }

    private void setDataAndInsertDB(String tier, String matchId, String formatGameTime, String
            formatNowTime, OriginalDto obj, CombinationDto combinationDto, JsonObject playerInfo) {
        Optional<CombinationDto> comData = mapper.checkCombination(combinationDto);
        if (comData.isPresent()) { //조합 테이블에 조합 ID가 있으면 그 ID를 obj 객체에 넣기
            int comSaveId = comData.get().getId();
            obj.setComSaveId(comSaveId);
        } else { //조합 테이블에 조합 ID가 없으면 새로 DB에 넣고 그 comSaveId 가져오기
            mapper.insertCombinationDto(combinationDto);
            int comSaveId = mapper.checkCombination(combinationDto).get().getId();
            obj.setComSaveId(comSaveId);
        }
        String teamId = playerInfo.get("teamId").toString();
        String win = playerInfo.get("win").toString();
        obj.setTeamId(teamId);
        obj.setWin(win);
        obj.setGameTime(formatGameTime);
        obj.setInsertTime(formatNowTime);
        obj.setTier(tier);
        obj.setMatchId(matchId);
        mapper.insertOriginalDto(obj);
    }

    public void insertChampionId() {
        try {
            Reader reader = new FileReader("C:\\Users\\1004m\\Desktop\\tgzFile\\13.20.1\\data\\ko_KR\\championId.json");
            Gson gson = new Gson();
            JsonObject data = (JsonObject) gson.fromJson(reader, JsonObject.class).get("keys");
            Set<String> dataKeySet = data.keySet();
            Iterator<String> iterator = dataKeySet.iterator();
            while (iterator.hasNext()) {
                ChampionIdDto championIdDto = new ChampionIdDto();
                int championId = Integer.parseInt(iterator.next());
                String championName = data.get(championId + "").toString().replaceAll("\"", "");
                championIdDto.setId(championId);
                championIdDto.setChampionName(championName);
                mapper.insertChampionIdDto(championIdDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getGsonData(String url, WebClient.Builder builder) {
        String data = builder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return data;
    }
}