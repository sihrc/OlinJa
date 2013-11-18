package com.olinQ.olinja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

/**
 * Created by chris on 11/17/13.
 */

public class SessionActivity extends Activity {
    Session curSession;
    ListView checkoffList, helpmeList;
    ArrayAdapter<String> checkoffAdapter, helpmeAdapter;

    //For managing the list view of sessions
    ListView sessionList;
    SessionAdapter sessionAdapter;

    //Username
    String username;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String FIREBASE_URL = "https://olinja-base.firebaseio.com/sessions";
    Firebase sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_view);

        Intent in = getIntent();
        String assignment_id = in.getStringExtra("id");

        //Setup Firebase Reference
        sessionRef = new Firebase(FIREBASE_URL).child(assignment_id);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Setup ListView
        sessionList = (ListView) findViewById(R.id.session_list);

        //Setup list adapter
        sessionAdapter = new SessionAdapter(sessionRef,this, R.layout.session_list_item);

        //Set the adapter
        sessionList.setAdapter(sessionAdapter);

        //Set OnItemClick
        sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent in = new Intent (MainActivity.this, SessionActivity.class);
                in.putExtra("Id", ((Session)sessionAdapter.getItem(position)).id);
                startActivity(in);*/
            }
        });

        //Connectivity Check
        connected = sessionRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected)
                    Toast.makeText(SessionActivity.this, "Connected to Assignment!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(SessionActivity.this, "Oh no! I can't find the internet!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
    @Override
    public void onStop(){
        super.onStop();
        //Application is closing - close the event listener - cleanup the adapter
        sessionRef.getRoot().child(".info/connected").removeEventListener(connected);
        sessionAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*public void populateListViews(){
        checkoffList = (ListView) findViewById(R.id.session_list_checkoff);
        helpmeList = (ListView) findViewById(R.id.session_list_helpMe);

        checkoffAdapter = new QListAdapter(SessionActivity.this, R.layout.q_list_item);
        helpmeAdapter = new QListAdapter(SessionActivity.this, R.layout.q_list_item);
        checkoffList.setAdapter(checkoffAdapter);
        helpmeList.setAdapter(helpmeAdapter);
    }*/

    /*
    public void onCreate(Bundle savedInstanceState){


        populateListViews();
        Firebase sessionRef = new Firebase("https://olinja-base.firebaseio.com/sessions/"+id);
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SessionActivity.this.curSession = dataSnapshot.getValue(Session.class).toSession();
                populateListViews();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    //Generate Notifications
    public void notification() {
        //Sight small icon
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setContentTitle("OlinJa Session")
                .setContentText("It's almost your turn!")
                .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);


        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }*/
}

