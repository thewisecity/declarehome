package com.wisecityllc.cookedapp.parseClasses;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;

/**
 * Created by dexterlohnes on 8/14/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {

    final public static String _AUTHOR = "author";
    final public static String _GROUP = "group";
    final public static String _BODY = "body";

    public Message() {
        // A default constructor is required.
    }

    public static void postNewMessage(ParseUser author, Group group, String body){
        Message msg = new Message();
        msg.put(_AUTHOR, author);
        msg.put(_GROUP, group);
        msg.put(_BODY, body);

        msg.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){ //Saving failed

                    Toast.makeText(App.getContext(), "Error while saving message", Toast.LENGTH_SHORT).show();

                }else{ //Saving succeeded

                    Toast.makeText(App.getContext(), "Message created", Toast.LENGTH_SHORT).show();
                    Intent messageSavedIntent = new Intent(App.getContext().getString(R.string.broadcast_message_saved_success));
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(messageSavedIntent);
                }
            }
        });
    }

    public static Message postNewMessageSynchronous (String author, String group, String body){
        Message msg = new Message();
        msg.put(_AUTHOR, author);
        msg.put(_GROUP, group);
        msg.put(_BODY, body);

        try {
            msg.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return msg;
    }

    public ParseUser getAuthor() {
        return getParseUser(_AUTHOR);
    }

    public void setAuthor(ParseUser author) {
        put(_AUTHOR, author);
    }

    public Group getGroup() {
        return (Group)getParseObject(_GROUP);
    }

    public void setGroup(Group group) {
        put(_GROUP, group);
    }

    public String getBody() {
        return getString(_BODY);
    }

    public void setBody(String body) {
        put(_BODY, body);
    }
}
