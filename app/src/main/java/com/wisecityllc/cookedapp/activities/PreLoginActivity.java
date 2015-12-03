package com.wisecityllc.cookedapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.fragments.LoginFragment;
import com.wisecityllc.cookedapp.fragments.RegistrationFragment;
import com.wisecityllc.cookedapp.utilities.Installation;
import com.wisecityllc.cookedapp.utilities.Notifications;
import com.wisecityllc.cookedapp.utilities.Stats;


public class PreLoginActivity extends ActionBarActivity
        implements LoginFragment.LoginFragmentInteractionListener, RegistrationFragment.RegistrationFragmentInteractionListener {



    //================================================================================
    //region Properties
    //================================================================================


    /**
     * Set to true once we are actually showing the login fragment
     */
    private boolean mIsShowingLoginFragment = false;

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

    //endregion

    //================================================================================
    //region Activity Lifecycle Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);


        if(savedInstanceState != null){
            mHasInitializedFirstFragment = savedInstanceState.getBoolean("hasInitializedFirstFragment");
            mIsShowingLoginFragment = savedInstanceState.getBoolean("isShowingLoginFragment");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoadingOverlay = findViewById(R.id.main_loading_view);

        setLoading(false);
        if(mHasInitializedFirstFragment == false) {
            if (ParseUser.getCurrentUser() != null) {
                //We are already logged in, go to main activity
                switchToPostLoginActivity();

                //Make sure our current installation is associated with our current user
                Installation.associateCurrentUserWithCurrentInstallation();

                mHasInitializedFirstFragment = true;
            } else {
                //Unassociate our current Installation from any user
                Installation.unassociateCurrentUserFromCurrentInstallation();
                //Go to login activity
                switchToLoginFragment();
                mHasInitializedFirstFragment = true;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsShowingLoginFragment == true)
            Stats.ScreenLogin();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("hasInitializedFirstFragment", mHasInitializedFirstFragment);
        outState.putBoolean("isShowingLoginFragment", mIsShowingLoginFragment);
        super.onSaveInstanceState(outState);
    }

    //endregion


    //================================================================================
    //region Fragment Management Methods
    //================================================================================


    private void switchToPostLoginActivity() {
        Intent startPostLoginActivityIntent = new Intent(this, PostLoginActivity.class);
        startPostLoginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPostLoginActivityIntent);
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

        mIsShowingLoginFragment = true;

//        if(mNavigationDrawerFragment != null)
//            mNavigationDrawerFragment.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        Stats.TrackRegistrationSuccess();
//        try {
//            ParseUser.getCurrentUser().pin();
//        } catch (ParseException err) {
//            err.printStackTrace();
//        }
        try{
            ParseInstallation.getCurrentInstallation().save();
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        Notifications.setSubscriptionForAllNotifs(true);
        switchToPostLoginActivity();
        setLoading(false);
        if (ParseUser.getCurrentUser() != null) {
            //We are already logged in, go to main activity after registering user
            Stats.AliasAndIdentifyUser();
        }
    }

    @Override
    public void registrationFailed() {
        Toast.makeText(this, "Error while registering. Check your info and try again", Toast.LENGTH_SHORT).show();
        Stats.TrackRegistrationFailed();
    }

    @Override
    public void loginAttemptFinished(boolean succeeded) {
        if(succeeded){
//            try {
//                ParseUser.getCurrentUser().pin();
//            } catch (ParseException err) {
//                err.printStackTrace();
//            }
            ParseInstallation cInstallation = ParseInstallation.getCurrentInstallation();
            cInstallation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null)
                        e.printStackTrace();
                    else {
                        Log.d("DEX", "installation saved");
                        Notifications.setSubscriptionForAllNotifs(true);
                    }
                }
            });
            Toast.makeText(this, "Login Succeeded.", Toast.LENGTH_SHORT).show();
            switchToPostLoginActivity();
            setLoading(false);
            Stats.AliasAndIdentifyUser();
            Stats.TrackLoginSuccess();
        }else{
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            Stats.TrackLoginFailed();
            setLoading(false);
        }
    }

    @Override
    public void registrationButtonTouched() {
        switchToRegistrationFragment();
    }
    //endregion


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Declare Home");
    }




    public void setLoading(boolean isLoading){
        if(isLoading){
            mLoadingOverlay.setVisibility(View.VISIBLE);
        }else{
            mLoadingOverlay.setVisibility(View.INVISIBLE);
        }
    }




}
