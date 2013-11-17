package com.olinQ.olinja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by chris on 11/17/13.
 */
public class SessionActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_detail_view);

        Intent in = getIntent();
        String id = in.getStringExtra("id");
        populateCheckList();
        populateHelpList();

    }

    public void populateCheckList(){

    }
    public void populateHelpList(){

    }
}
