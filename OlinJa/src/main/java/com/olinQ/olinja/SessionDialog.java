package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;

/**
 * Created by chris on 11/13/13.
 */
public class SessionDialog extends AlertDialog {

    String user;
    Context context;

    EditText assignment, place, date, time, duration;

    public SessionDialog(Context context, String user){
        super(context);
        setContentView(R.layout.session_dialog);
        this.context = context;
        this.user = user;
    }

    public void onCreate(Bundle savedInstanceState){
        assignment = (EditText) findViewById(R.id.dialog_input_assignment);
        place = (EditText) findViewById(R.id.dialog_input_place);
        date = (EditText) findViewById(R.id.dialog_input_date);
        time = (EditText) findViewById(R.id.dialog_input_time);
        duration = (EditText) findViewById(R.id.dialog_input_duration);

        Button cancel = (Button) findViewById(R.id.dialog_cancel);
        Button create = (Button) findViewById(R.id.dialog_create);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session newSession = new Session(
                    assignment.getText().toString(),
                    user,
                    place.getText().toString(),
                    date.getText().toString(),
                    time.getText().toString(),
                    duration.getText().toString()
                );

                addSessionToServer(newSession);
                Toast.makeText(getOwnerActivity(), "Session created! You're such a nice person!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addSessionToServer(Session session){
        Firebase sessionRef = new Firebase("https://olinja-base.firebaseio.com/sessions");
        Firebase pushRef = sessionRef.push();

        session.setId(pushRef.getName());
        pushRef.setValue(session.toFireBaseSession());
    }
}
