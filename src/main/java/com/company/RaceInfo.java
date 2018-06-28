package com.company;

/**
 * Created by Dotson on 2018-02-21.
 */
public class RaceInfo {

    String startMethod, season, trackCondition;
    int startNumber ;
    int odds;
    String track;
    int firstPrize;
    int distance;
    boolean front;
    boolean back;
    String driver;
    int lastPlace;
    int lastStart;
    int place;

    public RaceInfo(String startMethod, int startNumber, int odds, String track, int firstPrize, int distance,
                    boolean front, boolean back, String driver, int lastPlace, int lastStart, String season,String trackCondition, int place ){


        this.startMethod = startMethod;
        this.startNumber = startNumber;
        this.odds = odds;
        this.track = track;
        this.firstPrize = firstPrize;
        this.distance = distance;
        this.front = front;
        this.back = back;
        this.driver = driver;
        this.lastPlace = lastPlace;
        this.lastStart = lastStart;
        this.season = season;
        this.trackCondition = trackCondition;
        this.place = place;

    }

    public String getStartMethod(){
        return startMethod;
    }
    public int getStartNumber(){
        return startNumber;
    }
    public int getOdds(){
        return odds;
    }
    public String getTrack(){
        return track;
    }
    public int getFirstPrize(){
        return firstPrize;
    }
    public int getDistance(){
        return distance;
    }
    public boolean getFront(){
        return front;
    }
    public boolean getBack(){
        return back;
    }
    public String getDriver(){
        return driver;
    }
    public int getLastPlace(){
        return lastPlace;
    }
    public int getLastStart(){
        return lastStart;
    }
    public String getSeason() {return season;}
    public String getTrackCondition() {return trackCondition;}
    public int getPlace(){
        return place;
    }

}
