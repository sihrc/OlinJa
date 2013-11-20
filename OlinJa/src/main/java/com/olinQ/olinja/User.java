package com.olinQ.olinja;

/**
 * Created by chris on 11/18/13.
 */
public class User {
    String fullName, username;
    Boolean ninja;

    public User(String fullName, String username, Boolean ninja){
        this.fullName = fullName;
        this.username = username;
        this.ninja = ninja;
    }

    public String getFullName(){
        return this.fullName;
    }
    public String getUserName(){
        return this.username;
    }
    public Boolean getNinja(){
        return this.ninja;
    }
}
