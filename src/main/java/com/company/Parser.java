package com.company;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Dotson on 2018-02-10.
 */
public class Parser {
    String myDirectoryPath = "C:/Users/Dotson/Documents/GitHub/ATGScrapper/Data2017";
    private static final String FILE_NAME = "C:/Users/Dotson/Documents/GitHub/ATGScrapper/tmp/Atg.xlsx";

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String HEADER_LINE = "StartMethod,StartNumber,Odds,Track,Firstprize,Distance,Front,Back,Driver,LastPlace,LastStart,Season,TrackCondition,Place";




    File dir = new File(myDirectoryPath);
    File[] files = dir.listFiles();


    HashMap<String, Integer> lastPlaceMap = new HashMap<String, Integer>();
    HashMap<String, String> lastStartMap = new HashMap<String, String>();




    public void createCSVForHorse(org.json.JSONObject jsonObject, int Avd, int AvdNumber) {
        boolean first = true;
        int place = 0;
        int lastPlace = 0;
        String date = "";
        String lastDate = "";


        org.json.JSONObject horse = (org.json.JSONObject) jsonObject.get("horse");


        String file_name = "C:/Users/Dotson/Documents/GitHub/ATGScrapper/tmp/Avd" + Avd  + "_" + AvdNumber + ".csv";


        org.json.JSONObject results = (org.json.JSONObject) horse.get("results");
        org.json.JSONArray records = (org.json.JSONArray) results.get("records");
        String horseName = (String) horse.get("name");

        FileWriter writer;
        List<RaceInfo> raceList = new ArrayList();


        for(int i = records.length()-1; i >= 0; i--){
            if(i < records.length()-2){
                first = false;
            }
            boolean scratched = false;

            org.json.JSONObject race = (org.json.JSONObject) records.get(i);
            org.json.JSONObject raceInfo = (org.json.JSONObject) race.get("race");
            org.json.JSONObject start = (org.json.JSONObject) race.get("start");
            org.json.JSONObject track = (org.json.JSONObject) race.get("track");
            org.json.JSONObject driver = (org.json.JSONObject) start.get("driver");
            org.json.JSONObject horseShoe = (org.json.JSONObject) start.get("horse");

            try{
                scratched  =(Boolean) race.get("scratched");
            } catch (Exception e){

            }

            String countryCode = (String) track.get("countryCode");
            if(countryCode.matches("SE") && !scratched){


            lastDate = date;
            date = (String) race.get("date");
            String[] months = date.split("-");
            String month = months[1];
            int month_i = Integer.valueOf(month);
            String season = ConditionUtil.getSeason(month_i);

            String trackName = (String) track.get("name");
            String trackCondition = (String) track.get("condition");
            String driverName = (String) driver.get("shortName");

            int distance = Integer.parseInt(start.get("distance").toString());
            int startNumber = Integer.parseInt(start.get("postPosition").toString());



            int priceMoney = Integer.parseInt(raceInfo.get("firstPrize").toString());
            String startMethod = (String) raceInfo.get("startMethod");

            org.json.JSONObject shoes;
            boolean front = false;
            boolean back = false;
            try{
                shoes = (org.json.JSONObject) horseShoe.get("shoes");
                front = (Boolean) shoes.get("front");
                back = (Boolean) shoes.get("back");
            } catch(Exception e){

            }

            int odds = 0;
            try{
                 odds = Integer.parseInt(race.get("odds").toString());
            }
            catch (Exception e){

            }
            lastPlace = place;
            place = 16;
            try {
                place = Integer.parseInt(race.get("place").toString());
            } catch (Exception e){

            }
            if(place == 0){
                place = 7;
            }
            lastPlaceMap.put(horseName, place);
            lastStartMap.put(horseName, date);


            if(!first && place != 16) {
                int lastStart = getTimeDiffernce(date, lastDate);
                RaceInfo tempRaceInfo = new RaceInfo(startMethod, startNumber, odds,trackName,priceMoney,distance,front,back,driverName,lastPlace,lastStart,season, trackCondition,place);
                raceList.add(tempRaceInfo);
            }
        }
        }

        try {

            writer = new FileWriter(file_name);

            writer.append(HEADER_LINE.toString());
            writer.append(NEW_LINE_SEPARATOR);

            for(RaceInfo raceInfo: raceList){
                writer.append(raceInfo.getStartMethod());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getStartNumber()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getOdds()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getTrack());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getFirstPrize()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getDistance()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getFront()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getBack()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getDriver());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getLastPlace()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getLastStart()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getSeason());
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getTrackCondition());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getPlace()));
                writer.append(NEW_LINE_SEPARATOR);
            }
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getTimeDiffernce(String newDate, String oldDate){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date date1 = format.parse(newDate);
            Date date2 = format.parse(oldDate);

            long diff = date1.getTime() - date2.getTime();

            long diffDays = diff / (24 * 60 * 60 * 1000);

            int returnValue = (int) diffDays;

            return returnValue;


        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }

    public void createCSVForCurrent(org.json.JSONObject jsonObject, String date) {

        org.json.JSONArray races = (org.json.JSONArray) jsonObject.get("races");
        for (int i = 0; i <= races.length(); i++) {
            org.json.JSONObject race = (org.json.JSONObject) races.get(i);
            writeToFile(race, date);
        }
    }

    public void writeToFile(org.json.JSONObject jsonObject, String date){


        JSONArray starts = (JSONArray) jsonObject.get("starts");

        JSONObject track = (JSONObject) jsonObject.get("track");
        String trackName = (String) track.get("name");

        String trackCondition = "";

        try{

            trackCondition = (String) track.get("condition");
        } catch (Exception e){
            System.out.println("Track not added yet");
            trackCondition = "light";
        }

        int distance =(Integer) jsonObject.get("distance");
        String startMethod = (String) jsonObject.get("startMethod");
        String prize = (String) jsonObject.get("prize");


        String[] test = prize.split(" ");
        String[] test2 = test[1].split("-");
        String finalPrize = test2[0].replace('.', '0');
        finalPrize = finalPrize + "0";
        int finalPrizeint = Integer.parseInt(finalPrize);

        String raceID = (String) jsonObject.get("id");

        List<RaceInfo> startList = new ArrayList();
        int lastPlace;

        for(int i = 0; i < starts.length(); i++){

            boolean frontShoes = true;
            boolean backShoes = true;

            JSONObject start = (JSONObject) starts.get(i);

            int startNumber = (Integer) start.get("number");
            JSONObject horse = (JSONObject) start.get("horse");
            JSONObject driverObj = (JSONObject) start.get("driver");
            JSONObject pools = (JSONObject) start.get("pools");
            JSONObject vinnare = (JSONObject) pools.get("vinnare");



            JSONObject shoes = (JSONObject) horse.get("shoes");
            boolean reported = (Boolean) shoes.get("reported");

            if(reported){
                JSONObject frontObj = (JSONObject) shoes.get("front");
                JSONObject backObj = (JSONObject) shoes.get("back");

                frontShoes = (Boolean) frontObj.get("hasShoe");
                backShoes = (Boolean) backObj.get("hasShoe");

            }


            int odds = (Integer) vinnare.get("odds");

            String nameOfHorse = (String) horse.get("name");
            String driver = (String) driverObj.get("shortName");
            String lastDate = lastStartMap.get(nameOfHorse);

            if(lastDate == null){
                lastDate = date;
            }
            int lastStart = getTimeDiffernce(date, lastDate);

            try {
                lastPlace = lastPlaceMap.get(nameOfHorse);
            } catch (Exception e){
                lastPlace = 0;
            }



            RaceInfo tempraceInfo = new RaceInfo(startMethod,startNumber,odds,trackName,finalPrizeint,distance,frontShoes,backShoes,driver, lastPlace,lastStart,"SPRING", trackCondition,0);
            startList.add(tempraceInfo);
        }


        String file_name = "C:/Users/Dotson/Documents/GitHub/ATGScrapper/current/" + raceID +".csv";


        try {
            FileWriter writer;
            writer = new FileWriter(file_name);

            writer.append(HEADER_LINE.toString());
            writer.append(NEW_LINE_SEPARATOR);


            for(RaceInfo raceInfo: startList){
                writer.append(raceInfo.getStartMethod());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getStartNumber()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getOdds()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getTrack());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getFirstPrize()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getDistance()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getFront()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getBack()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getDriver());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getLastPlace()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getLastStart()));
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getSeason());
                writer.append(COMMA_DELIMITER);
                writer.append(raceInfo.getTrackCondition());
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(raceInfo.getPlace()));
                writer.append(NEW_LINE_SEPARATOR);
            }
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
