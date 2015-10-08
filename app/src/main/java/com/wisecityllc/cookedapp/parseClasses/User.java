package com.wisecityllc.cookedapp.parseClasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 10/8/15.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    final static String _DISPLAY_NAME = "displayName";
    final static String _PROFILE_PIC = "profilePic";
    final static String _ADMIN_OF_ARRAY = "adminOfArray";
    final static String _MEMBER_OF_ARRAY = "memberOfArray";
    final static String _PHONE_NUMBER = "phoneNumber";
    final static String _LINK_ONE = "linkOne";
    final static String _LINK_TWO = "linkTwo";
    final static String _LINK_THREE = "linkThree";
    final static String _DESCRIPTION = "description";

    public User() {
        // A default constructor is required.
    }

    public String getDisplayName() {
        return getString(_DISPLAY_NAME);
    }

    public String getDescription() {
        return getString(_DESCRIPTION);
    }

    public String getPhoneNumber() {
        return getString(_PHONE_NUMBER);
    }

    public String getLinkOne() {
        return getString(_LINK_ONE);
    }

    public String getLinkTwo() {
        return getString(_LINK_TWO);
    }

    public String getLinkThree() {
        return getString(_LINK_THREE);
    }

    public ParseFile getProfilePic() {
        return getParseFile(_PROFILE_PIC);
    }

    public void setDisplayName(String displayName) {
        put(_DISPLAY_NAME, displayName);
    }

    public void setDescription(String description) {
        put(_DESCRIPTION, description);
    }

    public void setPhoneNumber(String number) {
        put(_PHONE_NUMBER, number);
    }

    public void setLinkOne(String linkOne) {
        put(_LINK_ONE, linkOne);
    }

    public void setLinkTwo(String linkTwo) {
        put(_LINK_TWO, linkTwo);
    }

    public void setLinkThree(String linkThree) {
        put(_LINK_THREE, linkThree);
    }
    public void setProfilePic(ParseFile profPic) {
        put(_PROFILE_PIC, profPic);
    }

    public ArrayList<Group> getAdminGroups() {

        List<Group> groups = getList(_ADMIN_OF_ARRAY);
        ArrayList<Group> adminGroups = new ArrayList<>(groups);
        return adminGroups;
    }

    public ArrayList<Group> getMemberGroups() {
        List<Group> groups = getList(_MEMBER_OF_ARRAY);
        ArrayList<Group> memberGroups = new ArrayList<>(groups);
        return memberGroups;    }
}
