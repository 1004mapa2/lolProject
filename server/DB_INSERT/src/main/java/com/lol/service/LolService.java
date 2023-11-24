package com.lol.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lol.dto.ChampionNameDto;
import com.lol.dto.CombinationDto;
import com.lol.dto.OriginalDto;
import com.lol.dto.SummonerDto;
import com.lol.repository.LolMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class LolService {

    private static String apiKey = "";
    private final LolMapper mapper;
    private final WebClient.Builder builder = WebClient.builder();
    private int startTime = 1687359600; //6월 22일 0시
    private int endTime = 1687964400; //6월 28일 0시
    private final static List<String> tier = new ArrayList<>(Arrays.asList("CHALLENGER", "GRANDMASTER", "MASTER", "DIAMOND"));

    public LolService(LolMapper mapper) {
        this.mapper = mapper;
    }

    public void insertDB_userInfo() {
        /**
         * summonerIdDto 얻기
         */
        Gson gson = new Gson();
        for (String tierName : tier) {
            for (int i = 1; i <= 15; i++) {
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
                if (matchList.isEmpty() || matchList == null) { //matchList가 없을 경우 건너뛰기
                    SummonerDto summonerDto = new SummonerDto();
                    summonerDto.setId(id);
                    summonerDto.setStatus("O");
                    mapper.updateSummonerStatus(summonerDto);
                    id++;
                    continue;
                }

                /**
                 * matchData 얻기
                 */
                for (String matchId : matchList) {
                    Optional<OriginalDto> originalMatchId = mapper.checkMatchId(matchId);
                    if (originalMatchId.isPresent() == false) { //matchId 중복이면 실행x
                        try {
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
//                                String championName = playerInfo.get("championName").toString().replaceAll("\"", "");
//                                String teamPosition = playerInfo.get("teamPosition").toString().replaceAll("\"", "");
                                if (j < 5) {
                                    if (setChampionName(originalDto1, combinationDto1, playerInfo))
                                        break;
                                    originalDto1.setTeamId(playerInfo.get("teamId").toString());
                                    originalDto1.setWin(playerInfo.get("win").toString());
                                } else if (j < 10) {
                                    if (setChampionName(originalDto2, combinationDto2, playerInfo))
                                        break;
                                    originalDto2.setTeamId(playerInfo.get("teamId").toString());
                                    originalDto2.setWin(playerInfo.get("win").toString());
                                }
                                if (j == participantsData.size() - 1) {
                                    setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, originalDto1, combinationDto1);
                                    setDataAndInsertDB(tier, matchId, formatGameTime, formatNowTime, originalDto2, combinationDto2);
                                }
                            } //end for(match)
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(matchId);
                        }//end try-catch
                    } //end if
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
        resetSummonerStatus(); //소환사 status X로 초기화
        updateTimeStamp(); //일주일 늘리기
    } //end method

    public void moveDB_each_tier_total() {
        //삭제
        mapper.delete_each_tier_total();
        mapper.delete_each_tier_total_sequence();
        //생성
        mapper.create_each_tier_total();
        mapper.create_each_tier_total_sequence();
        //옮기기
        for (String tierName : tier) {
            mapper.move_each_tier_total(tierName);
        }
    } //end method

    public void moveDB_all_tier_total() {
        //삭제
        mapper.delete_all_tier_total();
        mapper.delete_all_tier_total_sequence();
        //생성
        mapper.create_all_tier_total();
        mapper.create_all_tier_total_sequence();
        //옮기기
        mapper.move_all_tier_total();
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

    public void resetSummonerStatus() {
        Optional<List<SummonerDto>> summonerDtos = mapper.checkSummonerStatus();
        if (summonerDtos.isEmpty()) {
            mapper.resetSummonerStatus();
        } else {
            try {
                throw new Exception("전체 스캔 안함");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateAPIKEY() {
        try {
            String url = "https://developer.riotgames.com/";
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");

            Runtime.getRuntime().exec("C:/Program Files/Google/Chrome/Application/chrome.exe --remote-debugging-port=9222 --user-data-dir=\"C:/Selenium/ChromeData\"");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("-remote-allow-origins=*");
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            System.setProperty("webdriver.http.factory", "jdk-http-client");
            WebDriver driver = new ChromeDriver(options);
            driver.get(url);
            if (driver.findElement(By.className("admin-title")).getAttribute("innerHTML").equals("Login")) { //로그인을 해야 된다면
                driver.findElement(By.className("admin-title")).click();
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div/form")).click();
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div/form/div/div/div[1]/div[4]/button[2]")).click();
                driver.findElement(By.xpath("//*[@id=\"view_container\"]/div/div/div[2]/div/div[1]/div/form/span/section/div/div/div/div/ul/li[1]/div/div[1]")).click();

                expiredKey(driver);
            } else {
                expiredKey(driver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTimeStamp() {
        startTime += 604800;
        endTime += 604800;
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

    private static boolean setChampionName(OriginalDto originalDto, CombinationDto combinationDto, JsonObject playerInfo) {
        String championName = playerInfo.get("championName").toString().replaceAll("\"", "");
        String teamPosition = playerInfo.get("teamPosition").toString().replaceAll("\"", "");
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
        String data = "";
        try {
            data = builder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 401 || e.getRawStatusCode() == 403) { //인증이 만료된 경우
                updateAPIKEY();
                try {
                    Thread.sleep(30000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                String cutStr = "api_key=";
                int i = url.indexOf(cutStr);
                String substring = url.substring(0, i + cutStr.length());
                String fixUrl = substring + apiKey;

                return builder.build().get().uri(fixUrl).retrieve().bodyToMono(String.class).block();
            } else if (e.getRawStatusCode() == 500 || e.getRawStatusCode() == 503 || e.getRawStatusCode() == 429) { //서버 쪽 문제 or 요청 초과
                try {
                    Thread.sleep(10000);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
        return data;
    }

    private static void expiredKey(WebDriver driver) throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        if (!driver.findElements(By.className("riotred")).isEmpty()) {
            WebElement iframe = driver.findElement(By.xpath("/html/body/div[2]/div/form/div[3]/div/div[3]/div[2]/div[1]/div/div/iframe"));
            driver.switchTo().frame(iframe);
            driver.findElement(By.className("recaptcha-checkbox-border")).click();
            driver.switchTo().defaultContent();
            Thread.sleep(5000);
            driver.findElement(By.name("confirm_action")).click();
        }
        WebElement newKeyBox = driver.findElement((By.id("apikey")));
        String newKey = newKeyBox.getAttribute("value");
        apiKey = newKey;
        driver.close();
    }
}