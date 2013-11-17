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
import android.widget.ListView;
import com.firebase.client.*;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {
    ListView sessionList;
    SessionAdapter sessionAdapter;
    ArrayList<Session> sessions = new ArrayList<Session>();

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
        sessionAdapter = new SessionAdapter(MainActivity.this, sessions);
        sessionList = (ListView) findViewById(R.id.session_list);
        sessionList.setAdapter(sessionAdapter);
    }

    //Grab a list of sessions from the database
    public void grabAllSessions(){
        sessionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> array = snapshot.getChildren();
                if (array == null)
                    Log.i("FireBaseClient", "MainActivity - Pulling Sessions - Does not exist.");
                else {
                    //Log.i("FireBaseClient", "MainActivity - Pulling Sessions:\n" + array);
                    MainActivity.this.sessions.clear();
                    for (DataSnapshot child: snapshot.getChildren()){
                        Object value = child.getValue();
                        //Log.i("FireBaseClient", "MainActivity - Pulling Children:\n" + value);
                        Session newSession = new Session (
                                getFireBaseString(value, "assignment"),
                                getFireBaseString(value, "ninja"),
                                getFireBaseString(value, "place"),
                                getFireBaseString(value, "date"),
                                getFireBaseString(value, "time"),
                                getFireBaseString(value, "duration"));
                        newSession.setCheckOffList(getFireBaseArray(value, "check"),
                                getFireBaseArray(value, "help"),
                                getFireBaseArray(value, "checked"));
                        MainActivity.this.sessions.add(newSession);
                    }
                    //Log.i("Refreshing Sessions", "Pull Size: " + MainActivity.this.sessions.size());
                    refreshListView();
                }
            }

            @Override
            public void onCancelled(FirebaseError E) {
                System.err.println("Listener was cancelled");
            }
        });
    }
    //Helper to get FireBase String Fields
    public String getFireBaseString(Object value, String field){
        return (String)((Map)value).get(field);
    }

    //Helper to get FireBase A<String> Fields
    public String[] getFireBaseArray(Object value, String field){
        ArrayList<String> stringArray = (ArrayList<String>)((Map)value).get(field);
        //Log.i("FireBaseClient", "MainActivity - Pulling ArrayChild:\n" + stringArray);
        return stringArray.toArray(new String[stringArray.size()]);
    }

    //Repopulate and refresh the list view
    public void refreshListView(){
        //Log.i("Refreshing Sessions", sessions.toString());
        //Log.i("Refreshing Sessions", "Size: " + MainActivity.this.sessions.size());
        sessionAdapter.notifyDataSetChanged();
        sessionList.invalidate();
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
            grabAllSessions();
            refreshListView();
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
