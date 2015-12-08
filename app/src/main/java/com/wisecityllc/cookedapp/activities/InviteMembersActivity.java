package com.wisecityllc.cookedapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;

import java.util.HashMap;

public class InviteMembersActivity extends ActionBarActivity {

    private LinearLayout mInviteFieldsHolder;
    private TextView mGroupName;
    private FrameLayout mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);
        getSupportActionBar().hide();

        mInviteFieldsHolder = (LinearLayout)findViewById(R.id.invite_members_invite_text_fields_holder);
        mGroupName = (TextView)findViewById(R.id.invite_members_group_name);
        mLoadingView = (FrameLayout)findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.GONE);

        mGroupName.setText(getIntent().getStringExtra("groupName"));
    }

    public void sendInvites(View v){
        String invites = "";
        int childCount = mInviteFieldsHolder.getChildCount();
        for(int i = 0; i < childCount; i++){
            EditText inviteField = (EditText)mInviteFieldsHolder.getChildAt(i);
            invites += " " + inviteField.getText().toString();
        }
        Toast.makeText(this, "Sending Invite...", Toast.LENGTH_SHORT).show();


        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("groupId", getIntent().getStringExtra("groupId"));
        params.put("inviteeEmail", ((EditText) mInviteFieldsHolder.getChildAt(0)).getText().toString());
        mLoadingView.setVisibility(View.VISIBLE);
        App.hideKeyboard(this);
        ParseCloud.callFunctionInBackground(getString(R.string.cloud_code_send_invite_to_individual_user),
                params,
                new FunctionCallback<String>() {
                    @Override
                    public void done(String string, ParseException e) {
                        mLoadingView.setVisibility(View.GONE);
                        int childCount = mInviteFieldsHolder.getChildCount();


                        if (e != null) { // Failure!
                            Log.e("Error", e.getLocalizedMessage());

                        } else { // Success!

                            Toast.makeText(getApplicationContext(), "Invitation Sent", Toast.LENGTH_SHORT).show();
                            // Get rid of all the text in all invite fields (currently only 1 but built for extensability)
                            for(int i = 0; i < childCount; i++){
                                EditText inviteField = (EditText)mInviteFieldsHolder.getChildAt(i);
                                inviteField.setText("");
                            }
                        }
                    }
                });
    }
}
