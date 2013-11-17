package com.olinQ.olinja;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chris on 11/13/13.
 */
public class Session {
    String assignment,ninja,place,time,duration, date, id;
    ArrayList<String> check;
    ArrayList<String> checked;
    ArrayList<String> help;

    //public Constructor
    public Session(String assignment, String ninja, String place, String date, String time, String duration){
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    //Public Methods
    public void setAssignment(String value){
        this.assignment = value;
    }
    public void setNinja(String value){
        this.ninja = value;
    }
    public void setPlace(String value){
        this.place = value;
    }
    public void setTime(String value){
        this.time = value;
    }
    public void setDuration(String value){
        this.duration = value;
    }
    public void setDate(String value){
        this.date = value;
    }
    public void setId(String value){
        this.id = value;
    }
    public void setCheck(ArrayList<String> values){
        this.check = values;
    }
    public void setChecked(ArrayList<String> values){
        this.checked = values;
    }
    public void setHelp(ArrayList<String> values){
        this.help = values;
    }


    public FirebaseSession toFireBaseSession(){
        return new FirebaseSession(this.assignment, this.ninja, this.place, this.time, this.duration, this.date, this.check.toString(), this.help.toString(), this.checked.toString());
    }
}
