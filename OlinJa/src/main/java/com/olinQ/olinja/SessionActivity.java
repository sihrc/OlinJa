package com.olinQ.olinja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by chris on 11/17/13.
 */

public class SessionActivity extends Activity {
    //List Views and Adapters
    ListView checkoffList, helpmeList;
    QListAdapter checkoffAdapter, helpmeAdapter;

    //Username
    String username;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String FIREBASE_URL = "https://olinja-base.firebaseio.com/sessions";
    Firebase checkRef, helpRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_view);

        //Grab id
        Intent in = getIntent();
        String assignment_id = in.getStringExtra("id");

        //Setup Firebase Reference
        checkRef = new Firebase(FIREBASE_URL).child(assignment_id).child("check");
        helpRef = new Firebase(FIREBASE_URL).child(assignment_id).child("help");
    }

    @Override
    public void onStart(){
        super.onStart();

        //Setup ListView
        checkoffList = (ListView) findViewById(R.id.session_list_checkoff);
        helpmeList = (ListView) findViewById(R.id.session_list_helpMe);

        //Setup list adapter
        checkoffAdapter = new QListAdapter(checkRef,this, R.layout.q_list_item);
        helpmeAdapter = new QListAdapter(helpRef, this, R.layout.q_list_item);


        //Connectivity Check
        connected = checkRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
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
        checkRef.getRoot().child(".info/connected").removeEventListener(connected);
        checkoffAdapter.cleanup();
        helpmeAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

