package com.olinQ.olinja;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 11/18/13.
 */
public class DurationPickerDialog extends TimePickerDialog
{
    //Listener and picker
    final OnTimeSetListener mCallback;
    TimePicker mTimePicker;

    //Specified increment
    final int increment;

    //Public constructor
    public DurationPickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView, int increment)
    {
        super(context, callBack, hourOfDay, minute/increment, true);
        this.mCallback = callBack;
        this.increment = increment;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null && mTimePicker!=null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute()*increment);
        }
    }

    @Override
    protected void onStop()
    {
        // override and do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Override the actual display for minutes
        try
        {
            //Grab Specific TimePicker class
            Class<?> rClass = Class.forName("com.android.internal.R$id");
            Field timePicker = rClass.getField("timePicker");
            this.mTimePicker = (TimePicker)findViewById(timePicker.getInt(null));

            //Grab fields
            Field m = rClass.getField("minute");
            Field h = rClass.getField("hour");

            //Creating a new number spinner for minutes
            NumberPicker mMinuteSpinner = (NumberPicker)mTimePicker.findViewById(m.getInt(null));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60/increment)-1);
            List<String> displayedValues = new ArrayList<String>();
            //Add values to minutes
            for(int i=0;i<60;i+=increment)
            {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));

            //Creating a new number spinner for hours
            NumberPicker mHourSpinner = (NumberPicker)mTimePicker.findViewById(h.getInt(null));
            mMinuteSpinner.setMinValue(0); //Min Value
            mMinuteSpinner.setMaxValue(5); //Max Value
            List<String> hourValues = new ArrayList<String>();
            //Add values to minutes
            for (int i = 0; i < 5; i++) //Iterate through values wanted
            {
                hourValues.add(String.format("%d", i)); //Set Values
            }
            mHourSpinner.setDisplayedValues(hourValues.toArray(new String[0]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       mTimePicker.setOnTimeChangedListener(
               new TimePicker.OnTimeChangedListener() {
                   @Override
                   public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                       setTitle("How Long?");
                   }
               }
       );
    }
}