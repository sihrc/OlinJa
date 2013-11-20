package com.olinQ.olinja;

/**
 * Created by chris on 11/18/13.
 */
public class User {
    String fullname, username, ninja, picture, canhelp, needhelp;

    public User(){} //This is required by Firebase - don't remove
    public User(String fullname, String username, String ninja, String picture, String canhelp, String needhelp){
        this.fullname = fullname;
        this.username = username;
        this.ninja = ninja;
        this.picture = picture;
        this.canhelp = canhelp;
        this.needhelp = needhelp;
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
    public String getPicture(){
        return this.picture;
    }
    public String getCanhelp(){
        return this.canhelp;
    }
    public String getNeedhelp(){
        return this.needhelp;
    }
}
