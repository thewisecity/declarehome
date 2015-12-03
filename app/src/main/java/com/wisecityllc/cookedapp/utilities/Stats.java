package com.wisecityllc.cookedapp.utilities;

import com.parse.ParseUser;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.Traits;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.fragments.EventsFragment;
import com.wisecityllc.cookedapp.fragments.GroupsFragment;

/**
 * Created by dexterlohnes on 11/30/15.
 */
public class Stats {

    private static String ALERTS_SCREEN = "AlertsScreen";
    private static String MESSAGE_WALL_CATEGORY = "MessageWallCategory";
    private static String MESSAGE_WALL_SCREEN = "MessageWallScreen";
    private static String CREATE_GROUP_SCREEN = "CreateGroupScreen";
    private static String GROUP_DETAILS_CATEGORY = "GroupDetailsCategory";
    private static String GROUP_DETAILS_SCREEN = "GroupDetailsScreen";
    private static String LOGIN_SCREEN = "LoginScreen";
    private static String REGISTRATION_SREEN = "RegistrationScreen";

    public static void TrackBeganMessageCreation()
    {
        Analytics.with(App.getContext()).track("Began Message Creation");
    }

    public static void TrackEndedMessageCreation()
    {
        Analytics.with(App.getContext()).track("Ended Message Creation");
    }

    public static void TrackOpenedNewMessageMenu()
    {
        Analytics.with(App.getContext()).track("Opened New Message Menu");
    }

    public static void TrackClosedNewMessageMenu()
    {
        Analytics.with(App.getContext()).track("Closed New Message Menu");
    }

    public static void TrackBeganAlertCreation()
    {
        Analytics.with(App.getContext()).track("Began Alert Creation");
    }

    public static void TrackBeganAlertComposition()
    {
        Analytics.with(App.getContext()).track("Began Alert Composition");
    }

    public static void TrackAttemptingGroupCreation()
    {
        Analytics.with(App.getContext()).track("Attempting Group Creation");
    }

    public static void TrackGroupCreationCancelled()
    {
        Analytics.with(App.getContext()).track("Group Creation Cancelled");
    }

    public static void TrackApplicationStarted()
    {
        Analytics.with(App.getContext()).track("Application Started");
    }

    public static void ScreenCreateGroup()
    {
        Analytics.with(App.getContext()).screen(null, CREATE_GROUP_SCREEN);
    }

    public static void ScreenGroupDetails(Properties props)
    {
        Analytics.with(App.getContext()).screen(GROUP_DETAILS_CATEGORY, GROUP_DETAILS_SCREEN, props);
    }

    public static void ScreenAllGroups()
    {
        Analytics.with(App.getContext()).screen(null, GroupsFragment.ALL_GROUPS_SCREEN);
    }

    public static void ScreenMessageWall(Properties props)
    {
        Analytics.with(App.getContext()).screen(MESSAGE_WALL_CATEGORY, MESSAGE_WALL_SCREEN, props);
    }

    public static void ScreenMyGroups()
    {
        Analytics.with(App.getContext()).screen(null, GroupsFragment.MY_GROUPS_SCREEN);
    }

    public static void ScreenEvents()
    {
        Analytics.with(App.getContext()).screen(null, EventsFragment.EVENTS_SCREEN);
    }

    public static void TrackRegistrationSuccess()
    {
        Analytics.with(App.getContext()).track("Registration Success");
    }

    public static void TrackRegistrationFailed()
    {
        Analytics.with(App.getContext()).track("Registration Failed");
    }

    public static void TrackRegistrationAttempt(){
        Analytics.with(App.getContext()).track("Registration Attempt");
    }

    public static void TrackInvalidRegistrationInfo(){
        Analytics.with(App.getContext()).track("Invalid Registration Info");
    }

    public static void TrackLoginSuccess()
    {
        Analytics.with(App.getContext()).track("Login Success");
    }

    public static void TrackLoginFailed()
    {
        Analytics.with(App.getContext()).track("Login Failed");
    }

    public static void ScreenLogin()
    {
        Analytics.with(App.getContext()).screen(null, LOGIN_SCREEN);
    }

    public static void ScreenRegistration()
    {
        Analytics.with(App.getContext()).screen(null, REGISTRATION_SREEN);
    }

    public static void AliasAndIdentifyUser()
    {
        Analytics.with(App.getContext()).alias(ParseUser.getCurrentUser().getObjectId(), null);
        Analytics.with(App.getContext()).identify(ParseUser.getCurrentUser().getObjectId(), new Traits().putName(ParseUser.getCurrentUser().getString("displayName")).putEmail(ParseUser.getCurrentUser().getEmail()), null);
    }

    public static void ScreenAlerts()
    {
        Analytics.with(App.getContext()).screen(null, ALERTS_SCREEN);
    }

    public static void TrackLoginAttempt()
    {
        Analytics.with(App.getContext()).track("Login Attempt");
    }

    public static void TrackGroupCreationFailed()
    {
        Analytics.with(App.getContext()).track("Group Creation Failed");
    }

    public static void TrackGroupCreated(Properties props)
    {
        Analytics.with(App.getContext()).track("Group Created", props);
    }

    public static void TrackAlertCreationFailed()
    {
        Analytics.with(App.getContext()).track("Alert Creation Failed");
    }
    public static void TrackMessageCreationFailed()
    {
        Analytics.with(App.getContext()).track("Message Creation Failed");
    }

    public static void TrackMessageCreated()
    {
        Analytics.with(App.getContext()).track("Message Created");
    }

    public static void TrackAlertCreated()
    {
        Analytics.with(App.getContext()).track("Alert Created");
    }

    public static void TrackLogout()
    {
        Analytics.with(App.getContext()).track("User Logged Out");
    }
}
