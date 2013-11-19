package com.olinQ.olinja;

/**
 * Created by chris on 11/18/13.
 */
public class QueueItem {
    String user, id;
    public QueueItem(){};
    public QueueItem(String user, String id){
        this.user = user;
        this.id = id;
    }

    public String getId(){
        return this.id;
    }
    public String getUser(){
        return this.user;
    }
}
