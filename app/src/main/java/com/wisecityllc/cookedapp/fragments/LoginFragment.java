package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.PreLoginActivity;
import com.wisecityllc.cookedapp.utilities.Stats;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.LoginFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private Button mSignInButton;
    private Button mRegisterButton;

    private EditText mEmailField;
    private EditText mPasswordField;

    private View mLoadingOverlay;


    private LoginFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (LoginFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mSignInButton = (Button)getActivity().findViewById(R.id.login_sign_in_button);
        mRegisterButton = (Button)getActivity().findViewById(R.id.login_register_button);
        mLoadingOverlay = getActivity().findViewById(R.id.login_loading_view);
        mEmailField = (EditText)getActivity().findViewById(R.id.login_email_field);
        mPasswordField = (EditText)getActivity().findViewById(R.id.login_password_field);

        if(mSignInButton != null)
            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

        if(mRegisterButton != null)
            mRegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
    //                startRegistrationActivity();
    //                printShit();
                    mListener.registrationButtonTouched();

                }
            });
        if(mLoadingOverlay != null)
            mLoadingOverlay.setVisibility(View.INVISIBLE);
    }

    private void attemptLogin(){
        mLoadingOverlay.setVisibility(View.VISIBLE);

        Stats.TrackLoginAttempt();

        ParseUser.logInInBackground(mEmailField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    //Login successful!
                    mListener.loginAttemptFinished(true);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    mListener.loginAttemptFinished(false);
                }
                //No matter what happens, stop showing our lo//ading indicator
                mLoadingOverlay.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void loginSuccessful() {
        Intent startActivityIntent = new Intent(getActivity(), PreLoginActivity.class);
        startActivity(startActivityIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface LoginFragmentInteractionListener {
        public void loginAttemptFinished(boolean succeeded);
        public void registrationButtonTouched();
    }

}
