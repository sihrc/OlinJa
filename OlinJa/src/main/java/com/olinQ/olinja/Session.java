package com.olinQ.olinja;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;

/**
 * Created by chris on 11/17/13.
 */
public class Session {
    String assignment,ninja,place,time,duration, date, check, help, checked, id;
    long priority;

    public Session(){} //Required by firebase
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
        this.priority = getPriority();
    }
    //Gets priority for firebase.
    public long getPriority(){
        String[] date = this.date.split("/");
        String[] splitTime = this.time.split(":");
        Calendar start = new GregorianCalendar(Integer.parseInt(2000 + date[2]),
                                               Integer.parseInt(date[0]),
                                               Integer.parseInt(date[1]));
        start.add(Calendar.HOUR_OF_DAY, Integer.valueOf(splitTime[0]));
        start.add(Calendar.MINUTE, Integer.valueOf(splitTime[1].substring(0, 2)));
        return start.getTimeInMillis();
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
