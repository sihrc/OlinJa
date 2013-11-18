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
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chris on 11/13/13.
 */
public class SessionDialog extends AlertDialog {

    String user;
    Context context;

    EditText assignment, place;
    TextView date,time,duration;
    public SessionDialog(Context context, String user){
        super(context);
        setContentView(R.layout.session_dialog);
        this.context = context;
        this.user = user;
    }
    /*assignment,ninja,place,time,duration, date, check, help, checked, id*/
    public void onCreate(Bundle savedInstanceState){
        assignment = (EditText) findViewById(R.id.dialog_input_assignment);
        place = (EditText) findViewById(R.id.dialog_input_place);
        date = (TextView) findViewById(R.id.dialog_input_date);
        time = (TextView) findViewById(R.id.dialog_input_time);
        duration = (TextView) findViewById(R.id.dialog_input_duration);

        Button cancel = (Button) findViewById(R.id.dialog_cancel);
        Button create = (Button) findViewById(R.id.dialog_create);

        setupDateTimePickers();
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
                    time.getText().toString(),
                    duration.getText().toString(),
                    date.getText().toString(),
                    "",
                    "",
                    ""
                );
                addSessionToServer(newSession);
                Toast.makeText(getContext(), "Session created! You're such a nice person!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void setupDateTimePickers(){
        //Making the EditText essentially a button...
        date.setFocusable(false);   time.setFocusable(false);  duration.setFocusable(false);
        date.setClickable(true);    time.setClickable(true);   duration.setClickable(true);
        //Date Picker for date
        setDatePicker();
        setTimePicker();
        setDurationPicker();
    }
    //Set the onClickListener for date EditText
    public void setDatePicker(){
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                                myCalendar.set(Calendar.YEAR, i);
                                myCalendar.set(Calendar.MONTH, i2);
                                myCalendar.set(Calendar.DAY_OF_MONTH, i3);
                                String myFormat = "MM/dd/yy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                date.setText(sdf.format(myCalendar.getTime()));
                            }
                        },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });it add .
        git commit -am ''
    }
    //Set the onClickListener for time EditText
    public void setTimePicker(){
        //Time picker for time
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute;
                String curTime = time.getText().toString();
                if (curTime.equals("")){
                    final Calendar setTime = Calendar.getInstance();
                    hour = setTime.get(Calendar.HOUR_OF_DAY);
                    minute = setTime.get(Calendar.MINUTE);
                }
                else
                {
                    String[] splitTime = curTime.split(":");
                    hour = Integer.valueOf(splitTime[0]);
                    if (splitTime[1].substring(2,4).equals("PM")){
                        hour += 12;
                    }
                    minute = Integer.valueOf(splitTime[1].substring(0, 2));
                }

                TimePickerDialog timePicker = new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                        String inputTime, AMPM;
                        if (selectedHour >= 12){
                            if (selectedHour%12 == 0) inputTime = "12";
                            else inputTime = String.valueOf(selectedHour%12);
                            AMPM = "PM";}
                        else{
                            if (selectedHour%12 == 0) inputTime = "12";
                            else inputTime = String.valueOf(selectedHour%12);
                            AMPM = "AM";}
                        String minute = String.valueOf(selectedMinute);
                        if (minute.length() < 2){
                            minute = "0" + minute;
                        }
                        inputTime = inputTime + ":" + minute + AMPM;
                        time.setText(inputTime);
                    }},hour,minute,false);
                timePicker.setTitle("Select Start Time");
                timePicker.show();
            }
        });
    }
    //Set the onClickListener for duration EditText
    public void setDurationPicker(){
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0;
                int minute = 0;
                DurationPickerDialog durationPicker = new DurationPickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        duration.setText(String.valueOf(hourOfDay) + " hr " + String.valueOf(minute) + " min");
                    }
                },hour,minute,true,15);

                durationPicker.setTitle("How Long?");
                durationPicker.show();
            }
        });
    }
    public void addSessionToServer(Session session){
        Firebase sessionRef = new Firebase("https://olinja-base.firebaseio.com/sessions");
        Firebase pushRef = sessionRef.push();

        session.setId(pushRef.getName());
        pushRef.setValue(session);
    }
}
