package com.wisecityllc.cookedapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.wisecityllc.cookedapp.fragments.GroupsFragment;
import com.wisecityllc.cookedapp.utilities.Stats;

public class GroupsListActivity extends ActionBarActivity implements GroupsFragment.GroupsFragmentInteractionListener {

    public static final int REQUEST_CODE = 1475;

    public static final int JOINED_GROUP_RESULT_CODE = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // If not already added to the Fragment manager add it. If you don't do this a new Fragment will be added every time this method is called (Such as on orientation change)
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, GroupsFragment.newInstance(getIntent().getIntExtra("mode", 0))).commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // We just finished the CreateGroupActivity
        if(resultCode == GroupsListActivity.JOINED_GROUP_RESULT_CODE) {
            // Refresh groups
            setResult(GroupsListActivity.JOINED_GROUP_RESULT_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Stats.ScreenAllGroups();
    }
}