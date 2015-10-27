package com.wisecityllc.cookedapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.fragments.IntroSlideFragment;

/**
 * Created by dexterlohnes on 10/5/15.
 */
public class IntroSlidesFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

    private int[] Images = new int[] { R.drawable.photo1, R.drawable.photo2,
            R.drawable.photo3, R.drawable.photo4

    };

    protected static final int[] ICONS = new int[] { R.drawable.marker,
            R.drawable.marker, R.drawable.marker, R.drawable.marker };

    private int mCount = Images.length;

    public IntroSlidesFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        boolean isLast = (position == Images.length - 1);
        return new IntroSlideFragment(Images[position], isLast);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
