package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.wisecityllc.cookedapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.RegistrationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private RegistrationFragmentInteractionListener mListener;

    private EditText mDisplayNameField, mEmailField, mPasswordField;

    private Button mRegisterButton;

    private View mLoadingOverlay;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        return fragment;
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.RegistrationFragmentInteractionListener();
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (RegistrationFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mDisplayNameField = (EditText)getActivity().findViewById(R.id.registration_display_name_field);
        mPasswordField = (EditText)getActivity().findViewById(R.id.registration_password_field);
        mEmailField = (EditText)getActivity().findViewById(R.id.registration_email_field);

        mRegisterButton = (Button)getActivity().findViewById(R.id.registration_register_button);
        mLoadingOverlay = getActivity().findViewById(R.id.login_loading_view);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){

        if(verifyEmail() && verifyPassword() && verifyUsername()){
            mLoadingOverlay.setVisibility(View.VISIBLE);
            ParseUser user = new ParseUser();
            user.setUsername(mEmailField.getText().toString());
            user.setPassword(mPasswordField.getText().toString());
            user.setEmail(mEmailField.getText().toString());

            user.put("displayName", mDisplayNameField.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        mListener.registrationSucceeded();
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                    mLoadingOverlay.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    private boolean verifyEmail(){
        //TODO: implement
        return true;
    }

    private boolean verifyPassword(){
        //TODO: implement
        return true;
    }

    private boolean verifyUsername(){
        //TODO: implement
        return true;
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
    public interface RegistrationFragmentInteractionListener {
        public void registrationSucceeded();
        //public void registrationFailed();
    }

}