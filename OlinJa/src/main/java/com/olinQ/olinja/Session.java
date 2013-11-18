package com.olinQ.olinja;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chris on 11/13/13.
 */
public class Session {
    String assignment,ninja,place,time,duration, date, id;
    LinkedHashSet<String> check, checked, help;

    //public Constructor
    public Session(String assignment, String ninja, String place, String date, String time, String duration){
        this.assignment = assignment;
        this.ninja = ninja;
        this.place = place;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.check = new LinkedHashSet<String>();
        this.checked = new LinkedHashSet<String>();
        this.help = new LinkedHashSet<String>();
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
    public void setCheck(LinkedHashSet<String> values){
        this.check = values;
    }
    public void setChecked(LinkedHashSet<String> values){
        this.checked = values;
    }
    public void setHelp(LinkedHashSet<String> values){
        this.help = values;
    }


    //Functions for changing states of students
    public void addToCheck(String value){
        this.check.add(value);
    }
    public void addToChecked(String value){
        this.checked.add(value);
    }
    public void addToHelp(String value){
        this.help.add(value);
    }
    public void removeFromCheck(String value){
        this.check.remove(value);
    }
    public void removeFromChecked(String value){
        this.checked.remove(value);
    }
    public void removeFromHelp(String value){
        this.help.remove(value);
    }

    public void checkToChecked(String value){
        addToChecked(value);
        removeFromCheck(value);
    }
    public void checkToHelp(String value){
        addToHelp(value);
        removeFromCheck(value);
    }
    public void checkedToCheck(String value){
        addToCheck(value);
        removeFromChecked(value);
    }
    public void checkedToHelp(String value){
        addToHelp(value);
        removeFromChecked(value);
    }
    public void helpToCheck(String value){
        addToCheck(value);
        removeFromHelp(value);
    }
    public void helpToChecked(String value){
        addToCheckec(value);
        removeFromHelp(value);
    }


    public FirebaseSession toFireBaseSession(){
        return new FirebaseSession(this.assignment, this.ninja, this.place, this.time, this.duration, this.date, this.check.toString(), this.help.toString(), this.checked.toString());
    }
}
