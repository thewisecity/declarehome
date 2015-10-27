package com.wisecityllc.cookedapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.wisecityllc.cookedapp.R;

public final class IntroSlideFragment extends Fragment {
    int imageResourceId;

    public IntroSlideFragment() {
        super();
    }

    private ImageView mImageView;
    private Button mDoneButton;
    private boolean isLast;

    public IntroSlideFragment(int i, boolean last) {
        imageResourceId = i;
        isLast = last;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_intro_slide, container, false);

//        ImageView image = new ImageView(getActivity());
//        image.setImageResource(imageResourceId);
//
//        LinearLayout layout = new LinearLayout(getActivity());
////        layout.setLayoutParams(new LinearLayoutCompat.LayoutParams()
//
//        layout.setGravity(Gravity.CENTER);
//        layout.addView(image);
//
//        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageView = (ImageView) view.findViewById(R.id.intro_slide_image);
        mImageView.setImageResource(imageResourceId);

        mDoneButton = (Button) view.findViewById(R.id.intro_slide_done_button);
        if(isLast)
        {
            mDoneButton.setVisibility(View.VISIBLE);
            mDoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntroSlideFragment.this.getActivity().finish();
                }
            });
        }
        else
        {
            mDoneButton.setVisibility(View.GONE);
            mDoneButton.setOnClickListener(null);
        }


    }
}