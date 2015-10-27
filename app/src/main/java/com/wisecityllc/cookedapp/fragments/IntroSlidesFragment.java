package com.wisecityllc.cookedapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.IntroSlidesFragmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntroSlidesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntroSlidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroSlidesFragment extends Fragment {
    IntroSlidesFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    public static final String TAG = "detailsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_slides,
                container, false);

        mAdapter = new IntroSlidesFragmentAdapter(getActivity()
                .getSupportFragmentManager());

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        ((CirclePageIndicator) mIndicator).setSnap(true);

        mIndicator
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        Toast.makeText(IntroSlidesFragment.this.getActivity(),
                                "Changed to page " + position,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
        return view;
    }

}