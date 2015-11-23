package com.wisecityllc.cookedapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.activities.UserDetailsActivity;
import com.wisecityllc.cookedapp.parseClasses.User;

/**
 * Created by dexterlohnes on 10/9/15.
 */
public class ClickableUserPortrait extends ParseImageView {

    ParseFile mFile;

    public ClickableUserPortrait(Context context) {
        super(context);
        if(context instanceof AppCompatActivity == false)
            throw new RuntimeException("ClickableUserPortrait context must be an AppCompatActivity");
    }

    public ClickableUserPortrait(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if(context instanceof AppCompatActivity == false)
            throw new RuntimeException("ClickableUserPortrait context must be an AppCompatActivity");
    }

    public ClickableUserPortrait(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        if(context instanceof AppCompatActivity == false)
            throw new RuntimeException("ClickableUserPortrait context must be an AppCompatActivity");
    }

    public void setUser(final User user, boolean loadNow) {
        mFile = user.getParseFile(("profilePic"));
        setParseFile(mFile);
        if(loadNow)
            load();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.startUserDetailsActivity((AppCompatActivity)getContext(), user);
            }
        });
    }


    public void setUser(final ParseUser user, boolean loadNow) {
        mFile = user.getParseFile(("profilePic"));
        setParseFile(mFile);
        if(loadNow)
            load();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.startUserDetailsActivity((AppCompatActivity)getContext(), user);
            }
        });
    }

    private void load() {
        // I think / hope that this is helping to avoid multiple calls to the same thing when we've already loaded a file before
        if(mFile.isDataAvailable()){
            try {
                byte[] bitmapdata = mFile.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    setImageBitmap(bitmap);
                } catch (java.lang.OutOfMemoryError error) {
                    Log.d("DeclareHome", "Out of memory error while loading user image " + mFile.getUrl());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            setParseFile(mFile);
            loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e != null) {
                        Log.e("DEX", e.getMessage());
                    }
                }
            });
        }
    }

}
