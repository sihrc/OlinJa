package com.olinQ.olinja;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.client.Query;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * Created by chris on 11/13/13.
 */
public class SessionAdapter extends FireBaseAdapter<Session> {
    public SessionAdapter(Query ref, Activity activity, int layout){
        super(ref, Session.class, layout, activity);
    }

    @Override
    protected void populateView(View view, final Session session){
        final ImageView profile = (ImageView) view.findViewById(R.id.ninja_profile);
        TextView title = (TextView) view.findViewById(R.id.session_title);
        TextView ninja = (TextView) view.findViewById(R.id.session_ninja);
        TextView details = (TextView) view.findViewById(R.id.session_details);

        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        title.setText(session.assignment);
        ninja.setText(session.ninja);
        details.setText(session.place + " - " +  session.time);

        if (session.picture.equals("")){
            profile.setImageResource(R.drawable.unknown);
        }
        if (profile.getDrawable() == null){
            new AsyncTask<Void, Void, Drawable>(){
                protected Drawable doInBackground(Void... voids){
                    return LoadImageFromWebOperations(session.picture);
                }
                protected void onPostExecute(Drawable draw){
                    profile.setImageDrawable(draw);
                }
            }.execute();
        }
    }


    //Grab profile picture
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            return Drawable.createFromStream((InputStream) new URL("http://www.olinapps.com/" + url).getContent(), "src name");
        } catch (Exception e) {e.printStackTrace();return null;}
    }
}