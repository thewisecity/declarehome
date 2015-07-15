package com.wisecityllc.cookedapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.wisecityllc.cookedapp.R;

import java.util.HashMap;

public class GroupDetailsActivity extends ActionBarActivity {

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

        setupUIForCurrentUserStatusWithinGroup();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void setupUIForCurrentUserStatusWithinGroup() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("group", getIntent().getStringExtra("id"));
        Log.d("DEX", "GroupHashId: " + getIntent().getStringExtra("groupHashId"));
        params.put("groupHashId", getIntent().getStringExtra("groupHashId"));
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_get_user_status_within_group),
                params,
                new FunctionCallback<Number>() {
                    @Override
                    public void done(Number number, ParseException e) {

                        if(e != null){
                            Log.e("Error", e.getLocalizedMessage());

                        }else {

                            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.group_details_activity_loading_indicator);
                            loadingIndicator.setVisibility(View.GONE);

                            int response = 9999;
                            if (number != null)
                                response = number.intValue();

                            updateUIForUserStatusResponse(response);
                        }
                    }
                });
    }


    private void updateUIForUserStatusResponse(int response){
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
        }

        else if(response==USER_HAS_BEEN_INVITED)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Respond To Invitation");
        }

        else if(response==USER_HAS_NO_ASSOCIATION)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Request To Join");
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Send a request to join the group

                    //Update our button
                }
            });
        }

        else if(response==USER_HAS_ALREADY_REQUESTED_TO_JOIN)

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setEnabled(false);
            mButton.setText("Awaiting Response");
        }

        else

        {
            mButton.setVisibility(View.VISIBLE);
            mButton.setText("Not sure what happened");
        }
    }

}
