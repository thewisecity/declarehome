package com.wisecityllc.cookedapp.parseClasses;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
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

    public ParseRole getMembersRole() { return (ParseRole)getParseObject("membersRole"); }

    public ParseRole getAdminsRole() { return (ParseRole)getParseObject("adminsRole"); }

    public static void refreshLocalGroupPermissions() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser currentUser, ParseException e) {
                ParseRelation<Group> adminOf = currentUser.getRelation("adminOf");
                adminOf.getQuery().findInBackground(new FindCallback<Group>() {
                    @Override
                    public void done(List<Group> list, ParseException e) {
                        for(Group group : list){
                            try{
                                group.pin();
                            } catch (ParseException err) {
                                err.printStackTrace();
                            }

                        }
                    }
                });

                ParseRelation<Group> memberOf = currentUser.getRelation("memberOf");
                memberOf.getQuery().findInBackground(new FindCallback<Group>() {
                    @Override
                    public void done(List<Group> list, ParseException e) {
                        for(Group group : list){
                            try{
                                group.pin();
                            } catch (ParseException err) {
                                err.printStackTrace();
                            }

                        }
                    }
                });

            }
        });
    }

//    public boolean isCurrentUserMember() {
//        ParseUser currentUser;
//        try {
//            currentUser = ParseUser.getCurrentUser();
//            currentUser.fetchFromLocalDatastore();
//
//        } catch (ParseException e) {
//            Toast.makeText(App.getContext(), "Couldn't find local version of user", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            return false;
//        }
//        try {
//
//            ParseRelation<Group> memberOf = currentUser.getRelation("memberOf");
//
//            ParseQuery memberQuery = memberOf.getQuery().
//                    fromLocalDatastore().
//                    whereEqualTo("objectId", this.getObjectId());
//
//            Group memberGroup =  (Group) memberQuery.getFirst();
//            if(memberGroup != null)
//                return true;
//            else
//                return false;
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean isCurrentUserAdmin() {
//        ParseUser currentUser;
//        try {
//            currentUser = ParseUser.getCurrentUser();
//            currentUser.fetchFromLocalDatastore();
//
//        } catch (ParseException e) {
//            Toast.makeText(App.getContext(), "Couldn't find local version of user", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            return false;
//        }
//
//        try{
//
//            ParseRelation<Group> adminOf = currentUser.getRelation("adminOf");
//
//            ParseQuery adminQuery = adminOf.getQuery().
//                    fromLocalDatastore().
//                    whereEqualTo("objectId", this.getObjectId());
//
//            Group adminGroup =  (Group) adminQuery.getFirst();
//            if(adminGroup != null)
//                return true;
//            else
//                return false;
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

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

            if(mUseLocalStorageForAdmins == true && forceServerContact == false){
                ParseRole adminsRole = this.getAdminsRole();
                adminsRole.fetchFromLocalDatastore();
                ParseRelation<ParseUser> admins = adminsRole.getUsers();
//                ParseQuery adminQuery = adminOf.getQuery()
                ParseQuery<ParseUser> adminQuery = admins.getQuery()
                        .whereEqualTo("objectId", user.getObjectId())
                        .fromLocalDatastore();

                ParseUser userAsAdmin =  (ParseUser) adminQuery.getFirst();
                if(userAsAdmin != null)
                    userIsAdmin = true;
                else
                    userIsAdmin = false;
            } else {
                ParseRole adminsRole = this.getAdminsRole();
                adminsRole.fetch();
                adminsRole.pin();
                ParseRelation<ParseUser> admins = adminsRole.getUsers();
                // We haven't retrieved or we're being forced to contact server
                // Get all members
                ParseQuery<ParseUser> adminQuery = admins.getQuery();
                ArrayList<ParseUser> adminList = new ArrayList<ParseUser>(adminQuery.find());
                // Loop through all of our members, pinning all of them for future use
                for(ParseUser admin : adminList) {
                    admin.pin();
                    // If we come across the user we are searching for, we want to return that they are a member
                    // but not before we finishing pinning all of the rest of the members
                    if(admin.getObjectId().equalsIgnoreCase(user.getObjectId()))
                        userIsAdmin = true;
                }
                mUseLocalStorageForAdmins = true;
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

            if(mUseLocalStorageForMembers == true && forceServerContact == false){
                ParseRole membersRole = this.getMembersRole();
                membersRole.fetchFromLocalDatastore();
                ParseRelation<ParseUser> members = membersRole.getUsers();
//                ParseQuery adminQuery = adminOf.getQuery()
                ParseQuery<ParseUser> memberQuery = members.getQuery()
                        .whereEqualTo("objectId", user.getObjectId())
                        .fromLocalDatastore();

                ParseUser userAsMember =  (ParseUser) memberQuery.getFirst();
                if(userAsMember != null)
                    userIsMember = true;
                else
                    userIsMember = false;
            } else {
                ParseRole membersRole = this.getMembersRole();
                membersRole.fetch();
                membersRole.pin();
                ParseRelation<ParseUser> members = membersRole.getUsers();
                // We haven't retrieved or we're being forced to contact server
                // Get all members
                ParseQuery<ParseUser> memberQuery = members.getQuery();
                ArrayList<ParseUser> memberList = new ArrayList<ParseUser>(memberQuery.find());
                // Loop through all of our members, pinning all of them for future use
                for(ParseUser member : memberList) {
                    member.pin();
                    // If we come across the user we are searching for, we want to return that they are a member
                    // but not before we finishing pinning all of the rest of the members
                    if(member.getObjectId().equalsIgnoreCase(user.getObjectId()))
                        userIsMember = true;
                }
                mUseLocalStorageForMembers = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return userIsMember;
    }

//    public boolean isUserAdmin(ParseUser user, boolean forceServerContact) {
//
//        boolean userIsAdmin = false;
//
//        try{
//
//            ParseRelation<Group> adminOf = user.getRelation("adminOf");
//
//            if(mUseLocalStorageForAdmins == true && forceServerContact == false){
//                ParseQuery adminQuery = adminOf.getQuery()
//                        .whereEqualTo("objectId", this.getObjectId())
//                        .fromLocalDatastore();
//
//                Group adminGroup =  (Group) adminQuery.getFirst();
//                if(adminGroup != null)
//                    userIsAdmin = true;
//                else
//                    userIsAdmin = false;
//            } else {
//                // We haven't retrieved or we're being forced to contact server
//                // Get all members
//                ParseQuery adminQuery = adminOf.getQuery();
//                ArrayList<ParseUser> adminList = new ArrayList<ParseUser>(adminQuery.find());
//                // Loop through all of our members, pinning all of them for future use
//                for(ParseUser admin : adminList) {
//                    admin.pin();
//                    // If we come across the user we are searching for, we want to return that they are a member
//                    // but not before we finishing pinning all of the rest of the members
//                    if(admin.getObjectId().equalsIgnoreCase(user.getObjectId()))
//                        userIsAdmin = true;
//                }
//                mUseLocalStorageForAdmins = true;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return userIsAdmin;
//    }
//
//    public boolean isUserMember(ParseUser user, boolean forceServerContact) {
//
//        boolean userIsMember = false;
//
//        try{
//
//            ParseRelation<Group> memberOf = user.getRelation("memberOf");
//
//            if(mUseLocalStorageForMembers == true && forceServerContact == false){
//                ParseQuery memberQuery = memberOf.getQuery()
//                        .whereEqualTo("objectId", this.getObjectId())
//                        .fromLocalDatastore();
//
//                Group memberGroup =  (Group) memberQuery.getFirst();
//                if(memberGroup != null)
//                    userIsMember = true;
//                else
//                    userIsMember = false;
//            } else {
//                // We haven't retrieved or we're being forced to contact server
//                // Get all members
//                ParseQuery memberQuery = memberOf.getQuery();
//                ArrayList<ParseUser> memberList = new ArrayList<ParseUser>(memberQuery.find());
//                // Loop through all of our members, pinning all of them for future use
//                for(ParseUser member : memberList) {
//                    member.pin();
//                    // If we come across the user we are searching for, we want to return that they are a member
//                    // but not before we finishing pinning all of the rest of the members
//                    if(member.getObjectId().equalsIgnoreCase(user.getObjectId()))
//                        userIsMember = true;
//                }
//
//                mUseLocalStorageForMembers = true;
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return userIsMember;
//    }
}