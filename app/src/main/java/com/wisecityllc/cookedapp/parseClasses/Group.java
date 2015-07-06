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

//import com.parse.PFObject;

/**
 * Created by dexterlohnes on 6/30/15.
 */
@ParseClassName("Group")
public class Group extends ParseObject {

    final static String _NAME = "name";
    final static String _PURPOSE = "purpose";
    final static String _NEIGHBERHOODS = "neighberhoods";
    final static String _ADDRESS = "address";
    final static String _STATE = "state";
    final static String _CITY = "city";
    final static String _WEBSITE = "website";
    final static String _FACEBOOK = "facebook";
    final static String _TWITTER = "twitter";

    public Group() {
        // A default constructor is required.
    }

    public static void createGroup(String name, String purpose, String neighberhoods, String address, String city, String state, String website, String facebook, String twitter){
        Group group = new Group();
        group.put(_NAME, name);
        group.put(_PURPOSE, purpose);
        group.put(_NEIGHBERHOODS, neighberhoods);
        group.put(_ADDRESS, address);
        group.put(_STATE, state);
        group.put(_CITY, city);
        group.put(_WEBSITE, website);
        group.put(_TWITTER, twitter);
        group.put(_FACEBOOK, facebook);

        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e != null){ //Saving failed

                    Toast.makeText(App.getContext(), "Error while saving group", Toast.LENGTH_SHORT).show();

                }else{ //Saving succeeded

                    Toast.makeText(App.getContext(), "Group saved", Toast.LENGTH_SHORT).show();
                    Intent groupSavedIntent = new Intent(App.getContext().getString(R.string.broadcast_group_saved_success));
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(groupSavedIntent);
                }
            }
        });
    }

    public static Group createGroupSynchronous(String name, String purpose, String neighberhoods, String address, String city, String state, String website, String facebook, String twitter){
        Group group = new Group();
        group.put(_NAME, name);
        group.put(_PURPOSE, purpose);
        group.put(_NEIGHBERHOODS, neighberhoods);
        group.put(_ADDRESS, address);
        group.put(_STATE, state);
        group.put(_CITY, city);
        group.put(_WEBSITE, website);
        group.put(_TWITTER, twitter);
        group.put(_FACEBOOK, facebook);

        try {
            group.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return group;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    //not yet in use
    public ParseUser getAdmin() {
        return getParseUser("admin");
    }

    //not yet in use
    public void setAdmin(ParseUser user) {
        put("admin", user);
    }

    public String getPurpose() {
        return getString("purpose");
    }

    public void setPurpose(String purpose) {
        put("purpose", purpose);
    }

    public String getNeighberhoods() {
        return getString("neighberhoods");
    }

    public void setNeighberhoods(String neighberhoods) {
        put("neighberhoods", neighberhoods);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        put("city", city);
    }

    public String getState() {
        return getString("state");
    }

    public void setState(String state) {
        put("state", state);
    }

    public String getWebsite() {
        return getString("website");
    }

    public void setWebsite(String website) {
        put("website", website);
    }

    public String getFacebook() {
        return getString("facebook");
    }

    public void setFacebook(String facebook) {
        put("facebook", facebook);
    }

    public String getTwitter() {
        return getString("twitter");
    }

    public void setTwitter(String twitter) {
        put("twitter", twitter);
    }
}