package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.AlertCategoryAdapter;
import com.wisecityllc.cookedapp.adapters.GroupsCheckboxAdapter;
import com.wisecityllc.cookedapp.adapters.delegates.AlertCategoryAdapterDelegate;
import com.wisecityllc.cookedapp.adapters.delegates.GroupsCheckboxAdapterDelegate;
import com.wisecityllc.cookedapp.parseClasses.AlertCategory;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.utilities.Stats;
import com.wisecityllc.cookedapp.views.ExtendedEditText;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostMessageUIFragment.OnPostMessageUIInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostMessageUIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostMessageUIFragment extends Fragment implements ExtendedEditText.OnBackButtonPressedWhileHasFocusListener, AlertCategoryAdapterDelegate, GroupsCheckboxAdapterDelegate {

    private int NEW_MESSAGE = 0;
    private int NEW_ALERT = 1;
    private int NEW_EVENT = 2;

    private OnPostMessageUIInteractionListener mListener;

    private FrameLayout mEditTextLayout;

    private RelativeLayout mPickAlertTypeLayout;
    private RelativeLayout mPickGroupsForAlertLayout;

    private ListView mAlertCategoryListView;
    private ListView mGroupsForAlertListView;

    private TextView mAlertTypeTextView;

    private View mGreyBackground;
    private ToggleButton mPlusButton;
    private Button mPostAlertButton;
    private Button mPostEventButton;
    private Button mPostMessageButton;

    private Button mFinishPostButton;

    private Button mFinishedChoosingGroupsButton;

    private ExtendedEditText mMessageBodyEditText;

    private AlertCategory mChosenAlertCategory;
    private ArrayList<Group> mSelectedGroupsForAlert;

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
        mSelectedGroupsForAlert = new ArrayList<>();
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
                    Stats.TrackOpenedNewMessageMenu();
                    showPostMessageButtons(true);
                } else {
                    Stats.TrackClosedNewMessageMenu();
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

        mPostAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginMessageCreation(NEW_ALERT);
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

        // Tells our ExtendedEditText that it should tell this fragment when the back
        // button is pressed while it has focus.
        mMessageBodyEditText.setListener(this);

        mPickAlertTypeLayout = (RelativeLayout) view.findViewById(R.id.post_message_ui_choose_category_area);
        mPickGroupsForAlertLayout = (RelativeLayout) view.findViewById(R.id.post_message_ui_choose_groups_area);

        mAlertTypeTextView = (TextView) view.findViewById(R.id.post_message_ui_choose_group_header_category_type);

        mAlertCategoryListView = (ListView) view.findViewById(R.id.post_message_ui_category_list_view);
        mGroupsForAlertListView = (ListView) view.findViewById(R.id.post_message_ui_groups_list_view);

        mAlertCategoryListView.setAdapter(new AlertCategoryAdapter(this.getActivity(), this));

        mGroupsForAlertListView.setAdapter(new GroupsCheckboxAdapter(this.getActivity(), this));

        mFinishedChoosingGroupsButton = (Button) view.findViewById(R.id.post_message_ui_finish_choosing_groups_button);

        mFinishedChoosingGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Make sure at least one group is checked here
                if(mSelectedGroupsForAlert.size() > 0)
                    groupsFinished();
                else
                    Toast.makeText(getActivity(), "Must select at least one group for alert", Toast.LENGTH_SHORT).show();
            }
        });


        cancelMessageCreation();

    }

    public void cancelMessageCreation() {
        mChosenAlertCategory = null;
        unselectAllCheckedGroups();
        hidePickGroupsForAlert();
        hidePickAlertTypeForAlert();
        configureMessageCompositionAreaForAlertCategory(null);
        clearMessageText();
        App.hideKeyboard(this.getActivity());
        mPlusButton.setVisibility(View.VISIBLE);
        mPlusButton.setChecked(false);
        mEditTextLayout.setVisibility(View.GONE);
        showPostMessageButtons(false);
    }

    private void unselectAllCheckedGroups() {
        mSelectedGroupsForAlert.clear();
        GroupsCheckboxAdapter adapter = (GroupsCheckboxAdapter) mGroupsForAlertListView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    private void clearMessageText() {
        mMessageBodyEditText.getText().clear();
    }

    private void showPostMessageButtons(boolean visible) {
        mPostAlertButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        // Uncomment this line to re-enable post event button
        mPostEventButton.setVisibility(View.GONE);
        // mPostEventButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mPostMessageButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        mGreyBackground.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void beginMessageCreation(int messageType) {
        mPlusButton.setVisibility(View.GONE);
        showPostMessageButtons(false);

        if(messageType == NEW_MESSAGE) {
            mEditTextLayout.setVisibility(View.VISIBLE);
            if(mMessageBodyEditText.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mMessageBodyEditText, InputMethodManager.SHOW_IMPLICIT);
            }
            Stats.TrackBeganMessageCreation();
        }else if(messageType == NEW_ALERT) {
            showPickAlertTypeForAlert();
            Stats.TrackBeganAlertCreation();
        }

    }

    private void onPostButtonPressed() {
        if (mListener != null && mMessageBodyEditText != null) {
            if(mChosenAlertCategory == null) {
                // Post a new message
                mListener.postNewMessage(mMessageBodyEditText.getText().toString());
            }else{
                // Post a new alert
                String finalText = mMessageBodyEditText.getText().toString();
                finalText = finalText.replace(mChosenAlertCategory.getTitle(), "");
                finalText.trim();
                mListener.postNewAlert(finalText, mSelectedGroupsForAlert, mChosenAlertCategory);
            }
        }
        // Resets our views
        cancelMessageCreation();
    }


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
        Stats.TrackEndedMessageCreation();
    }

    private void showPickAlertTypeForAlert() {
        mPickAlertTypeLayout.setVisibility(View.VISIBLE);
    }

    private void hidePickAlertTypeForAlert() {
        mPickAlertTypeLayout.setVisibility(View.GONE);
    }

    private void showPickGroupsForAlert(@NonNull AlertCategory chosenCategory) {
        mPickGroupsForAlertLayout.setVisibility(View.VISIBLE);
    }

    private void hidePickGroupsForAlert() {
        mPickGroupsForAlertLayout.setVisibility(View.GONE);
    }

    private void showAlertComposition(@NonNull AlertCategory chosenCategory) {

        configureMessageCompositionAreaForAlertCategory(chosenCategory);

        mPlusButton.setVisibility(View.GONE);
        showPostMessageButtons(false);
        mEditTextLayout.setVisibility(View.VISIBLE);
        if(mMessageBodyEditText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mMessageBodyEditText, InputMethodManager.SHOW_IMPLICIT);
        }
        Stats.TrackBeganAlertComposition();
    }

    private void configureMessageCompositionAreaForAlertCategory(@Nullable final AlertCategory chosenCategory) {
        mMessageBodyEditText.setHint(chosenCategory == null ? "Send Message" : "Send Alert");
        insertColoredTextForCategoryTitle(chosenCategory);
        preventDeletionOfCategoryTitle(chosenCategory);
    }

    private void insertColoredTextForCategoryTitle(AlertCategory chosenCategory){
        mMessageBodyEditText.getText().clear();
        if(chosenCategory!= null) {
            Spannable modifiedText = new SpannableString(chosenCategory.getTitle() + " ");
            modifiedText.setSpan(new ForegroundColorSpan(Color.rgb(chosenCategory.getTextColorR().intValue(), chosenCategory.getTextColorG().intValue(), chosenCategory.getTextColorB().intValue())), 0, chosenCategory.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            modifiedText.setSpan(new BackgroundColorSpan(Color.rgb(chosenCategory.getBGColorR().intValue(), chosenCategory.getBGColorG().intValue(), chosenCategory.getBGColorB().intValue())), 0, chosenCategory.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mMessageBodyEditText.setText(modifiedText);
        }
    }

    private void preventDeletionOfCategoryTitle(final AlertCategory chosenCategory) {
        if(chosenCategory != null){
            mMessageBodyEditText.setFilters(new InputFilter[]{
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {

                            // If dstart intrudes anywhere before the end of our category title,
                            // replace what already exists there with what already exists there

                            if(dstart < chosenCategory.getTitle().length())
                                return dst.subSequence(dstart, dend);
                            else
                                return null;

                        }
                    }
            });
        }else{
            mMessageBodyEditText.setFilters(new InputFilter[]{});
        }
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
        void postNewMessage(String message);
        void postNewAlert(String message, ArrayList<Group> groups, AlertCategory category);
//        public void postNewAlert(String alert);
    }

    @Override
    public void categoryTapped(AlertCategory category) {
        hidePickAlertTypeForAlert();
        showPickGroupsForAlert(category);
        mChosenAlertCategory = category;
        mAlertTypeTextView.setText("Alert Category: " + '"' + category.getTitle() + '"');
    }

    @Override
    public void groupChecked(Group group) {
        mSelectedGroupsForAlert.add(group);
    }

    @Override
    public void groupUnchecked(Group group) {
        mSelectedGroupsForAlert.remove(group);
    }

    @Override
    public void groupsFinished() {
        hidePickGroupsForAlert();
        showAlertComposition(mChosenAlertCategory);
    }

    @Override
    public boolean isGroupChecked(Group group) {
        return mSelectedGroupsForAlert.contains(group);
    }
}
