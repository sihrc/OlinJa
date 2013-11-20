package com.olinQ.olinja;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.client.Query;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by chris on 11/13/13.
 */
public class QListAdapter extends FireBaseAdapter<User> {
    ImageView profile;
    public QListAdapter(Query ref, Activity activity, int layout){
        super(ref, User.class, layout, activity);
    }

    @Override
    protected void populateView(View view, final User queueItem){
        //Grabbing Views
        TextView person = (TextView) view.findViewById(R.id.person_name);
        TextView help = (TextView) view.findViewById(R.id.help_text);
        ImageView helpLight = (ImageView) view.findViewById(R.id.help_button);
        profile = (ImageView) view.findViewById(R.id.user_profile);

        //Set the name
        person.setText(queueItem.fullname);

        //Setting help and helpLight
        if (queueItem.needhelp.equals("false")){ //If person does not need help / is in check off queue
            helpLight.setVisibility(View.VISIBLE);
            if (queueItem.canhelp.equals("false"))
                helpLight.setImageResource(R.drawable.yellow_help);
            else{
                helpLight.setImageResource(R.drawable.green_help);
                help.setText("Available to help!");
            }
        } else { //Person needs help is in help line
            help.setText(queueItem.needhelp);
            helpLight.setVisibility(View.INVISIBLE);
        }

        //Find the profile picture and set to view in asyncTask;
        new AsyncTask<Void, Void, Drawable>(){
            protected Drawable doInBackground(Void... voids){
                return LoadImageFromWebOperations(queueItem.picture);
            }
            protected void onPostExecute(Drawable draw){
                profile.setImageDrawable(draw);
            }
        }.execute();
    }



    //Grab profile picture
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
        return Drawable.createFromStream((InputStream) new URL("https://olinapps.herokuapp.com/" + url).getContent(), "src name");
        } catch (Exception e) {e.printStackTrace();return null;}
    }
}