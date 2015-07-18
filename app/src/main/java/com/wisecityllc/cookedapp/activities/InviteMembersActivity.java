package com.wisecityllc.cookedapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.wisecityllc.cookedapp.R;

import java.util.HashMap;

public class InviteMembersActivity extends ActionBarActivity {

    private LinearLayout mInviteFieldsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);
        getSupportActionBar().hide();

        mInviteFieldsHolder = (LinearLayout)findViewById(R.id.invite_members_invite_text_fields_holder);
    }

    public void sendInvites(View v){
        String invites = "";
        int childCount = mInviteFieldsHolder.getChildCount();
        for(int i = 0; i < childCount; i++){
            EditText inviteField = (EditText)mInviteFieldsHolder.getChildAt(i);
            invites += " " + inviteField.getText().toString();
        }
        Toast.makeText(this, invites, Toast.LENGTH_SHORT).show();


        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", getIntent().getStringExtra("groupId"));
//        Log.d("DEX", "GroupHashId: " + getIntent().getStringExtra("groupHashId"));
        params.put("inviteeEmail", ((EditText)mInviteFieldsHolder.getChildAt(0)).getText().toString());
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_send_invite_to_individual_user),
                params,
                new FunctionCallback<String>() {
                    @Override
                    public void done(String string, ParseException e) {

                        if (e != null) { // Failure!
                            Log.e("Error", e.getLocalizedMessage());

                        } else { // Success!

                            Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

////    private void setupUIForCurrentUserStatusWithinGroup() {
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("group", getIntent().getStringExtra("id"));
//        Log.d("DEX", "GroupHashId: " + getIntent().getStringExtra("groupHashId"));
//        params.put("groupHashId", getIntent().getStringExtra("groupHashId"));
//        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_get_user_status_within_group),
//                params,
//                new FunctionCallback<Number>() {
//                    @Override
//                    public void done(Number number, ParseException e) {
//
//                        if(e != null){
//                            Log.e("Error", e.getLocalizedMessage());
//
//                        }else {
//
//                            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.group_details_activity_loading_indicator);
//                            loadingIndicator.setVisibility(View.GONE);
//
//                            int response = 9999;
//                            if (number != null)
//                                response = number.intValue();
//
//                            updateUIForUserStatusResponse(response);
//                        }
//                    }
//                });
//    }


}
