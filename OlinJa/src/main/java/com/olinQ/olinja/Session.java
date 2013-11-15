package com.olinQ.olinja;


import java.util.HashMap;

/**
 * Created by chris on 11/13/13.
 */
public class Session {
    final public int CHECKED_OFF = 0;
    final public int NEEDS_CHECKOFF = 1;
    final public int NEEDS_HELP = 2;

    String assignment,ninja,place,time,duration, date;
    HashMap<String, Integer> checkOffList;

    //public constructor
    public Session(String assignment, String ninja, String place, String date, String time, String duration){
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.checkOffList = new HashMap<String, Integer>();
    }

    //Public Methods
    //Import a HashMap
    public void setCheckOffList(HashMap<String, Integer> checkOffList){
        this.checkOffList = checkOffList;
    }

    //Check someone off
    public void checkOff(String user){
        this.checkOffList.put(user, this.CHECKED_OFF);
    }

    //Add someone to the queue
    public void addToQueue(String user){
        this.checkOffList.put(user, this.NEEDS_CHECKOFF);
    }

    //Add someone to the need help queue
    public void setNeedHelp(String user){
        this.checkOffList.put(user, this.NEEDS_HELP);
    }
}
