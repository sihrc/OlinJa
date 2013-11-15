package com.olinQ.olinja;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by chris on 11/13/13.
 */
public class SessionDialog extends AlertDialog {

    Session session;
    Context context;

    public SessionDialog(Context context, Session curSession){
        super(context);
        setContentView(R.layout.session_dialog);
        this.context = context;
        this.session = curSession;
    }
}
