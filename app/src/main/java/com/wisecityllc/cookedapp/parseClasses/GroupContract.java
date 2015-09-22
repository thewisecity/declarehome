package com.wisecityllc.cookedapp.parseClasses;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by dexterlohnes on 8/14/15.
 */
@ParseClassName("GroupContract")
public class GroupContract extends ParseObject {

    //Fields
    public final static String _INVITEE_EMAIL = "inviteeEmail";
    public final static String _INVITEE = "invitee";
    public final static String _INVITED_BY = "invitedBy";
    public final static String _GROUP = "group";
    public final static String _STATUS = "status";

    //String consts for Status
    public final static String STATUS_USER_REQUESTED = "UserRequested";
    public final static String STATUS_USER_INVITED = "UserInvited";
    public final static String STATUS_SIGNED = "Signed";

    public GroupContract() {
        // A default constructor is required.
    }

    public String getInviteeEmail() {
        return getString(_INVITEE_EMAIL);
    }

    public void setInviteeEmail(String inviteeEmail) {
        put(_INVITEE_EMAIL, inviteeEmail);
    }

    public ParseUser getInvitee() {
        return getParseUser(_INVITEE);
    }

    public void setInvitee(ParseUser invitee) {
        put(_INVITEE, invitee);
    }

    public ParseUser getInvitedBy() {
        return getParseUser(_INVITED_BY);
    }

    public void setInvitedBy(ParseUser invitedBy) {
        put(_INVITED_BY, invitedBy);
    }

    public Group getGroup() {
        return (Group)getParseObject(_GROUP);
    }

    public void setGroup(Group group) {
        put(_GROUP, group);
    }

    public String getStatus() {
        return getString(_STATUS);
    }

    public void setStatus(String status) {
        put(_STATUS, status);
    }
}
