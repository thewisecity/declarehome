package com.wisecityllc.cookedapp.parseClasses;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.segment.analytics.Analytics;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 8/14/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    final public static String _AUTHOR = "author";
    final public static String _GROUPS = "groups";
    final public static String _BODY = "body";
    final public static String _IS_ALERT = "isAlert";
    final public static String _CATEGORY = "category";

    public Message() {
        // A default constructor is required.
    }

    public static void postNewMessage(ParseUser author, Group group, String body){
        Message msg = new Message();
        msg.setAuthor(author);
        msg.setGroup(group);
        msg.setBody(body);
        msg.put(_IS_ALERT, false);

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){ //Saving failed

                    Toast.makeText(App.getContext(), "Error while saving message", Toast.LENGTH_SHORT).show();
                    Analytics.with(App.getContext()).track("Message Creation Failed");
                }else{ //Saving succeeded

                    Toast.makeText(App.getContext(), "Message created", Toast.LENGTH_SHORT).show();
                    Intent messageSavedIntent = new Intent(App.getContext().getString(R.string.broadcast_message_saved_success));
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(messageSavedIntent);
                    Analytics.with(App.getContext()).track("Message Created");
                }
            }
        });
    }

    public static void postNewAlert(ParseUser author, ArrayList<Group> groups, String body, AlertCategory category){
        Message msg = new Message();
        msg.setAuthor(author);
        msg.setGroups(groups);
        msg.setBody(body);
        msg.put(_IS_ALERT, true);
        msg.put(_CATEGORY, category);

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){ //Saving failed

                    Toast.makeText(App.getContext(), "Error while saving message", Toast.LENGTH_SHORT).show();
                    Analytics.with(App.getContext()).track("Message Creation Failed");
                }else{ //Saving succeeded

                    Toast.makeText(App.getContext(), "Alert created", Toast.LENGTH_SHORT).show();
                    Intent messageSavedIntent = new Intent(App.getContext().getString(R.string.broadcast_message_saved_success));
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(messageSavedIntent);
                    Analytics.with(App.getContext()).track("Alert Created");
                }
            }
        });
    }

    // Unused
//    public static Message postNewMessageSynchronous (String author, String group, String body){
//        Message msg = new Message();
//        msg.put(_AUTHOR, author);
//        msg.put(_GROUP, group);
//        msg.put(_BODY, body);
//
//        try {
//            msg.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return msg;
//    }

    public ParseUser getAuthor() {
        return getParseUser(_AUTHOR);
    }

    public void setAuthor(ParseUser author) {
        put(_AUTHOR, author);
    }

    public ArrayList<Group> getGroups() {
        List<Group> groups = getList(_GROUPS);
        ArrayList<Group> groupsArrayList = new ArrayList<Group>(groups);
        return groupsArrayList;
    }


    public void setGroup(Group group) {
        remove(_GROUPS);
        addUnique(_GROUPS, group);
    }

    public void setGroups(ArrayList<Group> groups) {
        remove(_GROUPS);
        addAllUnique(_GROUPS, groups);
    }

    public String getBody() {
        return getString(_BODY);
    }

    public void setBody(String body) {
        put(_BODY, body);
    }
}
