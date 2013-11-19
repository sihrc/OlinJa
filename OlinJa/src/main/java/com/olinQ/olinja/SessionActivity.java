package com.olinQ.olinja;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

    //Add to queue Buttons
    TextView checkAdd, helpAdd;

    //If Ninja
    Boolean ninja;

    //Username
    String username;

    //Queue Id
    String queueId;
    Boolean inQueue;

    //Session Id
    String sessionId;
    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String CHECK_URL = "https://olinja-base.firebaseio.com/check/";
    String HELP_URL = "https://olinja-base.firebaseio.com/help/";
    String CHECKED_URL = "https://olinja-base.firebaseio.com/help/";


    Firebase checkRef, helpRef, checkedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_view);
        //Grab username
        setupUsername();
        updateUser();
        //Grab id
        Intent in = getIntent();
        sessionId = in.getStringExtra("id");

        //Setup Firebase Reference
        checkedRef = new Firebase(CHECKED_URL + "/" + sessionId);
        checkRef = new Firebase(CHECK_URL + "/" + sessionId);
        helpRef = new Firebase(HELP_URL + "/" + sessionId);
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

        //Set List Adapter
        checkoffList.setAdapter(checkoffAdapter);
        helpmeList.setAdapter(helpmeAdapter);

        //Add list item onClick
        checkoffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkOffNinjee(position, "check");
            }
        });
        helpmeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkOffNinjee(position, "help");
            }
        });

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

        //AddToQueue Buttons
        checkAdd = (TextView) findViewById(R.id.checkoff_add_queue);
        helpAdd = (TextView) findViewById(R.id.helpMe_add_queue);

        //Add click listeners
        checkAdd.setOnClickListener(addToQueue("check"));
        helpAdd.setOnClickListener(addToQueue("help"));
    }

    public View.OnClickListener addToQueue(final String mode){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase pushref;
                updateUser();
                if (!inQueue && !ninja){
                    if (mode.equals("check"))
                        pushref = checkRef.push();
                    else
                        pushref = helpRef.push();
                    String name = pushref.getName();
                    pushref.setValue(new User(username,name));

                    //Recording queue Id
                    getSharedPreferences("OlinJa", 0).edit().putString("queue", name).commit();
                    queueId = name;
                    inQueue = true;

                    //Start Notification Service for when name in Queue is first.
                    Intent in = new Intent(SessionActivity.this, NotificationService.class);
                    in.putExtra("id",name);
                    in.putExtra("mode", mode);
                    in.putExtra("session", sessionId);
                    startService(in);
                }
            }
        };
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
        getMenuInflater().inflate(R.menu.session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_checked:
                Intent in = new Intent(this, CheckedOffActivity.class);
                startActivityForResult(in, 0);
        }
        return super.onOptionsItemSelected(item);
    }
    //Setup Username
    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("OlinJa", 0);
        username = prefs.getString("username", null);
        ninja = prefs.getString("ninja","false").equals("true");
        queueId = prefs.getString("queue", null);
        if (queueId == null){
            inQueue = false;
        }
    }

    //Check off Ninjee
    public void checkOffNinjee(int position, String mode){
        Firebase pushref;
        User curItem;
        if (mode.equals("check")){ //Moves from the check off to checked off
            curItem = ((User)checkoffAdapter.getItem(position));
            checkRef.child(sessionId).child(curItem.getId()).removeValue();
            pushref = checkedRef.push();
            pushref.setValue(curItem);

        } else { //Moves from helpme to checked off
            curItem = ((User)helpmeAdapter.getItem(position));
            helpRef.child(sessionId).child(curItem.getId()).removeValue();
            pushref = checkedRef.push();
            pushref.setValue(curItem);
        }
    }
    //Update user info
    public void updateUser(){
        SharedPreferences prefs = getApplication().getSharedPreferences("OlinJa",0);
        username = prefs.getString("username", null);
        if (prefs.getString("ninja","false").equals("false")){
            ninja = false;
        } else {ninja = true;}
    }

    //Check if in Queue
}

