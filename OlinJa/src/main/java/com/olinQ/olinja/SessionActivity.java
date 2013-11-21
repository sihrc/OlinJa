package com.olinQ.olinja;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
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
    Button checkAdd, helpAdd;
    String before; //check before queue

    //If Ninja
    Boolean ninja;

    //If already in Queue
    Boolean inQueue = false;

    //Username
    String username, fullname;
    User curUser;

    //OnClick Selected
    User selected;

    //Session Id
    String sessionId;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String CHECK_URL = "https://olinja-base.firebaseio.com/check/";
    String HELP_URL = "https://olinja-base.firebaseio.com/help/";
    String CHECKED_URL = "https://olinja-base.firebaseio.com/checked/";
    String USER_URL = "https://olinja-base.firebaseio.com/users/";

    //Firebase References
    Firebase checkRef, helpRef, checkedRef, userRef, checkRefAdapter, helpRefAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        //Grab sessionId from MainActivity
        Intent in = getIntent();
        sessionId = in.getStringExtra("id");
        username = in.getStringExtra("username");

        if (sessionId == null){
            sessionId = getSharedPreferences("OlinJa", MODE_PRIVATE).getString("sessionId", "failed");
        }

        if (username == null){
            username = getSharedPreferences("OlinJa", MODE_PRIVATE).getString("userId", "failed");
        }
        //Setup Firebase Reference
        checkedRef = new Firebase(CHECKED_URL).child(sessionId);
        checkRef = new Firebase(CHECK_URL).child(sessionId);
        helpRef = new Firebase(HELP_URL).child(sessionId);
        checkRefAdapter = new Firebase(CHECK_URL).child(sessionId);
        helpRefAdapter = new Firebase(HELP_URL).child(sessionId);
        userRef = new Firebase(USER_URL).child(username);

        //Get User Information
        getFirebaseUserInfo();
    }

    @Override
    public void onStart(){
        super.onStart();
        checkInQueue();
        //Setup ListView
        checkoffList = (ListView) findViewById(R.id.session_list_checkoff);
        helpmeList = (ListView) findViewById(R.id.session_list_helpMe);

        //Setup list adapter
        checkoffAdapter = new QListAdapter(checkRefAdapter,this, R.layout.q_list_item);
        helpmeAdapter = new QListAdapter(helpRefAdapter, this, R.layout.q_list_item);

        //Set List Adapter
        checkoffList.setAdapter(checkoffAdapter);
        helpmeList.setAdapter(helpmeAdapter);

        //Add list item onClick
        checkoffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectUser(position, "check");
            }
        });
        helpmeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectUser(position, "help");
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
        checkAdd.setOnClickListener(addToQueue("check"));
        helpAdd.setOnClickListener(addToQueue("help"));
        findViewById(R.id.session_title_checkoff).setOnClickListener(addToQueue("check"));
        findViewById(R.id.session_title_helpMe).setOnClickListener(addToQueue("help"));
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
            case R.id.settings:
                if (ninja){
                    showNinjaSettings();
                }
                else {
                    showUserSettings();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    //Show Ninja Settings
    public void showNinjaSettings(){
        final NinjaSettingsDialog dialog = new NinjaSettingsDialog(SessionActivity.this);
        dialog.findViewById(R.id.get_checked_off_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SessionActivity.this, CheckedOffActivity.class);
                in.putExtra("sessionId", sessionId);
                startActivityForResult(in, 1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Show User Settings
    public void showUserSettings(){
        //NOT IN QUEUE YET
        if (!inQueue){
            new AlertDialog.Builder(SessionActivity.this)
                    .setTitle("Get in Line!")
                    .setMessage("Which line would you like to queue up in? You only get to choose 1 at a time!")
                    .setPositiveButton("I need help", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            methodAddToQueue("help");
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Get checked off", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            methodAddToQueue("check");
                            dialog.dismiss();
                        }
                    }).show();
        }
        else {
            if (!curUser.needhelp.equals("false")) new HelpSettingsDialog(SessionActivity.this, username, curUser, sessionId).show();
            else new CheckedSettingsDialog(SessionActivity.this, username, curUser, sessionId).show();
        }
    }

    //Add to Queue - Button listeners
    public View.OnClickListener addToQueue(final String mode){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodAddToQueue(mode);
            }
        };
    }

    public void methodAddToQueue(final String mode){
        Firebase pushref;
        if (ninja) {Toast.makeText(SessionActivity.this, "Hey, you're a ninja! D:",Toast.LENGTH_SHORT).show(); return;}
        if (inQueue){
            Toast.makeText(SessionActivity.this, "Hey! You can't line up twice.", Toast.LENGTH_SHORT).show(); return;
        }

        if (mode.equals("check")){
            pushref = checkRef.child(username);
            curUser.canhelp = "false";
            curUser.needhelp = "false";
            Toast.makeText(SessionActivity.this, "I'll let you know when it's your turn! In the meantime, specify if you can help others in settings!", Toast.LENGTH_LONG).show();}
        else{
            pushref = helpRef.child(username);
            curUser.needhelp = "true";
            Toast.makeText(SessionActivity.this, "I'll let you know when a ninja is ready for you. In the meantime, specify what question you need help on in settings.", Toast.LENGTH_LONG).show();
        }
        pushref.setValue(curUser);
        inQueue = true;
        //Start Notification Service for when name in Queue is first.
                    /*Intent in = new Intent(SessionActivity.this, NotificationService.class);
                    in.putExtra("id",name);
                    in.putExtra("mode", mode);
                    in.putExtra("session", sessionId);
                    startService(in);*/
    }

    //ListView OnClickListener
    public void selectUser(final int position, final String mode){
        if (ninja) showNinjaSelect(position, mode);
        else{showUserSelect(position, mode);}
    }


    //Ninja Select user dialog
    public void showNinjaSelect(final int position, final String mode){
        //Inflate Dialog View
        final View view = SessionActivity.this.getLayoutInflater().inflate(R.layout.ninja_click_dialog,null);

        //Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SessionActivity.this)
                .setView(view)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

        //Create the dialog
        final AlertDialog dialog = builder.create();

        //Set Buttons
        if (mode.equals("check"))
            selected = (User) checkoffAdapter.getItem(position);
        else
            selected = (User) helpmeAdapter.getItem(position);

        //Buttons
        view.findViewById(R.id.checkoff_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOffNinjee(position, mode);
                Toast.makeText(SessionActivity.this, "You've checked off " + selected.username, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.notify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.notify = "true";
                if (mode.equals("check")) checkRef.child(selected.username).setValue(selected);
                else helpRef.child(selected.username).setValue(selected);
                Toast.makeText(SessionActivity.this, "You've notified " + selected.username, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.remove_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("check")) checkRef.child(selected.username).removeValue();
                else helpRef.child(selected.username).removeValue();
                Toast.makeText(SessionActivity.this, "You've removed " + selected.username + " from queue.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        ((TextView)view.findViewById(R.id.ninja_click_title)).setText(selected.fullname);
        dialog.show();
    }

    //User Select user Dialog
    public void showUserSelect(int position, final String mode){
        //Getting who is selected

        if (mode.equals("help")){
            String message = ((User)helpmeAdapter.getItem(position)).needhelp;
            if (message.equals("true")){
                message = "I haven't specified what I need help on.";
            }
            new AlertDialog.Builder(SessionActivity.this)
                    .setTitle("Details")
                    .setMessage(message)
                    .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else if (username.equals(((User)checkoffAdapter.getItem(position)).username) || username.equals(((User)helpmeAdapter.getItem(position)).username)){
            new AlertDialog.Builder(SessionActivity.this)
                    .setTitle("Leave Queue?")
                    .setMessage("Are you sure?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkRef.child(username).removeValue();
                            dialog.dismiss();
                        }
                    });
        }
    }

    //Check off Ninjee
    public void checkOffNinjee(int position, String mode){
        User curItem;
        if (mode.equals("check")){ //Moves from the check off to checked off
            curItem = ((User)checkoffAdapter.getItem(position));

            checkRef.child(curItem.getUsername()).removeValue();
            checkedRef.child(curItem.getUsername()).setValue(curItem);


        } else { //Moves from helpme to checked off
            curItem = ((User)helpmeAdapter.getItem(position));

            helpRef.child(curItem.getUsername()).removeValue();
            checkedRef.child(curItem.getUsername()).setValue(curItem);

        }
    }

    //Firebase user info pull
    public void getFirebaseUserInfo(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (username.equals(dataSnapshot.getValue(User.class).username)) {
                    curUser = dataSnapshot.getValue(User.class);
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
        //Checks if you're second in queue
        checkRef.addChildEventListener(checkQueue());
        helpRef.addChildEventListener(checkQueue());

        //Checks for changes in Queue Status
        checkQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User finding = dataSnapshot.getValue(User.class);
                if (dataSnapshot.getValue() == null){
                    inQueue = false;
                }else {
                    inQueue =  (finding.username.equals(username));
                }
                if (inQueue && finding.notify.equals("true")){
                    notifyUser();
                    curUser.notify = "false";
                    checkRef.child(username).setValue(curUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        helpQueue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User finding = dataSnapshot.getValue(User.class);
                if (dataSnapshot.getValue() == null){
                    inQueue = false;
                }else {
                    inQueue =  (finding.username.equals(username));
                }
                if (inQueue && finding.notify.equals("true")){
                    notifyUser();
                    curUser.notify = "false";
                    checkRef.child(username).setValue(curUser);
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
                check(dataSnapshot,s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                check(dataSnapshot,s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("What happening", dataSnapshot.getName());
                inQueue = !dataSnapshot.getName().equals(username);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                check(dataSnapshot, s);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
    }

    public void check(DataSnapshot dataSnapshot, String s){
        if (s == null){
            inQueue = false;
            before = dataSnapshot.getName();}
        else if (s.equals(before) && dataSnapshot.getName().equals(username)){
            notifyUser();}
        if (dataSnapshot.getName().equals(username)){
            inQueue = true;
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            sessionId = data.getStringExtra("sessionId");
        }
    }
}

