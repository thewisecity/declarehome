package com.wisecityllc.cookedapp.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;
import com.segment.analytics.Analytics;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.AlertWallAdapter;
import com.wisecityllc.cookedapp.parseClasses.Message;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlertsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertsFragment extends Fragment {

    public static final String ALERTS_SCREEN = "AlertsScreen";

    private boolean mHasMadeInitialLoad = false;

    private AlertWallAdapter mAlertsAdapter;
    private ListView mAlertsListView;
    private TextView mNoAlertsTextView;
    private ProgressBar mLoadingIndicator;

    public static AlertsFragment newInstance() {
        AlertsFragment fragment = new AlertsFragment();
        return fragment;
    }

    public AlertsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.alerts_fragment_loading_indicator);

        mAlertsAdapter = new AlertWallAdapter(this.getActivity());

        mAlertsAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Message>() {
            @Override
            public void onLoading() {
                mAlertsListView.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mNoAlertsTextView.setVisibility(View.GONE);
            }

            @Override
            public void onLoaded(List<Message> list, Exception e) {
                mLoadingIndicator.setVisibility(View.GONE);
                mNoAlertsTextView.setVisibility(e != null || list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                mAlertsListView.setVisibility(View.VISIBLE);
            }
        });

        if(mAlertsListView == null){
            mAlertsListView = (ListView)view.findViewById(R.id.alerts_list_view);
        }



        mNoAlertsTextView = (TextView) view.findViewById(R.id.alerts_fragment_no_messages_text_view);





//        mAlertsListView.setOnItemClickListener(this);



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {

            // Has become visible
            Analytics.with(getActivity()).screen(null, ALERTS_SCREEN);

            // Delay our loading until we become visible
            if(mHasMadeInitialLoad == false && mAlertsAdapter != null) {
                mAlertsListView.setAdapter(mAlertsAdapter);
                mHasMadeInitialLoad = true;
            }

        }

    }

}
