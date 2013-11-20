package com.olinQ.olinja;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

    public CheckedSettingsDialog(Context context, String user){
        super(context);
        setContentView(R.layout.add_session_dialog);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState){

    }
}