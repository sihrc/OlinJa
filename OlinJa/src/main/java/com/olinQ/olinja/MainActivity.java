package com.olinQ.olinja;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class MainActivity extends Activity {
    //For managing the list view of sessions
    ListView sessionList;
    SessionAdapter sessionAdapter;

    //User Information
    String username, fullName, password;
    Boolean ninja;
    User curUser;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String FIREBASE_URL = "https://olinja-base.firebaseio.com";

    //Firebase References
    Firebase sessionRef;
    Firebase userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup Firebase References
        sessionRef = new Firebase(FIREBASE_URL).child("sessions");

        //Check for username
        checkIfUser();
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
                in.putExtra("id", ((Session)sessionAdapter.getItem(position)).id);
                in.putExtra("username", username);
                startActivity(in);
            }
        });

        //Connectivity Check
        connected = sessionRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected)
                    Toast.makeText(MainActivity.this, "Connected to OlinJa!", Toast.LENGTH_SHORT).show();
                //else
                    //Toast.makeText(MainActivity.this, "Oh no! I can't find the internet!", Toast.LENGTH_SHORT).show();
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

    //Dialog for adding a session
    public void showAddSessionDialog(){
        SessionDialog showDialog = new SessionDialog(MainActivity.this, fullName);
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
        switch (item.getItemId()){
            case R.id.action_addSession:
                if (ninja)
                    showAddSessionDialog();
                else
                    Toast.makeText(this, "You gots to be a ninja to make a session!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_relog:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userLogin();                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //User Authentication Methods
    public void checkIfUser(){
        Log.i("SavedId",getSharedPreferences("OlinJa", MODE_PRIVATE).getString("userId",""));
        username = getSharedPreferences("OlinJa", MODE_PRIVATE).getString("userId","failed");
        if (username.equals("failed")){
            userLogin();
        } else {
            if (fullName == null){
                Toast.makeText(this,"Logging in... wait a sec!",Toast.LENGTH_SHORT).show();
                getFirebaseUserInfo();
            }
        }
    }
    public void getFirebaseUserInfo(){
        userRef = new Firebase(FIREBASE_URL).child("users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    userLogin();
                } else {
                curUser = dataSnapshot.getValue(User.class);
                Log.i("Username at Firebase",username);
                    if (username.equals(curUser.username)) {
                        Log.i("FirebaseLogin", "Logging in");
                        fullName = curUser.fullname;
                        if (fullName.equals("failed")){
                            Toast.makeText(MainActivity.this,"Oh no! We lost you :(. Please relog!", Toast.LENGTH_SHORT);
                        }
                        ninja = curUser.ninja.equals("true");
                        Toast.makeText(MainActivity.this, "You've been logged in as " + fullName, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
    public void userLogin(){
        //Inflate Dialog View
        final View view = MainActivity.this.getLayoutInflater().inflate(R.layout.signin_dialog,null);
        //Prompt for username and password
        new AlertDialog.Builder(MainActivity.this)
                .setView(view)
                .setPositiveButton("Ninja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText userInput = (EditText) view.findViewById(R.id.username);
                        EditText passInput = (EditText) view.findViewById(R.id.password);
                        username = userInput.getText().toString();
                        getSharedPreferences("OlinJa",MODE_PRIVATE).edit().putString("userId", username).commit();
                        password = passInput.getText().toString();
                        ninja = true;
                        authenticate();
                    }
                }).setNegativeButton("Student", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                        EditText userInput = (EditText) view.findViewById(R.id.username);
                        EditText passInput = (EditText) view.findViewById(R.id.password);
                        username = userInput.getText().toString();
                        getSharedPreferences("OlinJa",MODE_PRIVATE).edit().putString("userId", username).commit();
                        password = passInput.getText().toString();
                        ninja = false;
                        authenticate();
            }
        }).show();
    }
    public void authenticate(){
        new AsyncTask<Void, Void, String>() {
            HttpResponse response;
            InputStream inputStream = null;
            String result = "";
            HttpClient client = new DefaultHttpClient();

            @Override
            protected void onPreExecute(){
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
                Toast.makeText(MainActivity.this,"Logging in...",Toast.LENGTH_SHORT).show();
            }
            protected String doInBackground(Void... voids) {
                //Website URL and header configuration
                String website = "https://olinapps.herokuapp.com/api/networklogin";
                HttpPost get_auth = new HttpPost(website);
                get_auth.setHeader("Content-type","application/json");

                //Create and execute POST with JSON Post Package
                JSONObject auth = new JSONObject();
                try{
                    auth.put("username",MainActivity.this.username);
                    auth.put("password",MainActivity.this.password);
                    StringEntity se = new StringEntity(auth.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
                    get_auth.setEntity(se);
                }catch(Exception e){e.printStackTrace();}
                try{response = client.execute(get_auth);}catch(Exception e){e.printStackTrace();}

                //Read the response
                HttpEntity entity = response.getEntity();

                try{inputStream = entity.getContent();}catch(Exception e){e.printStackTrace();}
                try{BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
                    StringBuilder sb = new StringBuilder(); String line; String nl = System.getProperty("line.separator");

                    while ((line = reader.readLine())!= null){
                        sb.append(line);
                        sb.append(nl);
                    }
                    result = sb.toString();}catch(Exception e){e.printStackTrace();}

                //Convert Result to JSON
                String username;
                try{
                    auth = new JSONObject(result);
                    JSONObject userID = auth.getJSONObject("user");
                    username = userID.getString("nickname");
                    if (username.equals("")){
                        username = userID.getString("name");
                    }
                    username += "#" + userID.getString("thumbnail");
                }catch(Exception e){e.printStackTrace(); username = "failed";}
                return username;
            }
            protected void onPostExecute(String fullName){
                if (fullName.equals("failed")){
                    Toast.makeText(MainActivity.this, "Authentication failed :(", Toast.LENGTH_SHORT).show();
                    userLogin();
                } else {
                    userRef = new Firebase(FIREBASE_URL).child("users").child(username);
                    String parts[] = fullName.split("#");
                    userRef.setValue(new User(parts[0], username, String.valueOf(ninja),parts[1],"false","false","false"));
                    MainActivity.this.getSharedPreferences("OlinJa",MODE_PRIVATE).edit().putString("userId", username).commit();
                    Toast.makeText(MainActivity.this, "Authentication success :)", Toast.LENGTH_SHORT).show();
                    getFirebaseUserInfo();
                }
            }
        }.execute();
    }
}

