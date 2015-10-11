package com.wisecityllc.cookedapp.utilities;

import com.wisecityllc.cookedapp.parseClasses.User;

/**
 * Created by dexterlohnes on 4/21/15.
 */
public class Validation {
    public static boolean validateEmail(String email){
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean validatePassword(String password){
        //TODO: implement
        return true;
    }

    public static boolean validateDisplayName(String displayName){
        if(displayName.trim().length() < User._MINIMUM_NAME_LENGTH)
            return false;
        else
            return true;
    }

    public static boolean validatePhoneNumber(String phoneNumber){
        if(phoneNumber.trim().length() < User._MINIMUM_PHONE_NUMBER_LENGTH)
            return false;
        else
            return true;
    }
}
