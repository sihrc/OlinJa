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
 */
public class FireConnect {
    //Handles all Firebase ajax and communication

    public void getSessions(){
        //Gets the session array
        String url = "https://olinja-base.firebaseio.com";
        Firebase dataRef = new Firebase(url);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if (value == null) {
                    System.out.println("User sessions doesn't exist");
                } else {
                    ArrayList<Object> allSessions = (ArrayList<Object>)((Map)value).get("sessions");
                    //Get each session from the server
                    for (int i=0; i < allSessions.size(); i++){

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError E) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    public void getSession(int id){
        String url = "https://olinja-base.firebaseio.com/sessions/" + id;
        Firebase dataRef = new Firebase(url);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if (value == null) {
                    System.out.println("User julie doesn't exist");
                } else {
                    String firstName = (String)((Map)value).get("first");
                    String lastName = (String)((Map)value).get("last");
                    System.out.println("User julie's full name is: " + firstName + " " + lastName);
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
}
