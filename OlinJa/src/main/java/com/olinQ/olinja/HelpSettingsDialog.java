package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by chris on 11/13/13.
 */
public class HelpSettingsDialog extends AlertDialog {
    Context context;
    Firebase helpRef;
    String username;
    User curUser;
    String HELP_URL = "https://olinja-base.firebaseio.com/help/";

    public HelpSettingsDialog(Context context, String username, User curUser){
        super(context);
        setContentView(R.layout.need_help_dialog);

        this.context = context;
        this.username = username;
        this.curUser = curUser;

        this.helpRef = new Firebase(HELP_URL);
    }

    public void onCreate(Bundle savedInstanceState){
        setTitle("What do you need help with?");
        setButton(BUTTON_POSITIVE, "Save Question", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                curUser.needhelp = ((EditText) findViewById(R.id.question_input)).getText().toString();
                helpRef.child(username).setValue(curUser);
                Toast.makeText(getContext(), "You've saved your question!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        setButton(BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        findViewById(R.id.leave_queue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpRef.child(username).removeValue();
                Toast.makeText(getContext(), "You've left the queue!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
