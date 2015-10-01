package com.wisecityllc.cookedapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.views.GroupMemberListView;

import java.util.HashMap;

public class GroupDetailsActivity extends ActionBarActivity {

    private static String GROUP_DETAILS_CATEGORY = "GroupDetailsCatergory";
    private static String GROUP_DETAILS_SCREEN = "GroupDetailsScreen";
    private static String GROUP_ID_EXTRA = "GroupId";

    public final static int REQUEST_CODE = 10485;

    private final int USER_IS_ADMIN = 1;
    private final int USER_IS_MEMBER = 2;
    private final int USER_HAS_BEEN_INVITED = 3;
    private final int USER_HAS_ALREADY_REQUESTED_TO_JOIN = 4;
    private final int USER_HAS_NO_ASSOCIATION = 5;


    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        getSupportActionBar().hide();

        Intent intent = getIntent();

        TextView tvName = (TextView) findViewById(R.id.group_details_activity_group_name);
        tvName.setText(intent.getStringExtra("name"));

        TextView tvPurpose = (TextView) findViewById(R.id.group_details_activity_group_purpose);
        tvPurpose.setText(intent.getStringExtra("purpose"));

        TextView tvNeighberhoods = (TextView) findViewById(R.id.group_details_activity_group_neighberhoods);
        tvNeighberhoods.setText(intent.getStringExtra("neighberhoods"));

        TextView tvCity = (TextView) findViewById(R.id.group_details_activity_group_city);
        tvCity.setText("City: " + intent.getStringExtra("city") + ", " + intent.getStringExtra("state"));

        mButton = (Button) findViewById(R.id.group_details_activity_button);
        mButton.setVisibility(View.GONE);

        GroupMemberListView membersLV = (GroupMemberListView) findViewById(R.id.group_details_activity_member_list_view);
//        Group group = ParseObject.createWithoutData(Group.class, getIntent().getStringExtra("id"));
        ParseQuery<Group> q = new ParseQuery<Group>(Group.class);
//        q.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
//        q.fromLocalDatastore();
        Group group = null;
        try {
            group = q.get(getIntent().getStringExtra("id"));
        } catch (ParseException e){
            e.printStackTrace();
        }
        membersLV.setGroup(group);

        setupUIForCurrentUserStatusWithinGroup();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setIsLoadingMemberStatus(){
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.group_details_activity_loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        mButton.setVisibility(View.GONE);
        mButton.setOnClickListener(null);
    }

    private void setupUIForCurrentUserStatusWithinGroup() {

        setIsLoadingMemberStatus();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("group", getIntent().getStringExtra("id"));
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_get_user_status_within_group),
                params,
                new FunctionCallback<Number>() {
                    @Override
                    public void done(Number number, ParseException e) {

                        if (e != null) {
                            Log.e("Error", e.getLocalizedMessage());

                        } else {


                            int response = 9999;
                            if (number != null)
                                response = number.intValue();

                            updateUIForUserStatusResponse(response);
                        }
                    }
                });
    }


    private void updateUIForUserStatusResponse(final int response){
        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.group_details_activity_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if(response==USER_IS_MEMBER)

        {
            mButton.setVisibility(View.GONE);
            //TODO: Find out what we want to do when user is member
//                                                        mButton.setText(getString(R.string.invite_members));
        }

        else if(response==USER_IS_ADMIN)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText(getString(R.string.invite_members));
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInviteMembersActivity();
                }
            });
        }

        else if(response==USER_HAS_BEEN_INVITED)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Accept Invitation");
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptInvitation();
                    //Update our button
                }
            });
        }

        else if(response==USER_HAS_NO_ASSOCIATION)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Request To Join");
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestMembership();
                    //Update our button
                }
            });
        }

        else if(response==USER_HAS_ALREADY_REQUESTED_TO_JOIN)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setEnabled(false);
            mButton.setText("Membership requested");
        }

        else

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Error");
        }
    }

    private void acceptInvitation() {
        setIsLoadingMemberStatus();
        //Send a request to accept membership
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", getIntent().getStringExtra("id"));
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_accept_membership_to_group),
                params,
                new FunctionCallback<Number>() {
                    @Override
                    public void done(Number number, ParseException e) {

                        if (e != null) { // Failure!
                            Log.e("Error", e.getLocalizedMessage());
                            updateUIForUserStatusResponse(9999);
                            setResult(0);

                        } else { // Success!

                            int response = 9999;
                            if (number != null)
                                response = number.intValue();

                            //Update our user
                            ParseUser.getCurrentUser().fetchInBackground();

                            updateUIForUserStatusResponse(response);


                        }
                    }
                });
        // Hopeful
        setResult(GroupsListActivity.JOINED_GROUP_RESULT_CODE);
    }

    private void requestMembership() {
        setIsLoadingMemberStatus();
        //Send a request to join the group
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", getIntent().getStringExtra("id"));
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_request_membership_to_group),
                params,
                new FunctionCallback<Number>() {
                    @Override
                    public void done(Number number, ParseException e) {

                        if (e != null) { // Failure!
                            Log.e("Error", e.getLocalizedMessage());

                        } else { // Success!

                            int response = 9999;
                            if (number != null)
                                response = number.intValue();

                            updateUIForUserStatusResponse(response);

                        }
                    }
                });
    }

    private void openInviteMembersActivity() {
        Intent openActivity = new Intent(this, InviteMembersActivity.class);
        openActivity.putExtra("groupId", getIntent().getStringExtra("id"));
        openActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(openActivity);
    }

    public static void startGroupDetailActivity(Context context, Group group){
        Intent viewGroupDetailsIntent = new Intent(context, GroupDetailsActivity.class);
        viewGroupDetailsIntent.putExtra("id", group.getObjectId());
        viewGroupDetailsIntent.putExtra("groupHashId", group.getGroupHashId());
        viewGroupDetailsIntent.putExtra("name", group.getName());
        viewGroupDetailsIntent.putExtra("purpose", group.getPurpose());
        viewGroupDetailsIntent.putExtra("neighberhoods", group.getNeighberhoods());
        viewGroupDetailsIntent.putExtra("city", group.getCity());
        viewGroupDetailsIntent.putExtra("state", group.getState());
        viewGroupDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ((AppCompatActivity) context).startActivityForResult(viewGroupDetailsIntent, GroupDetailsActivity.REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Analytics.with(this).screen(GROUP_DETAILS_CATEGORY, GROUP_DETAILS_SCREEN, new Properties().putValue(GROUP_ID_EXTRA, getIntent().getStringExtra("id")));
    }
}
