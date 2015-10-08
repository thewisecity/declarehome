package com.wisecityllc.cookedapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.common.view.SlidingTabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.segment.analytics.Analytics;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.GroupsAdapter;
import com.wisecityllc.cookedapp.adapters.PostLoginPageAdapter;
import com.wisecityllc.cookedapp.fragments.EventsFragment;
import com.wisecityllc.cookedapp.fragments.GroupsFragment;
import com.wisecityllc.cookedapp.fragments.NavigationDrawerFragment;
import com.wisecityllc.cookedapp.fragments.WellnessFragment;
import com.wisecityllc.cookedapp.utilities.Notifications;


public class PostLoginActivity extends ActionBarActivity
        implements  NavigationDrawerFragment.NavigationDrawerCallbacks,
        GroupsFragment.GroupsFragmentInteractionListener,
        WellnessFragment.WellnessFragmentInteractionListener,
        EventsFragment.EventsFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Adapter which controls the Groups and Events fragment
     */
    private PostLoginPageAdapter mPageAdapter;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    private BroadcastReceiver mParseObjectUpdatesReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);


        //Initialize broadcast receiver
        mParseObjectUpdatesReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //"Group was created" intent received
                if(intent.getAction().equalsIgnoreCase(getString(R.string.broadcast_group_saved_success))){
                    mPageAdapter.notifyGroupsDataUpdated();
                }
            }
        };
        IntentFilter parseObjectUpdates = new IntentFilter();
        parseObjectUpdates.addAction(getString(R.string.broadcast_group_saved_success));

        LocalBroadcastManager.getInstance(this).registerReceiver(mParseObjectUpdatesReceiver, parseObjectUpdates);

//        if(ParseUser.getCurrentUser() != null){
//            //Update our current user
//            Group.refreshLocalGroupPermissions();
//        }

        //DEBUG: Associate ourselves with all notifs
        Notifications.setSubscriptionForAllNotifs(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mNavigationDrawerFragment == null)
            mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        if(mViewPager == null) {
            mViewPager = (ViewPager) findViewById(R.id.post_login_pager);
            mPageAdapter = new PostLoginPageAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mPageAdapter);
        }

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        if(mSlidingTabLayout == null) {
            mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
            mSlidingTabLayout.setViewPager(mViewPager);
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mParseObjectUpdatesReceiver);
        super.onDestroy();

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (!mNavigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        if (ParseUser.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.menu_post_login, menu);
            restoreActionBar();
            return true;
        }else{
            return false;
        }
        //}
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            attemptLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void attemptLogout() {
        if(ParseUser.getCurrentUser() != null){
            try {
                ParseUser.getCurrentUser().unpin();
            } catch (ParseException err) {
                err.printStackTrace();
            }
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! We've logged out
                        switchToLoginActivity();

                    } else {
                        // Logout didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(getApplicationContext(), "Logout Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            switchToLoginActivity();
        }
    }


    private void switchToLoginActivity() {
        Intent loginActivityIntent = new Intent(this, PreLoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
    }

    //================================================================================
    //region Navigation Drawer Methods
    //================================================================================

    @Override
    public void onNavigationDrawerFinishedLoading() {
        NavigationDrawerFragment drawerFrag = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFrag.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(position == NavigationDrawerFragment.NAV_DRAWER_CREATE_GROUP_POSITION){
            startCreateNewGroupActivity();
        }else if (position == NavigationDrawerFragment.NAV_DRAWER_VIEW_ALL_GROUPS_POSITION){
            startViewAllGroupsListActivity(GroupsAdapter.ALL_GROUPS);
        }else if (position == NavigationDrawerFragment.NAV_DRAWER_VIEW_EDIT_MY_PROFILE_POSITION) {
            startViewEditMyProfileActivity();
        }

    }

    private void startViewAllGroupsListActivity(int mode) {
        Intent allGroupsActivityIntent = new Intent(this, GroupsListActivity.class);
        allGroupsActivityIntent.putExtra("mode", mode);
        startActivityForResult(allGroupsActivityIntent, GroupsListActivity.REQUEST_CODE);
    }

    private void startViewEditMyProfileActivity() {
        Intent editProfileActivityIntent = new Intent(this, EditUserDetailsActivity.class);
        editProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(editProfileActivityIntent);
    }

    private void startCreateNewGroupActivity(){
        Intent startNewGroupIntent = new Intent(this, CreateGroupActivity.class);
        startNewGroupIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(startNewGroupIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // We just finished the CreateGroupActivity
        if(requestCode == GroupsListActivity.REQUEST_CODE) {
            // Check if we want to update groups
            if(resultCode == GroupsListActivity.JOINED_GROUP_RESULT_CODE) {
                // Refresh groups
                mPageAdapter.notifyGroupsDataUpdated();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        postCurrentScreenToAnalytics();
    }

    private String postCurrentScreenToAnalytics() {
        int currentItem = mViewPager.getCurrentItem();
        String currentScreen = "";
        if(currentItem == 0) {
            // We are on the Groups fragment
            Analytics.with(this).screen(null, GroupsFragment.MY_GROUPS_SCREEN);
        } else if (currentItem == 1) {
            // We are on Events fragment
            Analytics.with(this).screen(null, EventsFragment.EVENTS_SCREEN);
        }
        return currentScreen;
    }

    //endregion


}
