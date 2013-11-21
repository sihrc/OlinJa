package com.olinQ.olinja;


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chris on 11/17/13.
 */
public class Session {
    String assignment,ninja,place,time,duration, date, id, picture;
    long priority;
    
    public Session(){} //Required by firebase - don't remove!
    public Session(String assignment, String ninja, String place, String time, String duration, String date, String picture){
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.time = time;
        this.duration = duration;
        this.date = date;
        this.priority = getPriority();
        this.picture = picture;
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
    public String getPicture(){return this.picture;}
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

/*    public ArrayList<String> deSerialize(String value){
        if (value.length() > 2)
            return new ArrayList<String>(Arrays.asList(value.substring(1, -1).split(",")));
        else
            return new ArrayList<String> ();
    }*/

}
