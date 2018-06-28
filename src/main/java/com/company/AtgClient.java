package com.company;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Dotson on 2016-07-06.
 */
public class AtgClient {

    final String baseURL = "https://www.atg.se/services/racinginfo/v1/api/";

    String getID, getResults, returnID;
    String[] returnArray;
    Parser parser = new Parser();

    public String getRaceID(String date, String raceType){

        getID = baseURL + date;

        try {
            System.out.println(getID);
            HttpResponse<JsonNode> request = Unirest.get(getID).asJson();
            JsonNode body = request.getBody();



            JSONArray raceArr = body.getObject().getJSONObject("games").getJSONArray(raceType);
            JSONObject raceObj = raceArr.getJSONObject(0);

            returnID = raceObj.getString("id");



        } catch (UnirestException e) {

            e.printStackTrace();
        }
            catch (JSONException e){
                System.out.println("NO given race this day");
            }

        return returnID;
    }


    public String[] getRaces(String date, String raceType){
        String getRaceArray = baseURL + date;
        try {
            System.out.println(getRaceArray);
            HttpResponse<JsonNode> request = Unirest.get(getRaceArray).asJson();
            JsonNode body = request.getBody();
            JSONArray vArr = body.getObject().getJSONObject("games").getJSONArray(raceType).getJSONObject(0).getJSONArray("races");

            returnArray = new String[vArr.length()];


            for(int i = 0; i < vArr.length(); i++){
                returnArray[i] = vArr.getString(i);
            }
        } catch (UnirestException e) {

            e.printStackTrace();
        }
        catch (JSONException e){
            System.out.println("NO V75 this day");
        }
        return returnArray;
    }

    public void getStartsInfo(String race, int Avd){

        String URL = baseURL + "races/" + race;
        String StartURL;
        JSONArray starts = new JSONArray();
        try{
            HttpResponse<JsonNode> request = Unirest.get(URL).asJson();
            JsonNode body = request.getBody();
            starts = body.getObject().getJSONArray("starts");
        } catch (Exception e){
            e.printStackTrace();
        }

        int numberOfHorses = starts.length();
        URL = baseURL + "races/" + race + "/start/";

        System.out.println(URL);
        for (int i = 1; i <= numberOfHorses; i++){
            StartURL = URL + i;
            try {
                HttpResponse<JsonNode> request = Unirest.get(StartURL).asJson();
                JsonNode body = request.getBody();
                JSONObject jsonObject = body.getObject();
                parser.createCSVForHorse(jsonObject, Avd, i);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    public void saveResults(String v75id){

            getResults = baseURL + "games/" + v75id;

        try {
            HttpResponse<JsonNode> request = Unirest.get(getResults).asJson();

            String data = request.getBody().toString();

            PrintWriter out = new PrintWriter(v75id + ".json");

            out.write(data);
            out.flush();
            out.close();



        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void getCurrentRaceInfo(String id, String date) {

        getResults = baseURL + "games/" + id;

        System.out.println(getResults);
        try {
            HttpResponse<JsonNode> request = Unirest.get(getResults).asJson();
            JsonNode body = request.getBody();
            JSONObject jsonObject = body.getObject();
            parser.createCSVForCurrent(jsonObject, date);
        } catch (UnirestException e) {
            e.printStackTrace();
        }


    }
}
