package com.lol.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lol.dto.*;
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
    private String startTime = "1696431600";
    private String endTime = "1696798800";
    private static List<String> tier = new ArrayList<>(Arrays.asList("CHALLENGER", "GRANDMASTER", "MASTER"));

    public LolService(LolMapper mapper) {
        this.mapper = mapper;
    }

    public void insertDB_userInfo() {
        /**
         * summonerIdDto 얻기
         */
        Gson gson = new Gson();
        for (String tierName : tier) {
            for (int i = 1; i <= 5; i++) {
                String summonerIdUrl = "https://kr.api.riotgames.com/lol/league-exp/v4/entries/RANKED_SOLO_5x5/" + tierName + "/I?page=" + i + "&api_key=" + apiKey; //page5면 1000명 정도
                String usersInfo = getGsonData(summonerIdUrl, builder);
                JsonArray dataToArray = gson.fromJson(usersInfo, JsonArray.class);

                for (int j = 0; j < dataToArray.size(); j++) {
                    SummonerDto summonerDto = new SummonerDto();
                    JsonObject userInfo = (JsonObject) dataToArray.get(j);
                    String tier = userInfo.get("tier").toString().replaceAll("\"", "");
                    String summonerId = userInfo.get("summonerId").toString().replaceAll("\"", "");
                    String summonerName = userInfo.get("summonerName").toString().replaceAll("\"", "");
                    summonerDto.setTier(tier);
                    summonerDto.setSummonerId(summonerId);
                    summonerDto.setSummonerName(summonerName);
                    summonerDto.setStatus("X");
                    mapper.insertSummonerDto(summonerDto);
                } //end for
            } //end for
        }
    } //end method

    public void insertDB_matchInfo() {
        /**
         * puuid 얻기
         */
        Gson gson = new Gson();

        int id = 1;
        int count = 0;
        while (true) {
            Optional<SummonerDto> userInfo = mapper.checkSummonerList(id);
            if (userInfo.isPresent() && userInfo.get().getStatus().equals("X")) {
                /**
                 * puuid 얻기
                 */
                String summonerId = userInfo.get().getSummonerId();
                String tier = userInfo.get().getTier();
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
                List<String> matchList = gson.fromJson(matchListData, List.class);

                /**
                 * matchData 얻기
                 */
                for (String matchId : matchList) {
                    Optional<OriginalDto> originalMatchId = mapper.checkMatchId(matchId);
                    if (originalMatchId.isPresent() == false) { //matchId 중복이면 실행x
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
                        OriginalDto originalDto1 = new OriginalDto(); //1팀 객체 생성
                        OriginalDto originalDto2 = new OriginalDto(); //2팀 객체 생성
                        CombinationDto combinationDto1 = new CombinationDto();
                        CombinationDto combinationDto2 = new CombinationDto();

                        for (int j = 0; j < participantsData.size(); j++) {
                            JsonObject playerInfo = (JsonObject) participantsData.get(j);
                            String championName = playerInfo.get("championName").toString().replaceAll("\"", "");
                            String teamPosition = playerInfo.get("teamPosition").toString().replaceAll("\"", "");
                            if (j < 5) {
                                if (setChampionName(originalDto1, combinationDto1, championName, teamPosition)) break;
                                originalDto1.setTeamId(playerInfo.get("teamId").toString());
                                originalDto1.setWin(playerInfo.get("win").toString());
                            } else if (j < 10) {
                                if (setChampionName(originalDto2, combinationDto2, championName, teamPosition)) break;
                                originalDto2.setTeamId(playerInfo.get("teamId").toString());
                                originalDto2.setWin(playerInfo.get("win").toString());
                            }
                            if (j == participantsData.size() - 1) {
                                setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, originalDto1, combinationDto1);
                                setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, originalDto2, combinationDto2);
                            }
                        } //end for(match)
                    }
                } //end for(matchList)
                SummonerDto summonerDto = new SummonerDto();
                summonerDto.setId(id);
                summonerDto.setStatus("O");
                mapper.updateSummonerStatus(summonerDto);
            } else if (userInfo.isPresent() && userInfo.get().getStatus().equals("O")) {
            } else {
                break;
            } //end if(userInfo)
            id++;
        } //end while
    } //end method

    public void moveDB_originalToTier() {
        //테이블 삭제 후 다시 생성
        mapper.deleteAlltier();
        mapper.deleteAlltierSeq();
        mapper.createAlltier();
        mapper.createAlltierSeq();
        for (String tierName : tier) {
            mapper.moveTier(tierName);
        }
    } //end method

    public void insertChampionId() {
        try {
            Reader reader1 = new FileReader("C:\\Users\\1004m\\Desktop\\tgzFile\\13.20.1\\data\\ko_KR\\championId.json");
            FileReader reader2 = new FileReader("C:\\Users\\1004m\\Desktop\\tgzFile\\13.20.1\\data\\ko_KR\\champion.json");
            Gson gson = new Gson();
            JsonObject data1 = (JsonObject) gson.fromJson(reader1, JsonObject.class).get("keys");
            JsonObject data2 = (JsonObject) gson.fromJson(reader2, JsonObject.class).get("data");
            Set<String> dataKeySet = data1.keySet();
            Iterator<String> iterator = dataKeySet.iterator();
            while (iterator.hasNext()) {
                ChampionNameDto championNameDto = new ChampionNameDto();
                int championId = Integer.parseInt(iterator.next());
                Optional<Integer> optionalId = mapper.checkChampionId(championId);
                if (optionalId.isEmpty()) {
                    String championEngName = data1.get(championId + "").toString().replaceAll("\"", "");
                    JsonObject championData = (JsonObject) data2.get(championEngName);
                    String championKorName = championData.get("name").toString().replaceAll("\"", "");
                    championNameDto.setId(championId);
                    championNameDto.setChampionEngName(championEngName);
                    championNameDto.setChampionKorName(championKorName);
                    mapper.insertChampionNameDto(championNameDto);
                } //end if
            } //end while
        } catch (Exception e) {
            e.printStackTrace();
        } //end try-catch
    } //end method

    public void resetSummonerStatus(){
        mapper.resetSummonerStatus();
    }

    /*-- 메서드 추출 --*/
    private static int countCheck(int count) {
        if (count == 100) { //2분 5초 기다리기
            try {
                count = 0;
                System.out.println("sleep");
                Thread.sleep(125000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            count++;
        }
        return count;
    }

    private static boolean setChampionName(OriginalDto originalDto, CombinationDto combinationDto, String championName, String teamPosition) {
        if (teamPosition.equals("TOP")) {
            originalDto.setTopName(championName);
            combinationDto.setTopName(championName);
        } else if (teamPosition.equals("JUNGLE")) {
            originalDto.setJungleName(championName);
            combinationDto.setJungleName(championName);
        } else if (teamPosition.equals("MIDDLE")) {
            originalDto.setMiddleName(championName);
            combinationDto.setMiddleName(championName);
        } else if (teamPosition.equals("BOTTOM")) {
            originalDto.setBottomName(championName);
            combinationDto.setBottomName(championName);
        } else if (teamPosition.equals("UTILITY")) {
            originalDto.setUtilityName(championName);
            combinationDto.setUtilityName(championName);
        } else {
            System.out.println("오류");
            return true;
        }
        return false;
    }

    private void setDataAndInsertDB(String tier, String matchId, String formatGameTime, String formatNowTime, OriginalDto originalDto, CombinationDto combinationDto) {
        Optional<CombinationDto> comData = mapper.checkCombination(combinationDto);
        if (comData.isPresent()) { //조합 테이블에 조합 ID가 있으면 그 ID를 obj 객체에 넣기
            int comSaveId = comData.get().getId();
            originalDto.setComSaveId(comSaveId);
        } else { //조합 테이블에 조합 ID가 없으면 새로 DB에 넣고 그 comSaveId 가져오기
            mapper.insertCombinationDto(combinationDto);
            int comSaveId = mapper.checkCombination(combinationDto).get().getId();
            originalDto.setComSaveId(comSaveId);
        }
        originalDto.setGameTime(formatGameTime);
        originalDto.setInsertTime(formatNowTime);
        originalDto.setTier(tier);
        originalDto.setMatchId(matchId);
        mapper.insertOriginalDto(originalDto);
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