package com.olinQ.olinja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.firebase.client.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView sessionList;
    SessionAdapter sessionAdapter;
    ArrayList<Session> sessions = new ArrayList<Session>();
    // Create a reference to a Firebase location, returned as array
    Firebase sessionRef = new Firebase("https://olinja-base.firebaseio.com/sessions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grabAllSessions();
        populateListView();
    }

    //Initially Populates the session list view
    private void populateListView(){
        sessionAdapter = new SessionAdapter(MainActivity.this, sessions);
        sessionList = (ListView) findViewById(R.id.session_list);
        sessionList.setAdapter(sessionAdapter);
    }

    //Grab a list of sessions from the database
    public void grabAllSessions(){
        //sessions = (Database Call)

        //TEST example
        String url = "https://SampleChat.firebaseIO-demo.com/users/fred/name/first";
        Firebase dataRef = new Firebase(url);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Fred's first name is " + snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError E) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    //Repopulate and refresh the list view
    public void refreshListView(){
        sessionAdapter.notifyDataSetChanged();
    }

    //Dialog for adding a session
    public void showAddSessionDialog(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
