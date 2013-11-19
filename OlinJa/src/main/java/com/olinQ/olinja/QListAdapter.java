package com.olinQ.olinja;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;

/**
 * Created by chris on 11/13/13.
 */
public class QListAdapter extends FireBaseAdapter<User> {
    public QListAdapter(Query ref, Activity activity, int layout){
        super(ref, User.class, layout, activity);
    }

    @Override
    protected void populateView(View view, User queueItem){
        TextView person = (TextView) view.findViewById(R.id.person_name);
        person.setText(queueItem.user);
    }
}