package com.wisecityllc.cookedapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.wisecityllc.cookedapp.parseClasses.Group;

/**
 * Created by dexterlohnes on 4/25/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Group.class);
        Parse.initialize(this, "BrndBVrRczElKefgG3TvjCk3JYxtd5GB2GMzKoEP", "Xb7Pcc0lT2I3uJYNNoT6buaCuZ9dcvBMtCx9U5gw");
    }
}
