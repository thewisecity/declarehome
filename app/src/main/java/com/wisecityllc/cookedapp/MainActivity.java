package com.wisecityllc.cookedapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.fragments.LoadingScreenFragment;
import com.wisecityllc.cookedapp.fragments.LoginFragment;
import com.wisecityllc.cookedapp.fragments.RegistrationFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoginFragment.LoginFragmentInteractionListener, RegistrationFragment.RegistrationFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Tells us whether this is the first time starting up the activity or not
     */
    private boolean mHasInitialized = false;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean hasAlreadySetUpParse = false;

        if(savedInstanceState != null){
            hasAlreadySetUpParse = savedInstanceState.getBoolean("hasLoaded");
        }
        // Enable Local Datastore.
        if(hasAlreadySetUpParse == false) {
            Parse.enableLocalDatastore(this);

            Parse.initialize(this, "BrndBVrRczElKefgG3TvjCk3JYxtd5GB2GMzKoEP", "Xb7Pcc0lT2I3uJYNNoT6buaCuZ9dcvBMtCx9U5gw");
        }

        LoadingScreenFragment loadingFrag = new LoadingScreenFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, loadingFrag, "loading");
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mNavigationDrawerFragment == null)
            mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        if(mHasInitialized == false) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("loading")).commit();
            if (ParseUser.getCurrentUser() != null) {
                //We are already logged in, go to main activity
                switchToNavFragment();
            } else {
                //Go to login activity
                switchToLoginFragment();
            }
        }
    }

    private void switchToNavFragment() {

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        NavigationDrawerFragment drawerFrag = new NavigationDrawerFragment();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.container, drawerFrag, "");
        trans.commit();
    }

    private void switchToRegistrationFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new RegistrationFragment(), "registration")
                        // Add this transaction to the back stack
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void switchToLoginFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        LoginFragment loginFrag = new LoginFragment();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.container, loginFrag, "");
        trans.commit();
        if(mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


    }

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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
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
            getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public void registrationSucceeded() {
        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
        switchToNavFragment();
    }

    @Override
    public void loginAttemptFinished(boolean succeeded) {
        if(succeeded){
            Toast.makeText(this, "Login Succeeded.", Toast.LENGTH_SHORT).show();
            switchToNavFragment();
        }else{
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptLogout() {
        if(ParseUser.getCurrentUser() != null){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! We've logged out
                        switchToLoginFragment();
                    } else {
                        // Logout didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(getApplicationContext(), "Logout Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            switchToLoginFragment();
        }
    }


    @Override
    public void registrationButtonTouched() {
        switchToRegistrationFragment();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasLoaded", true);
    }
}
