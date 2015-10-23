package com.wisecityllc.cookedapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.WebAddress;


public class AboutThisAppActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar loadingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        loadingIcon = (ProgressBar)findViewById(R.id.progressBar);

        //Get a handle on our webview
        webView = (WebView)findViewById(R.id.webView);

        //Enable javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Don't redirect to browser for launching URLs
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadingIcon.setVisibility(View.INVISIBLE);
            }
        });



//        ParseQuery<User> userQuery = new ParseQuery<>(User.class);
//        userQuery.whereEqualTo("objectId", getIntent().getStringExtra("id"));
//        userQuery.include(User._ADMIN_OF_ARRAY);
//        userQuery.include(User._MEMBER_OF_ARRAY);
//
//        mLoadingIndicator.setVisibility(View.VISIBLE);
//        user.fetchIfNeededInBackground(new GetCallback<User>() {
//            @Override
//            public void done(User retrievedUser, ParseException e) {
//                if (e == null && retrievedUser != null) {
//                    fillInFieldsWithUser(retrievedUs  er);
//                } else {
//                    Toast.makeText(App.getContext(), "Couldn't retrieve user. Try again", Toast.LENGTH_SHORT).show();
//                }
//                mLoadingIndicator.setVisibility(View.GONE);
//            }
//        });

        ParseQuery<WebAddress> q = ParseQuery.getQuery(WebAddress.class);
        q.whereEqualTo(WebAddress._TITLE, WebAddress._ABOUT_TITLE);

        q.getFirstInBackground(new GetCallback<WebAddress>()
        {
            @Override
            public void done(WebAddress webAddress, ParseException e)
            {
                if (e == null)
                {
                    webView.loadUrl(webAddress.getString(WebAddress._URL));
                }
                else
                {
                    e.printStackTrace();
                    if(AboutThisAppActivity.this != null)
                    {
                        Toast.makeText(AboutThisAppActivity.this, "Failed to load About. Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
