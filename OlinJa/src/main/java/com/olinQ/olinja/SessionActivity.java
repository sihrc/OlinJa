/*
package com.olinQ.olinja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

*/
/**
 * Created by chris on 11/17/13.
 *//*

public class SessionActivity extends Activity {
    Session curSession;
    ListView checkoffList, helpmeList;
    ArrayAdapter<String> checkoffAdapter, helpmeAdapter;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_view);

        Intent in = getIntent();
        String id = in.getStringExtra("id");

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
    public void populateListViews(){
        checkoffList = (ListView) findViewById(R.id.session_list_checkoff);
        helpmeList = (ListView) findViewById(R.id.session_list_helpMe);

        checkoffAdapter = new ArrayAdapter<String>(SessionActivity.this, android.R.layout.simple_list_item_1, curSession.check.toArray(new String[curSession.check.size()]));
        helpmeAdapter = new ArrayAdapter<String>(SessionActivity.this, android.R.layout.simple_list_item_1, curSession.help.toArray(new String[curSession.help.size()]));
        checkoffList.setAdapter(checkoffAdapter);
        helpmeList.setAdapter(helpmeAdapter);
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
    }
}
*/
