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
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;

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

    private boolean mUseLocalStorageForMembers = false;
    private boolean mUseLocalStorageForAdmins = false;

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

    public void setGroupHashId(Number groupHashId) {
        put("groupHashId", groupHashId);
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
        List<ParseUser> admins = getList("adminsArray");
        if(admins!= null)
            return new ArrayList<ParseUser>(admins);
        else
            return new ArrayList<ParseUser>();
    }

    public ArrayList<ParseUser> getMembersArray() {
        List<ParseUser> members = getList("membersArray");
        if(members != null)
            return new ArrayList<ParseUser>(members);
        else
            return new ArrayList<ParseUser>();
    }

    public ParseRole getMembersRole() { return (ParseRole)getParseObject("membersRole"); }

    public ParseRole getAdminsRole() { return (ParseRole)getParseObject("adminsRole"); }

    public static void refreshLocalGroupPermissions() {
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        currentUser.fetchInBackground(new GetCallback<ParseUser>() {
//            @Override
//            public void done(ParseUser currentUser, ParseException e) {
//                if(currentUser != null) {
//                    ParseRelation<Group> adminOf = currentUser.getRelation("adminOf");
//                    adminOf.getQuery().findInBackground(new FindCallback<Group>() {
//                        @Override
//                        public void done(List<Group> list, ParseException e) {
//                            for(Group group : list){
//                                try{
//                                    group.pin();
//                                } catch (ParseException err) {
//                                    err.printStackTrace();
//                                }
//
//                            }
//                        }
//                    });
//
//                    ParseRelation<Group> memberOf = currentUser.getRelation("memberOf");
//                    memberOf.getQuery().findInBackground(new FindCallback<Group>() {
//                        @Override
//                        public void done(List<Group> list, ParseException e) {
//                            for(Group group : list){
//                                try{
//                                    group.pin();
//                                } catch (ParseException err) {
//                                    err.printStackTrace();
//                                }
//
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }


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

    public boolean isUserMember(ParseUser user, boolean forceServerContact) {

        boolean userIsMember = false;

        try{

            if(forceServerContact == true)
                this.fetch();

            ArrayList<ParseUser> allMembers = getAdminsArray();
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