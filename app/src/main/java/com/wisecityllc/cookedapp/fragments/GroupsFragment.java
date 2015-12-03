package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.GroupsQueryAdapter;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.utilities.Stats;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.wisecityllc.cookedapp.fragments.GroupsFragment.GroupsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {

    public static final String MY_GROUPS_SCREEN = "My_Groups_Screen";
    public static final String ALL_GROUPS_SCREEN = "All_Groups_Screen";

    public static boolean sShouldIgnoreFirstAnalyticsCall = false;

    private GroupsFragmentInteractionListener mListener;

    private GroupsQueryAdapter mGroupsQueryAdapter;
    private ListView mGroupsListView;
    private ProgressBar mLoadingIndicator;
    private TextView mNoGroupsTextView;

    private BroadcastReceiver mBroadcastReceiver;

    private int mAdapterMode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupsFragment.
     */

    public static GroupsFragment newInstance(int mode) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle argsBundle = new Bundle();
        argsBundle.putInt("mode", mode);
        fragment.setArguments(argsBundle);
        return fragment;
    }

    public GroupsFragment() {

    }

    public void notifyGroupsDataUpdated() {
        if(mGroupsQueryAdapter != null)
            mGroupsQueryAdapter.loadObjects();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapterMode = getArguments().getInt("mode", 0);

        // Initialize main ParseQueryAdapter
        mGroupsQueryAdapter = new GroupsQueryAdapter(getActivity(), mAdapterMode);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupsListView = (ListView)view.findViewById(R.id.groups_frag_list_view);
        
        mNoGroupsTextView = (TextView)view.findViewById(R.id.groups_frag_no_groups_text_view);

        if(mAdapterMode == GroupsQueryAdapter.ALL_GROUPS)
            mNoGroupsTextView.setText("Looks like no Groups exist yet\nWhy not create one from the navigation drawer?");
        else if(mAdapterMode == GroupsQueryAdapter.MEMBER_AND_ADMIN_ONLY)
            mNoGroupsTextView.setText("You aren't yet a member of any groups\nOpen the navigation drawer to create a Group or join a group in your city");

        mLoadingIndicator = (ProgressBar)view.findViewById(R.id.groups_frag_loading_indicator);

        mGroupsQueryAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Group>() {
            @Override
            public void onLoading() {
//                if(mGroupsListView != null)
                    mGroupsListView.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mNoGroupsTextView.setVisibility(View.GONE);
            }

            @Override
            public void onLoaded(List<Group> list, Exception e) {
//                if(mGroupsListView != null)
                    mGroupsListView.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.GONE);
                mNoGroupsTextView.setVisibility((list != null && list.size() == 0) ? View.VISIBLE : View.GONE);
//                for(Group group : list){
//                    try {
//                        group.pin();
//                    } catch (ParseException err){
//                        err.printStackTrace();
//                    }
//                }

            }
        });
        mGroupsListView.setAdapter(mGroupsQueryAdapter);
        //mGroupsQueryAdapter.loadObjects();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GroupsFragmentInteractionListener) activity;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface GroupsFragmentInteractionListener {
//        public void onUserAcceptedInvitationToGroup();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            // Has become visible
            // If the app has just opened, avoid making this call since the View Pager will repeat it
            if(sShouldIgnoreFirstAnalyticsCall == false)
                Stats.ScreenMyGroups();
            sShouldIgnoreFirstAnalyticsCall = false;
        }
    }
}
