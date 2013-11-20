package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        setButton(BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {dialog.dismiss();}});
        getButton(BUTTON_NEGATIVE).setEnabled(false);

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
