package com.wisecityllc.cookedapp.utilities;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by dexterlohnes on 7/22/15.
 */
public class Installation {

    public static void associateCurrentUserWithCurrentInstallation(){
        ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        currentInstallation.put("user", ParseUser.getCurrentUser());
        currentInstallation.saveInBackground();
    }

    public static void unassociateCurrentUserFromCurrentInstallation(){
        ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        currentInstallation.remove("user");
        currentInstallation.saveInBackground();
    }


}
