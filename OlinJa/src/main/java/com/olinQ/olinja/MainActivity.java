package com.olinQ.olinja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends Activity {
    //For managing the list view of sessions
    ListView sessionList;
    SessionAdapter sessionAdapter;

    //Holding the sessions by Id and session
    LinkedHashMap<String,Session> sessions = new LinkedHashMap<String, Session>();

    //Holding the arraylist of sessions for session adapter
    ArrayList<Session> sessionHolder = new ArrayList<Session>();
    String user;

    // Create a reference to a Firebase location, returned as array
    Firebase sessionRef = new Firebase("https://olinja-base.firebaseio.com/sessions");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set User Name
        user = "Chris"; //Hard Coded username //Needs implementation
        populateListView();
        grabAllSessions();
    }

    //Initially Populates the session list view
    private void populateListView(){
        sessionHolder.addAll(MainActivity.this.sessions.values());
        sessionAdapter = new SessionAdapter(this,sessionHolder);
        sessionList = (ListView) findViewById(R.id.session_list);
        sessionList.setAdapter(sessionAdapter);

        sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent (MainActivity.this, SessionActivity.class);
                in.putExtra("Id", MainActivity.this.sessionHolder.get(position).id);
            }
        });
    }

    //Grab a list of sessions from the database
    public void grabAllSessions(){
        sessionRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FirebaseSession newSession = dataSnapshot.getValue(FirebaseSession.class);
                newSession.setId(dataSnapshot.getName());
                Log.i("Firebase Pull", "ChildAdded is running");
                MainActivity.this.sessions.put(newSession.id, newSession.toSession());
                MainActivity.this.sessionAdapter.add(newSession.toSession());
                MainActivity.this.sessionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FirebaseSession newSession = dataSnapshot.getValue(FirebaseSession.class);
                MainActivity.this.sessions.put(newSession.id, newSession.toSession());
                //refreshListView();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FirebaseSession newSession = dataSnapshot.getValue(FirebaseSession.class);
                MainActivity.this.sessions.remove(newSession.id);
                //refreshListView();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //Repopulate and refresh the list view
    public void refreshListView(){
        //Log.i("Refreshing Sessions", sessions.toString());
        //Log.i("Refreshing Sessions", "Size: " + MainActivity.this.sessions.size());
        sessionAdapter.clear();
        sessionAdapter.addAll(sessions.values());
        sessionAdapter.notifyDataSetChanged();
    }

    //Dialog for adding a session
    public void showAddSessionDialog(){
        SessionDialog showDialog = new SessionDialog(MainActivity.this, user);
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
}
