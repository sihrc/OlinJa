package com.olinQ.olinja;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chris on 11/13/13.
 */
public class SessionAdapter extends ArrayAdapter<Session> {
    Context context;
    ArrayList<Session> sessions;

    SessionHolder holder;
    Session curSession;

    public SessionAdapter(Context context, ArrayList<Session> sessions){
        super(context, R.layout.session_list_item, sessions);
        this.context = context;
        this.sessions = sessions;
    }

    //Hold the session views
    public class SessionHolder{
        TextView title,ninja,time,place;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        //
        if (convertView == null){
            //Grab the correct view in the correct context
            //since it was missing from the arguments.
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.session_list_item, parent, false);
            holder = new SessionHolder();

            //Possible Null Pointer Exception because Java is dumb.
            try {
                holder.title = (TextView) convertView.findViewById(R.id.session_title);
                holder.ninja = (TextView) convertView.findViewById(R.id.session_ninja);
                holder.time = (TextView) convertView.findViewById(R.id.session_time);
                holder.place = (TextView) convertView.findViewById(R.id.session_place);
            } catch (NullPointerException e){e.printStackTrace();Log.i("NullPointerException","Java was dumb!");}
        } else holder = (SessionHolder) convertView.getTag();

        //Grab the current session
        curSession = this.sessions.get(position);

        //Set the text for the views
        holder.title.setText(curSession.assignment);
        holder.ninja.setText(curSession.ninja);
        holder.time.setText(curSession.time); //Might want to do something interesting here - like happening now!
        holder.place.setText(curSession.place);

        return convertView;
    }
}
