package com.olinQ.olinja;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by chris on 11/13/13.
 */
public class CheckedSettingsDialog extends AlertDialog {
    Context context;
    Firebase checkRef;
    String username;
    User curUser;
    String CHECK_URL = "https://olinja-base.firebaseio.com/check/";

    public CheckedSettingsDialog(Context context, String username, User curUser, String sessionId){
        super(context);
        setContentView(R.layout.check_dialog);
        this.context = context;
        this.username = username;
        this.curUser = curUser;
        checkRef = new Firebase(CHECK_URL + sessionId);
    }

    public void onCreate(Bundle savedInstanceState){
        setButton(BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curUser.canhelp = "true";
                checkRef.child(username).setValue(curUser);
                Toast.makeText(getContext(), "You're available!", Toast.LENGTH_SHORT).show();
            }});
        setButton(BUTTON_NEGATIVE,"No",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        curUser.canhelp = "false";
                        checkRef.child(username).setValue(curUser);
                        Toast.makeText(getContext(), "You're not available.", Toast.LENGTH_SHORT).show();
                    }});
        findViewById(R.id.leave_queue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRef.child(username).removeValue();
                Toast.makeText(getContext(), "You've left the queue!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
