package com.olinQ.olinja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by chris on 11/18/13.
 */
public class CheckedOffActivity extends Activity {
    //ListView and Adapters
    ListView checkedoffList;
    QListAdapter checkedoffAdapter;

    //Connectivity
    ValueEventListener connected;

    //Firebase URL Location
    String FIREBASE_URL = "https://olinja-base.firebaseio.com/checked";
    Firebase checkedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkedoff);

        //Grab id
        Intent in = getIntent();
        String id = in.getStringExtra("sessionId");
        getSharedPreferences("OlinJa", MODE_PRIVATE).edit().putString("sessionId", id).commit();
        setResult(1, in);
        //Setup Firebase Reference
        checkedRef = new Firebase(FIREBASE_URL).child(id);
    }

    @Override
    public void onStart(){
        super.onStart();

        //Setup ListView
        checkedoffList = (ListView) findViewById(R.id.checked_off_list);

        //Setup list adapter
        checkedoffAdapter = new QListAdapter(checkedRef,this, R.layout.q_list_item);

        //Set List Adapter
        checkedoffList.setAdapter(checkedoffAdapter);


        //Connectivity Check
        connected = checkedRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected)
                    Toast.makeText(CheckedOffActivity.this, "Connected to Assignment!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CheckedOffActivity.this, "Oh no! I can't find the internet!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checked, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_export:
                Toast.makeText(CheckedOffActivity.this, "Checked List sucessfully exported to gradesheet.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
