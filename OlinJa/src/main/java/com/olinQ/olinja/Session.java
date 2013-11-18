package com.olinQ.olinja;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by chris on 11/17/13.
 */
public class Session {
    String assignment,ninja,place,time,duration, date, check, help, checked, id;

    public Session(){}
    public Session(String assignment, String ninja, String place, String time, String duration, String date, String check, String help, String checked){
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.time = time;
        this.duration = duration;
        this.date = date;
        this.check = check;
        this.help = help;
        this.checked = checked;
    }

    public LinkedHashSet<String> getArrayList(String value){
        if (value.length() > 2){
            Log.i("FireBaseSession", "String " + value);
            return new LinkedHashSet<String>(Arrays.asList(value.substring(1, -1).split(",")));
        }
        else {
            return new LinkedHashSet<String>();
        }
    }
    //Public Get Methods as required by Firebase
    public String getAssignment(){
        return this.assignment;
    }
    public String getNinja(){
        return this.ninja;
    }
    public String getPlace(){
        return this.place;
    }
    public String getTime(){
        return this.time;
    }
    public String getDuration(){
        return this.duration;
    }
    public String getDate(){
        return this.date;
    }
    public String getCheck(){
        return this.check;
    }
    public String getHelp(){
        return this.help;
    }
    public String getChecked(){
        return this.checked;
    }
    public String getId(){
        return this.id;
    }
    //Public Set Methods as required by Firebase
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
    public void setCheck(String value){
        this.check = value;
    }
    public void setHelp(String value){
        this.help = value;
    }
    public void setChecked(String value){
        this.checked = value;
    }
    public void setId(String value){
        this.id = value;
    }
}
