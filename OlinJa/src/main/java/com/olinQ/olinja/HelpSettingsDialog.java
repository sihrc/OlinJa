package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    public HelpSettingsDialog(Context context, String username, User curUser, String sessionId){
        super(context);
        setContentView(R.layout.need_help_dialog);

        this.context = context;
        this.username = username;
        this.curUser = curUser;

        this.helpRef = new Firebase(HELP_URL + sessionId);
    }

    public void onCreate(Bundle savedInstanceState){
        Button save = (Button)findViewById(R.id.save_question);
        Button cancel = (Button)findViewById(R.id.cancel_question);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curUser.needhelp = ((EditText) findViewById(R.id.question_input)).getText().toString();
                helpRef.child(username).setValue(curUser);
                Toast.makeText(getContext(), "You've saved your question!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
