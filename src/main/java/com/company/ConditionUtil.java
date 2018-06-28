package com.company;

/**
 * Created by Dotson on 2018-03-14.
 */
public class ConditionUtil {

    private ConditionUtil(){

    }


    static public String getSeason(int month){


        if(month <= 2 || month > 11){
            return "WINTER";
        } else if(month >= 3 && month <= 5){
            return "SPRING";
        } else if(month > 5 && month <= 8){
            return "SUMMER";
        } else{
            return "AUTUMN";
        }



    }
}
