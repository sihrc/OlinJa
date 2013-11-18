package com.olinQ.olinja;


import java.util.Calendar;
import java.util.GregorianCalendar;

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

    //Set the Id
    public void setId(String value) {this.id = value;}
}
