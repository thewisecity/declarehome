package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.GroupContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dexterlohnes on 9/15/15.
 */
public class GroupMemberListAdapter extends ParseQueryAdapter<ParseUser> {

    private Group mGroup;

    private static ParseQuery<ParseUser> sMembersQuery;
    private static ParseQuery<ParseUser> sAdminsQuery;
    private static ParseQuery<ParseUser> sInviteesQuery;
    private static ParseQuery<ParseUser> sRequestersQuery;

    public GroupMemberListAdapter(Context context,
                                  final Group membersOfGroup,
                                  final boolean includeMembers,
                                  final boolean includeAdmins,
                                  final boolean includePendingNeedsApproval,
                                  final boolean includePendingNeedsToAccept) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
            public ParseQuery<ParseUser> create() {

                ParseQuery<ParseUser> assembledQuery = null;

                ArrayList<ParseQuery<ParseUser>> queryList = new ArrayList<ParseQuery<ParseUser>>();

                if (includeMembers) {
                    sMembersQuery = getQueryForMembersOfGroup(membersOfGroup);
                    queryList.add(sMembersQuery);
                }

                if(includeAdmins) {
                    sAdminsQuery = getQueryForAdminsOfGroup(membersOfGroup);
                    queryList.add(sAdminsQuery);
                }

                if(includePendingNeedsApproval) {
                    sRequestersQuery = getQueryForOutstandingRequestersOfGroup(membersOfGroup);
                    queryList.add(sRequestersQuery);
                }

                if(includePendingNeedsToAccept) {
                    sInviteesQuery = getQueryForOutstandingInviteesOfGroup(membersOfGroup);
                    queryList.add(sInviteesQuery);
                }

                if(queryList.size() > 0)
                    assembledQuery = ParseQuery.or(queryList);

                return assembledQuery;
            }
        });

        mGroup = membersOfGroup;

    }

    private static ParseQuery<ParseUser> getQueryForMembersOfGroup(Group group) {
//        ParseRole membersRole = group.getMembersRole();
//        try {
//            membersRole.fetchIfNeeded();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        ParseRelation<ParseUser> memberRelation = membersRole.getUsers();
//        ParseQuery<ParseUser> membersQuery = (ParseQuery<ParseUser>) memberRelation.getQuery();

        ParseQuery<ParseUser> membersQuery = new ParseQuery<ParseUser>(ParseUser.class);
        membersQuery.whereEqualTo("memberOfArray", group);


        return membersQuery;
    }

    private static ParseQuery<ParseUser> getQueryForAdminsOfGroup(Group group) {
//        ParseRole adminsRole = group.getAdminsRole();
//        try {
//            adminsRole.fetchIfNeeded();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        ParseRelation<ParseUser> adminRelation = adminsRole.getUsers();
//        ParseQuery<ParseUser> adminsQuery = (ParseQuery<ParseUser>) adminRelation.getQuery();

        ParseQuery<ParseUser> adminsQuery = new ParseQuery<ParseUser>(ParseUser.class);
        adminsQuery.whereEqualTo("adminOfArray", group);

        return adminsQuery;
    }

    private static ParseQuery<ParseUser> getQueryForOutstandingInviteesOfGroup(Group group) {

        ParseQuery<GroupContract> contractsQuery = new ParseQuery<>(GroupContract.class);
        contractsQuery.whereEqualTo(GroupContract._GROUP, group);
        contractsQuery.whereEqualTo(GroupContract._STATUS, GroupContract.STATUS_USER_INVITED);

        ParseQuery<ParseUser> users = ParseUser.getQuery();
        users.whereMatchesKeyInQuery("email", "inviteeEmail", contractsQuery);

        return users;

    }

    private static ParseQuery<ParseUser> getQueryForOutstandingRequestersOfGroup(Group group) {

        ParseQuery<GroupContract> contractsQuery = new ParseQuery<>(GroupContract.class);
        contractsQuery.whereEqualTo(GroupContract._GROUP, group);
        contractsQuery.whereEqualTo(GroupContract._STATUS, GroupContract.STATUS_USER_REQUESTED);

        ParseQuery<ParseUser> users = ParseUser.getQuery();
        users.whereMatchesKeyInQuery("email", "inviteeEmail", contractsQuery);

        return users;

    }



    @Override
    public View getItemView(final ParseUser user, View v, ViewGroup parent) {

        boolean currentUserIsAdmin = mGroup.isCurrentUserAdmin();
        // If user isn't admin or member, they must be invitee
        boolean userIsInvitee = (mGroup.isUserMember(user, false) == false && mGroup.isUserAdmin(user, false) == false);

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_group_members, null);
        }

        final ProgressBar loadingIndicator = (ProgressBar) v.findViewById(R.id.group_member_list_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        final Button approveButton = (Button) v.findViewById(R.id.group_member_list_approve_button);

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("groupId", mGroup.getObjectId());
                params.put("inviteeId", user.getObjectId());
                loadingIndicator.setVisibility(View.VISIBLE);
                approveButton.setVisibility(View.GONE);
                ParseCloud.callFunctionInBackground(getContext().getString(R.string.cloud_code_approve_membership_for_group),
                        params,
                        new FunctionCallback<String>() {
                            @Override
                            public void done(String string, ParseException e) {

                                if (e != null) { // Failure!
                                    Toast.makeText(App.getContext(), "Error while approving. Try again.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    Log.e("Error", e.getLocalizedMessage());

                                } else { // Success!

                                    Toast.makeText(App.getContext(), "Approved", Toast.LENGTH_SHORT).show();
                                    mGroup.addMember(user);
                                    notifyDataSetChanged();
//                                    loadObjects();

                                }
                            }
                        });



            }
        });

        //If the current user is an admin of this group and the user for this view is an invitee, show the 'Approve' button, else hide it
        approveButton.setVisibility((userIsInvitee && currentUserIsAdmin) ? View.VISIBLE : View.GONE);

        TextView nameTV = (TextView) v.findViewById(R.id.group_member_list_member_name);
        //If the current user is an invitee, mark them as '(pending)'
        nameTV.setText(user.getString("displayName") + (userIsInvitee ? " (pending)" : ""));

        ParseImageView userImage = (ParseImageView) v.findViewById(R.id.group_member_list_member_photo);
        userImage.setParseFile(user.getParseFile("profilePic"));
        userImage.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e != null) {
                    Log.e("DEX", e.getMessage());
                }
            }
        });

        super.getItemView(user, v, parent);


        return v;
    }

}
