package com.olinQ.olinja;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chris on 11/13/13.
 */
public class Session {
    final public int CHECKED_OFF = 0;
    final public int NEEDS_CHECKOFF = 1;
    final public int NEEDS_HELP = 2;

    int id, duration;
    String assignment,ninja,place,time;
    ArrayList<String>[] checkOffList;

    //public constructor
    public Session(int id, String assignment, String ninja, String place, String time, int duration, ArrayList<String>[] checkOffList){
        this.id = id;
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.time = time;
        this.duration = duration;
        this.checkOffList = checkOffList;
    }

    //Public Methods
    //Check someone off
    /*public void checkOff(String user){
        this.checkOffList.put(user, this.CHECKED_OFF);
    }

    //Add someone to the queue
    public void addToQueue(String user){
        this.checkOffList.put(user, this.NEEDS_CHECKOFF);
    }

    //Add someone to the need help queue
    public void setNeedHelp(String user){
        this.checkOffList.put(user, this.NEEDS_HELP);
    }*/
}
