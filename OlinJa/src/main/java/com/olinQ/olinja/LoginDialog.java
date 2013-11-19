package com.olinQ.olinja;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
public class LoginDialog extends AlertDialog {
    Context context;
    EditText username;
    public LoginDialog(Context context){
        super(context);
        setContentView(R.layout.login);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState){
        username = (EditText) findViewById(R.id.dialog_input_name);
        Button ninja = (Button) findViewById(R.id.login_ninja);
        Button student = (Button) findViewById(R.id.login_student);

        //If Ninja
        ninja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheName("true");
            }
        });

        //If Student
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheName("false");
            }
        });
    }

    //Commit to shared preferences
    public void getTheName(String mode){
        String user = username.getText().toString();
        if (!user.equals("")){
            SharedPreferences prefs = getContext().getSharedPreferences("OlinJa", 0);
            prefs.edit().putString("username",user).commit();
            prefs.edit().putString("ninja",mode).commit();
            dismiss();
        } else Toast.makeText(getContext(), "Give yourself a name please!", Toast.LENGTH_SHORT).show();
    }
}
