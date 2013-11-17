package com.olinQ.olinja;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaustin on 11/14/13.
 * Handles all Firebase ajax and communication
 */
public class FireConnect {

    ArrayList<Session> allSessions;

    public void makeSessions(){

        allSessions = new ArrayList<Session>();

        //Gets the session array from the server
        String url = "https://olinja-base.firebaseio.com";
        Firebase dataRef = new Firebase(url);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if (value == null) {
                    System.out.println("User sessions doesn't exist");
                } else {

                    ArrayList<Object> fireSessions = (ArrayList<Object>)((Map)value).get("sessions");
                    //Get each session from the server

                    for (int i=0; i < fireSessions.size(); i++){
                        //Make Session object
                        Session s = extractSession(i, fireSessions.get(i));
                        //Add it to all Sessions
                        allSessions.add(s);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError E) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    private Session extractSession(int id, Object sesh){
        String name = (String) ((Map)sesh).get("name"); //Name of the assignment
        String ninja = (String) ((Map)sesh).get("ninja");   //Ninja for assignment hours
        String time = (String) ((Map)sesh).get("time"); //Time for assignment
        String location = (String) ((Map)sesh).get("location"); //Time for assignment
        int duration = (int) ((Map)sesh).get("duration"); //THow long the session goes for
        ArrayList<String>[] checkOffList = (ArrayList<String>[]) ((Map)sesh).get("checkOffList"); //Getting the list of check off

        Session s = new Session(id, name, ninja, time, location, duration, checkOffList);

        return s;
    }

    public ArrayList<Session> getAllSessions(){
        return allSessions;
    }

    public Session getOneSession(int id){
        return allSessions.get(id);
    }

    public void postNewSession(){
        //MAKING A NEW SESSION AND PuSHING IT TO THE SERVER
    }

    public void postCheckOffUpdate(int i){
        //With the given assignment id, pushes an updated version of the CheckOff to the server
    }

}
