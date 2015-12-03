package com.wisecityllc.cookedapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseObject;
import com.segment.analytics.Analytics;
import com.wisecityllc.cookedapp.fragments.GroupsFragment;
import com.wisecityllc.cookedapp.parseClasses.AlertCategory;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.GroupContract;
import com.wisecityllc.cookedapp.parseClasses.Message;
import com.wisecityllc.cookedapp.parseClasses.User;
import com.wisecityllc.cookedapp.parseClasses.WebAddress;
import com.wisecityllc.cookedapp.utilities.Stats;

import io.fabric.sdk.android.Fabric;

/**
 * Created by dexterlohnes on 4/25/15.
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
//        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(GroupContract.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(WebAddress.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(AlertCategory.class);
        Parse.initialize(this, "BrndBVrRczElKefgG3TvjCk3JYxtd5GB2GMzKoEP", "Xb7Pcc0lT2I3uJYNNoT6buaCuZ9dcvBMtCx9U5gw");

        Analytics analytics = new Analytics.Builder(this, getString(R.string.analytics_write_key)).build();
        Analytics.setSingletonInstance(analytics);

        // Safely call Analytics.with(context) from anywhere within your app!
        Stats.TrackApplicationStarted();

        GroupsFragment.sShouldIgnoreFirstAnalyticsCall = true;

    }

    public static void hideKeyboard(Activity act) {
        // Check if no view has focus:
        if(act != null) {
            View view = act.getCurrentFocus();
            if (view != null && instance != null) {
                InputMethodManager imm = (InputMethodManager) instance.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static Context getContext() {
        return instance;
    }
}
