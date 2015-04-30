package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.wisecityllc.cookedapp.MainActivity;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.utilities.Verification;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.RegistrationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private MainActivity mListener;

    private EditText mDisplayNameField, mEmailField, mPasswordField;

    private ImageButton mProfilePicButton;

    private Bitmap mImageForUpload;

    private Button mRegisterButton;

    private ParseFile mProfilePicUploadFile;

    static final int REGISTRATION_REQUEST_PHOTO_CAPTURE = 100;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegistrationFragment.
     */
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
            mListener = (MainActivity) activity;
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

        mProfilePicButton = (ImageButton)getActivity().findViewById(R.id.registration_profile_pic_button);

        mRegisterButton = (Button)getActivity().findViewById(R.id.registration_register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mProfilePicButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REGISTRATION_REQUEST_PHOTO_CAPTURE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch(requestCode) {
            case REGISTRATION_REQUEST_PHOTO_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = returnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mProfilePicButton.setImageBitmap(imageBitmap);
                    mImageForUpload = imageBitmap;

                    //Format image for Parse upload
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // get byte array here
                    byte[] bytearray= stream.toByteArray();
                    mProfilePicUploadFile = new ParseFile("profilePic.jpg", bytearray);
                }
                break;
            default:
                break;
        }
    }

    private void registerUser() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String displayName = mDisplayNameField.getText().toString();
        if (Verification.verifyEmail(email) && Verification.verifyPassword(password) && Verification.verifyUsername(displayName) && mProfilePicUploadFile != null) {
            ((MainActivity)getActivity()).setLoading(true);
            final ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(password);
            user.setEmail(email);

            user.put("displayName", displayName);

            mProfilePicUploadFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        user.put("profilePic", mProfilePicUploadFile);
                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Hooray! Let them use the app now.
                                    mListener.registrationSucceeded();
                                } else {
                                    // Sign up didn't succeed. Look at the ParseException
                                    // to figure out what went wrong
                                }
                                if(getActivity() != null)
                                    ((MainActivity)getActivity()).setLoading(false);
                            }
                        });
                    }else{
                        //Problem saving photo
                    }
                }
            });


        }

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