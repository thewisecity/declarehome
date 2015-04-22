package com.wisecityllc.cookedapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wisecityllc.cookedapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoadingScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadingScreenFragment extends Fragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoadingScreenFragment.
     */
    public static LoadingScreenFragment newInstance() {
        LoadingScreenFragment fragment = new LoadingScreenFragment();
        return fragment;
    }

    public LoadingScreenFragment() {
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
        return inflater.inflate(R.layout.fragment_loading_screen, container, false);
    }


}
