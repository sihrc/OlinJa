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
    public void setCheckOffList(String[] check, String[] help, String[] checked){
        for (String user: check){this.addToQueue(user);}
        for (String user: help){this.setNeedHelp(user);}
        for (String user: checked){this.checkOff(user);}
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
