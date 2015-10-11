package com.wisecityllc.cookedapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.GroupsQueryAdapter;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.User;

import java.util.ArrayList;

public class UserDetailsActivity extends AppCompatActivity {

    private ParseImageView mProfilePictureView;
    private TextView mNameField;
    private TextView mEmailAddressField;
    private TextView mPhoneNumberField;
    private TextView mLinkOneField;
    private TextView mLinkTwoField;
    private TextView mLinkThreeField;
    private TextView mDescriptionField;
    private ProgressBar mLoadingIndicator;
    private ListView mGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mProfilePictureView = (ParseImageView) findViewById(R.id.user_details_profile_picture);
        mNameField = (TextView) findViewById(R.id.user_details_name_text_view);
        mEmailAddressField = (TextView) findViewById(R.id.user_details_email_address_text_view);
        mPhoneNumberField = (TextView) findViewById(R.id.user_details_phone_number_text_view);
        mLinkOneField = (TextView) findViewById(R.id.user_details_link_1_text_view);
        mLinkTwoField = (TextView) findViewById(R.id.user_details_link_2_text_view);
        mLinkThreeField = (TextView) findViewById(R.id.user_details_link_3_text_view);
        mDescriptionField = (TextView) findViewById(R.id.user_details_description_text_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.user_details_loading_indicator);
        mGroupsList = (ListView) findViewById(R.id.user_details_groups_list);



        ParseUser user = ParseObject.createWithoutData(User.class, getIntent().getStringExtra("id"));


        ParseQuery<User> userQuery = new ParseQuery<>(User.class);
        userQuery.whereEqualTo("objectId", getIntent().getStringExtra("id"));
        userQuery.include(User._ADMIN_OF_ARRAY);
        userQuery.include(User._MEMBER_OF_ARRAY);

        mLoadingIndicator.setVisibility(View.VISIBLE);
         user.fetchIfNeededInBackground(new GetCallback<User>() {
            @Override
            public void done(User retrievedUser, ParseException e) {
                if (e == null && retrievedUser != null) {
                    fillInFieldsWithUser(retrievedUser);
                }else {
                    Toast.makeText(App.getContext(), "Couldn't retrieve user. Try again", Toast.LENGTH_SHORT).show();
                }
                mLoadingIndicator.setVisibility(View.GONE);
            }
        });

    }

    private void fillInFieldsWithUser(User user) {

        // Text fields
        mNameField.setText(user.getDisplayName());
        mEmailAddressField.setText(user.getEmail());
        mPhoneNumberField.setText(user.getPhoneNumber());
        mLinkOneField.setText(user.getLinkOne());
        mLinkTwoField.setText(user.getLinkTwo());
        mLinkThreeField.setText(user.getLinkThree());
        mDescriptionField.setText(user.getDescription());

        // List of groups
        ArrayList<Group> allGroups = new ArrayList<Group>();
        allGroups.addAll(user.getAdminGroups());
        allGroups.addAll(user.getMemberGroups());

//        GroupsAdapter groupsAdapter = new GroupsAdapter(this, R.layout.item_list_group, R.id.group_list_title, allGroups);
//
//        mGroupsList.setAdapter(groupsAdapter);
//
//        groupsAdapter.notifyDataSetChanged();

        GroupsQueryAdapter groupsAdapter = new GroupsQueryAdapter(this, GroupsQueryAdapter.MEMBER_AND_ADMIN_ONLY, user);

        mGroupsList.setAdapter(groupsAdapter);


        // Profile picture
        mProfilePictureView.setParseFile(user.getProfilePic());

        if(user.getProfilePic().isDataAvailable() == true) {
            try {
                byte[] bitmapdata = user.getProfilePic().getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                mProfilePictureView.setImageBitmap(bitmap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            mProfilePictureView.loadInBackground();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
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

    public static void startUserDetailsActivity(AppCompatActivity context, String userId) {
        Intent userDetailsActivityIntent = new Intent(context, UserDetailsActivity.class);
        userDetailsActivityIntent.putExtra("id", userId);
        context.startActivity(userDetailsActivityIntent);
    }

    public static void startUserDetailsActivity(AppCompatActivity context, ParseUser user) {
        Intent userDetailsActivityIntent = new Intent(context, UserDetailsActivity.class);
        userDetailsActivityIntent.putExtra("id", user.getObjectId());
        context.startActivity(userDetailsActivityIntent);
    }
}
