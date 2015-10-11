package com.wisecityllc.cookedapp.parseClasses;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.utilities.Notifications;

import java.util.ArrayList;
import java.util.List;

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
    final static String _MEMBERS_ARRAY = "membersArray";
    final static String _ADMINS_ARRAY = "adminsArray";
    final static String _MEMBERS_ROLE = "membersRole";
    final static String _ADMINS_ROLE = "adminsRole";

    private boolean mUseLocalStorageForMembers = false;
    private boolean mUseLocalStorageForAdmins = false;

    public Group() {
        // A default constructor is required.
    }

    public static void createGroup(final String name, final String purpose, final String neighberhoods, final String address, final String city, final String state, final String website, final String facebook, final String twitter){
        final Group group = new Group();
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
                    Analytics.with(App.getContext()).track("Group Creation Failed");

                }else{ //Saving succeeded

                    Toast.makeText(App.getContext(), "Group saved", Toast.LENGTH_SHORT).show();
                    Intent groupSavedIntent = new Intent(App.getContext().getString(R.string.broadcast_group_saved_success));
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(groupSavedIntent);

                    //Update notifications
                    Notifications.subscribeToNotifsForNewGroup(group);
                    Analytics.with(App.getContext()).track("Group Created",
                            new Properties().
                                    putValue(_NAME, name).
                                    putValue(_PURPOSE, purpose).
                                    putValue(_NEIGHBERHOODS, neighberhoods).
                                    putValue(_ADDRESS, address).
                                    putValue(_STATE, state).
                                    putValue(_CITY, city).
                                    putValue(_WEBSITE, website).
                                    putValue(_TWITTER, twitter).
                                    putValue(_FACEBOOK, facebook));
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

    public String getGroupHashId() {
        Number hashId = getNumber("groupHashId");
        Log.d("Dex", hashId.toString());
        return hashId.toString();
    }

    public void setName(String name) {
        put("name", name);
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

    public ArrayList<ParseUser> getAdminsArray() {
        List<ParseUser> admins = getList(_ADMINS_ARRAY);
        if(admins!= null)
            return new ArrayList<ParseUser>(admins);
        else
            return new ArrayList<ParseUser>();
    }

    public ArrayList<ParseUser> getMembersArray() {
        List<ParseUser> members = getList(_MEMBERS_ARRAY);
        if(members != null)
            return new ArrayList<ParseUser>(members);
        else
            return new ArrayList<ParseUser>();
    }

    public ParseRole getMembersRole() { return (ParseRole)getParseObject(_MEMBERS_ROLE); }

    public ParseRole getAdminsRole() { return (ParseRole)getParseObject(_ADMINS_ROLE); }


    public boolean isCurrentUserMember() {
        boolean isMember = isUserMember(ParseUser.getCurrentUser(), false);
        return isMember;
    }

    public boolean isCurrentUserAdmin() {
        boolean isAdmin = isUserAdmin(ParseUser.getCurrentUser(), false);
        return isAdmin;
    }

    public boolean isUserAdmin(ParseUser user, boolean forceServerContact) {

        boolean userIsAdmin = false;

        try{

            if(forceServerContact == true)
                this.fetch();

            ArrayList<ParseUser> allAdmins = getAdminsArray();
            for(ParseUser admin : allAdmins) {
                if(admin.getObjectId().equalsIgnoreCase(user.getObjectId())) {
                    userIsAdmin = true;
                    break;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return userIsAdmin;
    }

    public void addMember(ParseUser user) {
        add(_MEMBERS_ARRAY, user);
    }

    public boolean isUserMember(ParseUser user, boolean forceServerContact) {

        boolean userIsMember = false;

        try{

            if(forceServerContact == true)
                this.fetch();

            ArrayList<ParseUser> allMembers = getMembersArray();
            for(ParseUser member : allMembers) {
                if(member.getObjectId().equalsIgnoreCase(user.getObjectId())) {
                    userIsMember = true;
                    break;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return userIsMember;
    }
}