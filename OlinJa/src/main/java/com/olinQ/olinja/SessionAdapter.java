package com.olinQ.olinja;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;

/**
 * Created by chris on 11/13/13.
 */
public class SessionAdapter extends FireBaseAdapter<FirebaseSession> {
    public SessionAdapter(Query ref, Activity activity, int layout){
        super(ref, FirebaseSession.class, layout, activity);
    }

    @Override
    protected void populateView(View view, FirebaseSession session){
        TextView title = (TextView) view.findViewById(R.id.session_title);
        TextView ninja = (TextView) view.findViewById(R.id.session_ninja);
        TextView time = (TextView) view.findViewById(R.id.session_time);
        TextView place = (TextView) view.findViewById(R.id.session_place);

        title.setText(session.assignment);
        ninja.setText(session.ninja);
        time.setText(session.time); // Do Some Date Calculations Here
        place.setText(session.place);
    }
}