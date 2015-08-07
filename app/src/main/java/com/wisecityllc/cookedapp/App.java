package com.wisecityllc.cookedapp;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.wisecityllc.cookedapp.parseClasses.Group;
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
        Parse.initialize(this, "BrndBVrRczElKefgG3TvjCk3JYxtd5GB2GMzKoEP", "Xb7Pcc0lT2I3uJYNNoT6buaCuZ9dcvBMtCx9U5gw");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static Context getContext() {
        return instance;
    }
}
