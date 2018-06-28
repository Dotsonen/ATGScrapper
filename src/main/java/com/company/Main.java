package com.company;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    static Calendar date;
    static SimpleDateFormat formatter;
    static String tempDate, id;
    static final int days = 800;

    private static String  d;
    private static String race;

    final static String day = "calendar/day/";

    public static void main(String[] args) {

        AtgClient ac = new AtgClient();
        //downloadData(ac);

        date = new GregorianCalendar(2018,Calendar.MARCH,24);
        String raceOption = "V75";
        downloadHistory(ac, date, raceOption);
        downloadCurrentRaceInfo(ac, date, raceOption);

    }

    public static void downloadHistory(AtgClient ac, Calendar date, String raceType){
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        tempDate = formatter.format(date.getTime());
        String[] raceList = ac.getRaces(day + tempDate,raceType);

        for(int i = 0; i < raceList.length; i++){
            ac.getStartsInfo(raceList[i], i+1);
        }
    }

    public static void downloadCurrentRaceInfo(AtgClient ac, Calendar date, String raceType){
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        tempDate = formatter.format(date.getTime());
        id = ac.getRaceID(day + tempDate, raceType);
        ac.getCurrentRaceInfo(id, tempDate);

    }



    public static void downloadData(AtgClient ac) {
        date = new GregorianCalendar(2018, Calendar.FEBRUARY, 10);
        formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < days; i++) {

            tempDate = formatter.format(date.getTime());

             id = ac.getRaceID(day + tempDate, "V75");
             ac.saveResults(id);

            date.add(Calendar.DAY_OF_MONTH, -1);
        }
    }




}
