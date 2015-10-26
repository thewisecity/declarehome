package com.wisecityllc.cookedapp.parseClasses;

import android.content.Context;
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
import com.wisecityllc.cookedapp.fragments.AlertsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                    AlertsFragment.shouldReload = true;
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

    public AlertCategory getCategory() {
        return (AlertCategory)getParseObject(_CATEGORY);
    }

    public String getBody() {
        return getString(_BODY);
    }

    public void setBody(String body) {
        put(_BODY, body);
    }

    public String getTimeStamp()
    {

        long timeDiff = getTimeDifference();

        String timestamp = "";

        if (timeDiff < 60) // Less than 1 minute ago
        {
            timestamp = "< 1 minute ago";
        }
        else if (timeDiff < 120) // More than 1 minute, less than 2 minutes
        {
            timestamp = "1 minute ago";
        }
        else if (timeDiff < 3600) // Less than 1 hour ago
        {
            int minutes = (int) (timeDiff / 60);
            timestamp = minutes + " minutes ago";
        }
        else if (timeDiff < 31536000) // Less than 1 year ago
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, hh:mm a");
            timestamp = sdf.format(getCreatedAt());
        }
        else // More than 1 year ago
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd YYYY, hh:mm a");
            timestamp = sdf.format(getCreatedAt());
        }

        //if < 1 minute return "< 1 minute ago"

        // if 1-2 minutes return "1 minute ago"

        //if < 1 hour return "NN minutes ago"

        // if < 1 year ago return "mmm dd, hh:mm pm/am"

        // else return same but with year added

        return timestamp;
    }

    /**
     *
     * @return long representing the time difference between the current time and the creation time of this message in seconds
     */
    private long getTimeDifference()
    {
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
//        Date d = sdf.parse("Mon May 27 11:46:15 IST 2013");

        Calendar c = Calendar.getInstance();
        Date d = this.getCreatedAt();
        c.setTime(d);
        long time = c.getTimeInMillis();
        long curr = System.currentTimeMillis();
        long diff = curr - time;    //Time difference in milliseconds
        return diff/1000;
    }

    public void copyMessageText() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(this.getBody());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

            android.content.ClipData clip = android.content.ClipData.newPlainText(getCategory() != null ? getCategory().getTitle() : "message", getBody());
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(App.getContext(), "Copied message body to clipboard", Toast.LENGTH_SHORT).show();
    }
}
