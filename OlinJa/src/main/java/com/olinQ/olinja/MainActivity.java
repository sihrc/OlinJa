package com.olinQ.olinja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.firebase.client.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    //For managing the list view of sessions
    ListView sessionList;
    SessionAdapter sessionAdapter;

    //Username
    String username;

    //Firebase URL Location
    String FIREBASE_URL = "https://olinja-base.firebaseio.com";
    Firebase sessionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Username
        setupUsername();

        //Setup Firebase Reference
        sessionRef = new Firebase(FIREBASE_URL).child("sessions");
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
                Intent in = new Intent (MainActivity.this, SessionActivity.class);
                in.putExtra("Id", ((FirebaseSession)sessionAdapter.getItem(position)).id);
                startActivity(in);
            }
        });

    }

    //Dialog for adding a session
    public void showAddSessionDialog(){
        SessionDialog showDialog = new SessionDialog(MainActivity.this, username);
        showDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_addSession) {
            showAddSessionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Generate Notifications
    public void notification() {
        //Sight small icon
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setContentTitle("Rain Check")
                .setContentText("Go check the weather!")
                .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);


        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    //Setup Username
    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        username = prefs.getString("username", null);
        if (username == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            username = "Oliner" + r.nextInt(100000);
            prefs.edit().putString("username", username).commit();
        }
    }
}
