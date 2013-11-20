package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by chris on 11/13/13.
 */
public class HelpSettingsDialog extends AlertDialog {
    Context context;

    public HelpSettingsDialog(Context context, String user){
        super(context);
        setContentView(R.layout.add_session_dialog);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState){

    }
}