package com.wisecityllc.cookedapp.utilities;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.parseClasses.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 8/7/15.
 */
public class Notifications {

    public static final String NEW_MESSAGES = "NewMessage";
    public static final String NEW_EVENTS = "NewEvent";
    public static final String ALERTS = "Alert";
    public static final String INVITATION_ACCEPTED = "InvitationAccepted";
    public static final String MEMBERSHIP_REQUEST = "MembershipRequest";

    public static void setSubscriptionForAllNotifs(final boolean subscribed) {
        setSubscriptionForNewEvents(subscribed);
        setSubscriptionForNewMessages(subscribed);
        setSubscriptionForAlerts(subscribed);
        setSubscriptionForInvitationAcceptedChannel(subscribed);
        setSubscriptionForMembershipRequested(subscribed);
    }

    public static void setSubscriptionForNewMessages(final boolean subscribed){

        ParseQuery<Group> allGroupsQuery = getAllGroupsQuery();

        allGroupsQuery.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> results, ParseException e) {
                if (e != null) {
                    // There was an error
                    Log.d("ERROR", e.getMessage());
                } else {
                    // results contains all of the groups of which our user is either an admin or a member
                    for(Group group : results){
                        setSubscriptionForNewMessagesChannelForGroup(subscribed, group);
                    }
                }
            }
        });
    }

    public static void setSubscriptionForNewEvents(final boolean subscribed){
        ParseQuery<Group> allGroupsQuery = getAllGroupsQuery();

        allGroupsQuery.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> results, ParseException e) {
                if (e != null) {
                    // There was an error
                    Log.d("ERROR", e.getMessage());
                } else {
                    // results contains all of the groups of which our user is either an admin or a member
                    for(Group group : results){
                        setSubscriptionForNewEventsChannelForGroup(subscribed, group);
                    }
                }
            }
        });
    }

    public static void setSubscriptionForAlerts(final boolean subscribed){
        ParseQuery<Group> allGroupsQuery = getAllGroupsQuery();

        allGroupsQuery.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> results, ParseException e) {
                if (e != null) {
                    // There was an error
                    Log.d("ERROR", e.getMessage());
                } else {
                    // results contains all of the groups of which our user is either an admin or a member
                    for(Group group : results){
                        setSubscriptionForAlertsChannelForGroup(subscribed, group);
                    }
                }
            }
        });
    }

    public static void setSubscriptionForInvitationAcceptedChannel(boolean subscribed){
        String channel = INVITATION_ACCEPTED + "_" + ParseUser.getCurrentUser().getObjectId();
        if(subscribed)
            ParsePush.subscribeInBackground(channel);
        else
            ParsePush.unsubscribeInBackground(channel);
    }

    public static void setSubscriptionForMembershipRequested(final boolean subscribed){
        ParseQuery<Group> allGroupsQuery = getAllGroupsQuery();

        allGroupsQuery.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> results, ParseException e) {
                if (e != null) {
                    // There was an error
                    Log.d("ERROR", e.getMessage());
                } else {
                    // results contains all of the groups of which our user is either an admin or a member
                    for(Group group : results){
                        setSubscriptionForMemberShipRequestedForGroup(subscribed, group);
                    }
                }
            }
        });
    }




    private static void setSubscriptionForNewMessagesChannelForGroup(boolean subscribed, Group group){
        String channel = NEW_MESSAGES + "_" + group.getObjectId();
        if(subscribed)
            ParsePush.subscribeInBackground(channel);
        else
            ParsePush.unsubscribeInBackground(channel);
    }

    private static void setSubscriptionForNewEventsChannelForGroup(boolean subscribed, Group group){
        String channel = NEW_EVENTS + "_" + group.getObjectId();
        if(subscribed)
            ParsePush.subscribeInBackground(channel);
        else
            ParsePush.unsubscribeInBackground(channel);
    }

    private static void setSubscriptionForAlertsChannelForGroup(boolean subscribed, Group group){
        String channel = ALERTS + "_" + group.getObjectId();
        if(subscribed)
            ParsePush.subscribeInBackground(channel);
        else
            ParsePush.unsubscribeInBackground(channel);
    }



    private static void setSubscriptionForMemberShipRequestedForGroup(boolean subscribed, Group group){
        String channel = MEMBERSHIP_REQUEST + "_" + group.getObjectId();
        if(subscribed)
            ParsePush.subscribeInBackground(channel);
        else
            ParsePush.unsubscribeInBackground(channel);
    }

    private static ParseQuery<Group> getAllGroupsQuery() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        try {
            currentUser.fetchIfNeeded();
        } catch (ParseException e){

        }

        ParseRelation<Group> memberOfRelation = currentUser.getRelation("memberOf");
        ParseRelation<Group> adminOfRelation = currentUser.getRelation("adminOf");



        //First we have to make a list of all the queries we want to combine rather than doing multiple queries then combining ourselves
        List<ParseQuery<Group>> queries = new ArrayList<>();
        ParseQuery memberQuery = memberOfRelation.getQuery();
        ParseQuery adminQuery = adminOfRelation.getQuery();
        queries.add(memberQuery);
        queries.add(adminQuery);

        //This query will return to us all groups for which the user is either a member or an admin :)
        ParseQuery<Group> allGroupsQuery = ParseQuery.or(queries); //Combines our queries together

        return allGroupsQuery;
    }
}
