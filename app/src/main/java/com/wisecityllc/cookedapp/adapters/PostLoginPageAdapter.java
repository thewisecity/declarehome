package com.wisecityllc.cookedapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wisecityllc.cookedapp.fragments.EventsFragment;
import com.wisecityllc.cookedapp.fragments.GroupsFragment;
import com.wisecityllc.cookedapp.fragments.WellnessFragment;

/**
 * Created by dexterlohnes on 6/29/15.
 */
public class PostLoginPageAdapter extends FragmentPagerAdapter {
    private static final int GROUPS_FRAGMENT = 0;
    private static final int EVENTS_FRAGMENT = 1;
    private static final int WELLNESS_FRAGMENT = 2;

    private GroupsFragment mGroupsFragment;
    private EventsFragment mEventsFragment;

    public PostLoginPageAdapter (FragmentManager fm) {
        super(fm);
        mGroupsFragment = GroupsFragment.newInstance(GroupsAdapter.MEMBER_AND_ADMIN_ONLY);
        mEventsFragment = new EventsFragment();
    }

    public void notifyGroupsDataUpdated(){
        mGroupsFragment.notifyGroupsDataUpdated();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case GROUPS_FRAGMENT : return mGroupsFragment;
            case EVENTS_FRAGMENT : return mEventsFragment;
            case WELLNESS_FRAGMENT : return new WellnessFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case GROUPS_FRAGMENT : return "Groups";
            case EVENTS_FRAGMENT : return "Events";
            case WELLNESS_FRAGMENT : return "Wellness";
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}