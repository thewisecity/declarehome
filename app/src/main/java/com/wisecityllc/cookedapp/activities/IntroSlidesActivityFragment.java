package com.wisecityllc.cookedapp.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisecityllc.cookedapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class IntroSlidesActivityFragment extends Fragment {

    public IntroSlidesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_slides, container, false);
    }
}
