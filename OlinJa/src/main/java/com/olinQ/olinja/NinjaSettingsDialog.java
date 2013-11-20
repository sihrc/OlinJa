package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by chris on 11/13/13.
 */
public class NinjaSettingsDialog extends AlertDialog {
    Context context;

    public NinjaSettingsDialog(Context context){
        super(context);
        setContentView(R.layout.ninja_click_dialog);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState){
        setButton(BUTTON_POSITIVE, );
    }
}