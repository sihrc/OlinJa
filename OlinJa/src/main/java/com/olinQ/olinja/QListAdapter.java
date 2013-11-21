package com.olinQ.olinja;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
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
            if (queueItem.canhelp.equals("false")){
                helpLight.setImageResource(R.drawable.yellow_help);
                help.setText("Away");}
            else{
                helpLight.setImageResource(R.drawable.green_help);
                help.setText("I can help!");
            }
        } else { //Person needs help is in help line
            if (queueItem.needhelp.equals("true")){
                help.setText("I need help ...");
            }
            else if (queueItem.needhelp.length() > 7){
            help.setText(queueItem.needhelp.substring(0,7) + "...");
            }
            else {
                help.setText(queueItem.needhelp);
            }
            helpLight.setVisibility(View.GONE);
        }
        //Find the profile picture and set to view in asyncTask;
        if (queueItem.picture.equals("")){
            profile.setImageResource(R.drawable.unknown);
        }
        if (profile.getDrawable() == null){
        new AsyncTask<Void, Void, Drawable>(){
            protected Drawable doInBackground(Void... voids){
                return LoadImageFromWebOperations(queueItem.picture);
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