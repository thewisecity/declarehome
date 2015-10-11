package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.UserDetailsActivity;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.GroupContract;
import com.wisecityllc.cookedapp.parseClasses.User;
import com.wisecityllc.cookedapp.views.ClickableUserPortrait;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dexterlohnes on 9/15/15.
 */
public class GroupMemberListAdapter extends ParseQueryAdapter<User> {

    private Group mGroup;

    private static ParseQuery<User> sMembersQuery;
    private static ParseQuery<User> sAdminsQuery;
    private static ParseQuery<User> sInviteesQuery;
    private static ParseQuery<User> sRequestersQuery;

    public GroupMemberListAdapter(Context context,
                                  final Group membersOfGroup,
                                  final boolean includeMembers,
                                  final boolean includeAdmins,
                                  final boolean includePendingNeedsApproval,
                                  final boolean includePendingNeedsToAccept) {
        super(context, new ParseQueryAdapter.QueryFactory<User>() {
            public ParseQuery<User> create() {

                ParseQuery<User> assembledQuery = null;

                ArrayList<ParseQuery<User>> queryList = new ArrayList<ParseQuery<User>>();

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

    private static ParseQuery<User> getQueryForMembersOfGroup(Group group) {

        ParseQuery<User> membersQuery = new ParseQuery<>(User.class);
        membersQuery.whereEqualTo("memberOfArray", group);


        return membersQuery;
    }

    private static ParseQuery<User> getQueryForAdminsOfGroup(Group group) {

        ParseQuery<User> adminsQuery = new ParseQuery<>(User.class);
        adminsQuery.whereEqualTo("adminOfArray", group);

        return adminsQuery;
    }

    private static ParseQuery<User> getQueryForOutstandingInviteesOfGroup(Group group) {

        ParseQuery<GroupContract> contractsQuery = new ParseQuery<>(GroupContract.class);
        contractsQuery.whereEqualTo(GroupContract._GROUP, group);
        contractsQuery.whereEqualTo(GroupContract._STATUS, GroupContract.STATUS_USER_INVITED);

        ParseQuery<User> users = new ParseQuery<>(User.class);
        users.whereMatchesKeyInQuery("email", "inviteeEmail", contractsQuery);

        return users;

    }

    private static ParseQuery<User> getQueryForOutstandingRequestersOfGroup(Group group) {

        ParseQuery<GroupContract> contractsQuery = new ParseQuery<>(GroupContract.class);
        contractsQuery.whereEqualTo(GroupContract._GROUP, group);
        contractsQuery.whereEqualTo(GroupContract._STATUS, GroupContract.STATUS_USER_REQUESTED);

        ParseQuery<User> users = new ParseQuery<>(User.class);
        users.whereMatchesKeyInQuery("email", "inviteeEmail", contractsQuery);

        return users;

    }



    @Override
    public View getItemView(final User user, View v, ViewGroup parent) {

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
                                    Toast.makeText(getContext(), "Error while approving. Try again.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    Log.e("Error", e.getLocalizedMessage());

                                } else { // Success!

                                    Toast.makeText(getContext(), "Approved", Toast.LENGTH_SHORT).show();
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

        ClickableUserPortrait userImage = (ClickableUserPortrait) v.findViewById(R.id.group_member_list_member_photo);
        userImage.setUser(user, true);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.startUserDetailsActivity((AppCompatActivity) getContext(), user);
            }
        });

        super.getItemView(user, v, parent);


        return v;
    }

}
