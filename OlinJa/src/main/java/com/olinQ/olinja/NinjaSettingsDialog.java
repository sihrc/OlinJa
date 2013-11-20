package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by chris on 11/13/13.
 */
public class NinjaSettingsDialog extends AlertDialog {
    Context context;

    public NinjaSettingsDialog(Context context){
        super(context);
        setContentView(R.layout.ninja_settings_dialog);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState){
        Button cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { dismiss();}});


        findViewById(R.id.get_checked_off_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), CheckedOffActivity.class);
                getContext().startActivity(in);
                dismiss();
            }
        });
    }
}
