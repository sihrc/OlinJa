package com.olinQ.olinja;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.Node;

/**
 * Created by chris on 11/17/13.
 */

public class SessionActivity extends Activity {
    //List Views and Adapters
    ListView checkoffList, helpmeList;
    QListAdapter checkoffAdapter, helpmeAdapter;

    //Add to queue Buttons
    Button checkAdd, helpAdd;
    String before; //check before queue

    //If Ninja
    Boolean ninja;

    //If already in Queue
    Boolean inQueue = false;

    //Username
    String username, fullname;
    User curUser;

    //Session Id
    String sessionId;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String CHECK_URL = "https://olinja-base.firebaseio.com/check/";
    String HELP_URL = "https://olinja-base.firebaseio.com/help/";
    String CHECKED_URL = "https://olinja-base.firebaseio.com/help/";
    String USER_URL = "https://olinja-base.firebaseio.com/users/";

    //Firebase References
    Firebase checkRef, helpRef, checkedRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        //Grab sessionId from MainActivity
        Intent in = getIntent();
        sessionId = in.getStringExtra("id");
        username = in.getStringExtra("username");

        //Setup Firebase Reference
        checkedRef = new Firebase(CHECKED_URL + "/" + sessionId);
        checkRef = new Firebase(CHECK_URL + "/" + sessionId);
        helpRef = new Firebase(HELP_URL + "/" + sessionId);
        userRef = new Firebase(USER_URL + "/" + username);

        //Get User Information
        getFirebaseUserInfo();

        //SetUp inQueue Checker
        checkInQueue();
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
        checkAdd = (Button) findViewById(R.id.checkoff_add_queue);
        helpAdd = (Button) findViewById(R.id.helpMe_add_queue);

        //Add click listeners
        checkAdd.setOnClickListener(selectUser("check"));
        helpAdd.setOnClickListener(selectUser("help"));
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

    //Add to Queue - Button listeners
    public View.OnClickListener selectUser(final String mode){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ninja) showNinjaSelect();
                else showUserSelect();
/*                Firebase pushref;
                if (!inQueue && !ninja){
                    if (mode.equals("check")){
                        pushref = checkRef.child(username);
                        curUser.canhelp = "false";
                        curUser.needhelp = "false";
                        Toast.makeText(SessionActivity.this, "You're in Queue to get checked off! I'll let you know when it's almost your turn.", Toast.LENGTH_LONG).show();}
                    else{
                        pushref = helpRef.child(username);
                        Toast.makeText(SessionActivity.this, "You're in Queue to get help! I'll let you know when it's almost your turn.", Toast.LENGTH_LONG).show();
                    }
                    pushref.setValue(curUser);
                    inQueue = true;*/
                    //Start Notification Service for when name in Queue is first.
/*                    Intent in = new Intent(SessionActivity.this, NotificationService.class);
                    in.putExtra("id",name);
                    in.putExtra("mode", mode);
                    in.putExtra("session", sessionId);
                    startService(in);*/
                }
            }
        };
    }

    //Ninja Select user dialog
    public void showNinjaSelect(){

    }

    //User Select user Dialog
    public void showUserSelect(){

    }

    //Check off Ninjee
    public void checkOffNinjee(int position, String mode){
        Firebase pushref;
        User curItem;
        if (mode.equals("check")){ //Moves from the check off to checked off
            curItem = ((User)checkoffAdapter.getItem(position));

            checkRef.child(sessionId).child(curItem.getFullname()).removeValue();
            pushref = checkedRef.push();
            pushref.setValue(curItem);

        } else { //Moves from helpme to checked off
            curItem = ((User)helpmeAdapter.getItem(position));

            helpRef.child(sessionId).child(curItem.getUsername()).removeValue();

            pushref = checkedRef.push();
            pushref.setValue(curItem);
        }
    }

    //Firebase user info pull
    public void getFirebaseUserInfo(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curUser = dataSnapshot.getValue(User.class);
                if (username.equals(curUser.username)) {
                    fullname = curUser.fullname;
                    ninja = curUser.ninja.equals("true");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    //Check if in Queue
    public void checkInQueue(){
        Firebase checkQueue = new Firebase(CHECK_URL).child(username);
        Firebase helpQueue = new Firebase(HELP_URL).child(username);

        checkRef.addChildEventListener(checkQueue());
        helpQueue.addChildEventListener(checkQueue());

        checkQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    inQueue = false;
                }else {
                    inQueue =  (dataSnapshot.getValue(User.class).username.equals(username));
                }
                if (curUser.notify.equals("true")){
                    notifyUser();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        helpQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    inQueue = false;
                }else {
                    inQueue =  (dataSnapshot.getValue(User.class).username.equals(username));
                }
                if (curUser.notify.equals("true")){
                    notifyUser();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
    public ChildEventListener checkQueue(){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (s == null)
                    before = dataSnapshot.getName();
                else if (s.equals(before) && dataSnapshot.getName().equals(username)){
                        notifyUser();}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (s == null)
                    before = dataSnapshot.getName();
                else{
                    if (s.equals(before) && dataSnapshot.getName().equals(username))
                        notifyUser();}
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getName().equals(username)){
                    inQueue = false;
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (s == null)
                    before = dataSnapshot.getName();
                else{
                    if (s.equals(before) && dataSnapshot.getName().equals(username))
                        notifyUser();}
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }
    public void notifyUser(){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.drawable.olinja).setContentTitle("You're up!").setContentText("You're next in line. Come quick!");

        Intent resultIntent = new Intent(this, SessionActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SessionActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,notification.build());
    }
}

