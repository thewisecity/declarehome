package com.wisecityllc.cookedapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wisecityllc.cookedapp.fragments.GroupsFragment;

public class GroupsListActivity extends FragmentActivity implements GroupsFragment.GroupsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // If not already added to the Fragment manager add it. If you don't do this a new Fragment will be added every time this method is called (Such as on orientation change)
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, GroupsFragment.newInstance(getIntent().getIntExtra("mode", 0))).commit();
    }

}