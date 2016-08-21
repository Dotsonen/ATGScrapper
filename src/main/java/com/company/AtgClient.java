package com.company;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Dotson on 2016-07-06.
 */
public class AtgClient {

    final String baseURL = "https://www.atg.se/services/v1/";

    String getID, getResults, returnID, returnResults;


    public String getV75ID(String date){

        getID = baseURL + date;

        try {
            HttpResponse<JsonNode> request = Unirest.get(getID).asJson();
            System.out.println(request);
            int code = request.getStatus();
            JsonNode body = request.getBody();




            JSONArray v75Arr = body.getObject().getJSONObject("games").getJSONArray("V75");
            JSONObject v75Obj = v75Arr.getJSONObject(0);

            returnID = v75Obj.getString("id");


        } catch (UnirestException e) {
            e.printStackTrace();
        }
            catch (JSONException e){
             //   System.out.println("NO V75 this day");
            }

        return returnID;
    }

    public void saveResults(String v75id){

            getResults = baseURL + "games/" + v75id;

        try {
            HttpResponse<JsonNode> request = Unirest.get(getResults).asJson();

            String data = request.getBody().toString();

            PrintWriter out = new PrintWriter(v75id + ".json");

         //   System.out.println(getResults);

            out.write(data);
            out.flush();
            out.close();



        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
