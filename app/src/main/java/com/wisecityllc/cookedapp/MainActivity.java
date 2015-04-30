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
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.fragments.LoginFragment;
import com.wisecityllc.cookedapp.fragments.RegistrationFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoginFragment.LoginFragmentInteractionListener, RegistrationFragment.RegistrationFragmentInteractionListener {


    //================================================================================
    //region Properties
    //================================================================================

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Tells us whether this is the first time starting up the activity or not
     */
    private boolean mHasInitializedFirstFragment = false;


    /**
     * Holds a reference to whatever the current Image Picker button is.
     * This can be in a number of fragments, and as such we want to have one
     * reference so that all the functionality which occurs in onActivityResult(...)
     * can be repurposed no matter what the current fragment shown is
     */
    private ImageButton mImagePickerButton;

    /**
     * The loading view used throughout the app to indicate that something is taking place / loading is occuring
     * Used in place of having a loading indicator for each fragment that needs one throughout the app
     */
    private View mLoadingOverlay;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    //endregion

    //================================================================================
    //region Activity Lifecycle Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState != null){
            mHasInitializedFirstFragment = savedInstanceState.getBoolean("hasInitializedFirstFragment");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoadingOverlay = findViewById(R.id.main_loading_view);

        if(mNavigationDrawerFragment == null)
            mNavigationDrawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        setLoading(false);
        if(mHasInitializedFirstFragment == false) {
            if (ParseUser.getCurrentUser() != null) {
                //We are already logged in, go to main activity
                switchToNavFragment();
                mHasInitializedFirstFragment = true;
            } else {
                //Go to login activity
                switchToLoginFragment();
                mHasInitializedFirstFragment = true;
            }
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("hasInitializedFirstFragment", mHasInitializedFirstFragment);
        super.onSaveInstanceState(outState);
    }

    //endregion


    //================================================================================
    //region Fragment Management Methods
    //================================================================================



    private void switchToNavFragment() {

        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        NavigationDrawerFragment drawerFrag = new NavigationDrawerFragment();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.container, drawerFrag, "");
        trans.commit();
    }

    private void switchToRegistrationFragment() {
        RegistrationFragment regFrag = new RegistrationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, regFrag, null)
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
        trans.add(R.id.container, loginFrag, null);
        trans.commit();
        if(mNavigationDrawerFragment != null)
            mNavigationDrawerFragment.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void setCurrentImagePickerButton(ImageButton picker){
        mImagePickerButton = picker;
    }

    //endregion

    //================================================================================
    //region Fragment Callback Methods
    //================================================================================

    @Override
    public void registrationSucceeded() {
        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
        switchToNavFragment();
        setLoading(false);
    }

    @Override
    public void loginAttemptFinished(boolean succeeded) {
        if(succeeded){
            Toast.makeText(this, "Login Succeeded.", Toast.LENGTH_SHORT).show();
            switchToNavFragment();
            setLoading(false);
        }else{
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            setLoading(false);
        }
    }

    @Override
    public void registrationButtonTouched() {
        switchToRegistrationFragment();
    }
    //endregion

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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    //endregion

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

    public void setLoading(boolean isLoading){
        if(isLoading){
            mLoadingOverlay.setVisibility(View.VISIBLE);
        }else{
            mLoadingOverlay.setVisibility(View.INVISIBLE);
        }
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

}
