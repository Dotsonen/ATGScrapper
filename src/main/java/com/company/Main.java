package com.company;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    static Calendar date;
    static SimpleDateFormat formatter;
    static String tempDate, id;
    static final int days = 1095;

    final static String day = "calendar/day/";



    public static void main(String[] args) {

        AtgClient ac = new AtgClient();
        downloadData(ac);



    }


    public static void downloadData(AtgClient ac) {
        date = new GregorianCalendar(2016, Calendar.JULY, 29);
        formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < days; i++) {

            tempDate = formatter.format(date.getTime());

             id = ac.getV75ID(day + tempDate);
             ac.saveResults(id);

            date.add(Calendar.DAY_OF_MONTH, -1);
        }
    }




}
