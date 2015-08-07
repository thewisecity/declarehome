package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.GroupDetailsActivity;
import com.wisecityllc.cookedapp.adapters.AllGroupsAdapter;
import com.wisecityllc.cookedapp.parseClasses.Group;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.wisecityllc.cookedapp.fragments.GroupsFragment.GroupsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private GroupsFragmentInteractionListener mListener;

    private AllGroupsAdapter mGroupsAdapter;
    private ListView mGroupsListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupsFragment.
     */

    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
        return fragment;
    }

    public GroupsFragment() {
        // Required empty public constructor
    }

    public void notifyGroupsDataUpdated() {
        if(mGroupsAdapter != null)
            mGroupsAdapter.loadObjects();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize main ParseQueryAdapter
        mGroupsAdapter = new AllGroupsAdapter(getActivity());



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mGroupsListView == null){
            mGroupsListView = (ListView)view.findViewById(R.id.groups_frag_list_view);
        }

        mGroupsListView.setAdapter(mGroupsAdapter);
        mGroupsListView.setOnItemClickListener(this);

        mGroupsAdapter.loadObjects();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
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
//        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "Tapped " + ((Group)mGroupsAdapter.getItem(i)).getName(), Toast.LENGTH_SHORT).show();
        Group selectedGroup = mGroupsAdapter.getItem(i);
        startCreateNewGroupActivity(selectedGroup);
    }

    private void startCreateNewGroupActivity(Group group){
        Intent viewGroupDetailsIntent = new Intent(getActivity(), GroupDetailsActivity.class);
        viewGroupDetailsIntent.putExtra("id", group.getObjectId());
        viewGroupDetailsIntent.putExtra("groupHashId", group.getGroupHashId());
        viewGroupDetailsIntent.putExtra("name", group.getName());
        viewGroupDetailsIntent.putExtra("purpose", group.getPurpose());
        viewGroupDetailsIntent.putExtra("neighberhoods", group.getNeighberhoods());
        viewGroupDetailsIntent.putExtra("city", group.getCity());
        viewGroupDetailsIntent.putExtra("state", group.getState());
        viewGroupDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(viewGroupDetailsIntent);
    }
}
