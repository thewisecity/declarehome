package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.views.ExtendedEditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostMessageUIFragment.OnPostMessageUIInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostMessageUIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostMessageUIFragment extends Fragment implements ExtendedEditText.OnBackButtonPressedWhileHasFocusListener {

    private int NEW_MESSAGE = 0;
    private int NEW_ALERT = 1;
    private int NEW_EVENT = 2;

    private OnPostMessageUIInteractionListener mListener;

    private FrameLayout mEditTextLayout;

    private View mGreyBackground;
    private ToggleButton mPlusButton;
    private Button mPostAlertButton;
    private Button mPostEventButton;
    private Button mPostMessageButton;

    private Button mFinishPostButton;

    private ExtendedEditText mMessageBodyEditText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PostMessageUIFragment.
     */
    public static PostMessageUIFragment newInstance() {
        PostMessageUIFragment fragment = new PostMessageUIFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PostMessageUIFragment() {
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
        return inflater.inflate(R.layout.fragment_post_message_ui, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGreyBackground = view.findViewById(R.id.post_message_ui_grey_background);

        mPlusButton = (ToggleButton) view.findViewById(R.id.post_message_ui_plus_button);

        mPlusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showPostMessageButtons(true);
                } else {
                    cancelMessageCreation();
                }
            }
        });

        mEditTextLayout = (FrameLayout) view.findViewById(R.id.post_message_ui_message_edit_text_holder);

        mFinishPostButton = (Button) view.findViewById(R.id.post_message_ui_send_button);

        mFinishPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostButtonPressed();
            }
        });

        mPostAlertButton = (Button) view.findViewById(R.id.post_message_ui_post_action_alert_button);
        mPostEventButton = (Button) view.findViewById(R.id.post_message_ui_post_event_button);
        mPostMessageButton = (Button) view.findViewById(R.id.post_message_ui_post_message_button);

        mPostMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginMessageCreation(NEW_MESSAGE);
            }
        });

        mMessageBodyEditText = (ExtendedEditText) view.findViewById(R.id.post_message_ui_message_edit_text);


        // We want to know when our EditText loses focus because when it does we want
        // the fragment to respond as if we had 'unchecked' the Plus button
        mMessageBodyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    mPlusButton.setChecked(false);
                }
            }
        });

        mPlusButton.setChecked(false);
        cancelMessageCreation();

        // Tells our ExtendedEditText that it should tell this fragment when the back
        // button is pressed while it has focus.
        mMessageBodyEditText.setListener(this);

    }

    private void cancelMessageCreation() {
        clearMessageText();
        App.hideKeyboard(this.getActivity());
        mPlusButton.setVisibility(View.VISIBLE);
        mEditTextLayout.setVisibility(View.GONE);
        showPostMessageButtons(false);

    }

    private void clearMessageText() {
        mMessageBodyEditText.getText().clear();
    }

    private void showPostMessageButtons(boolean visible) {
        mPostAlertButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mPostEventButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mPostMessageButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mGreyBackground.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void beginMessageCreation(int messageType) {
        mPlusButton.setVisibility(View.GONE);
        showPostMessageButtons(false);
        mEditTextLayout.setVisibility(View.VISIBLE);
        if(mMessageBodyEditText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mMessageBodyEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }



    private void onPostButtonPressed() {
        if (mListener != null && mMessageBodyEditText != null) {
            mListener.postNewMessage(mMessageBodyEditText.getText().toString());
        }
        // Resets our views
        cancelMessageCreation();
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostMessageUIInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This is the callback which is launched when our ExtendedEditText tells us we have
     * pressed the hardware Back button down while it had focus. If we don't have this,
     * we have no way of manipulating the UI at the time that this action occurs.
     */
    @Override
    public void pressedBackWithFocus(){
        cancelMessageCreation();
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
    public interface OnPostMessageUIInteractionListener {
        public void postNewMessage(String message);
    }



}
