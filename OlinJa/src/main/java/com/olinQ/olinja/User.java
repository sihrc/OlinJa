package com.olinQ.olinja;

/**
 * Created by chris on 11/18/13.
 */
public class User {
    String fullname, username, ninja;

    public User(){} //This is required by Firebase - don't remove
    public User(String fullname, String username, String ninja){
        this.fullname = fullname;
        this.username = username;
        this.ninja = ninja;
    }

    public String getFullname(){
        return this.fullname;
    }
    public String getUsername(){
        return this.username;
    }
    public String getNinja(){
        return this.ninja;
    }
}
