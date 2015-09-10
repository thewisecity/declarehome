package com.wisecityllc.cookedapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.Message;

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
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this, "BrndBVrRczElKefgG3TvjCk3JYxtd5GB2GMzKoEP", "Xb7Pcc0lT2I3uJYNNoT6buaCuZ9dcvBMtCx9U5gw");
        ParseInstallation.getCurrentInstallation().saveInBackground();

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
