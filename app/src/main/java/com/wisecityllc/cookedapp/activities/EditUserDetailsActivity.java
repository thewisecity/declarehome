package com.wisecityllc.cookedapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.User;

public class EditUserDetailsActivity extends AppCompatActivity {

    private ParseImageView mProfilePictureView;
    private Button mUploadNewProfilePictureButton;
    private Button mSaveChangesButton;
    private EditText mNameField;
    private EditText mEmailAddressField;
    private EditText mPhoneNumberField;
    private EditText mLinkOneField;
    private EditText mLinkTwoField;
    private EditText mLinkThreeField;
    private EditText mDescriptionField;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        mProfilePictureView = (ParseImageView) findViewById(R.id.user_details_profile_picture);
        mUploadNewProfilePictureButton = (Button) findViewById(R.id.user_details_upload_profile_picture_button);
        mSaveChangesButton = (Button) findViewById(R.id.user_details_save_changes_button);
        mNameField = (EditText) findViewById(R.id.user_details_name_edit_text);
        mEmailAddressField = (EditText) findViewById(R.id.user_details_email_address_edit_text);
        mPhoneNumberField = (EditText) findViewById(R.id.user_details_phone_number_edit_text);
        mLinkOneField = (EditText) findViewById(R.id.user_details_link_1_edit_text);
        mLinkTwoField = (EditText) findViewById(R.id.user_details_link_2_edit_text);
        mLinkThreeField = (EditText) findViewById(R.id.user_details_link_3_edit_text);
        mDescriptionField = (EditText) findViewById(R.id.user_details_description_edit_text);


        mCurrentUser = (User) ParseUser.getCurrentUser();

        mProfilePictureView.setParseFile(mCurrentUser.getProfilePic());

        if(mCurrentUser.getProfilePic().isDataAvailable() == true) {
            try {
                byte[] bitmapdata = mCurrentUser.getProfilePic().getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                mProfilePictureView.setImageBitmap(bitmap);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            mProfilePictureView.loadInBackground();
        }

        mNameField.setText(mCurrentUser.getDisplayName());
        mEmailAddressField.setText(mCurrentUser.getEmail());
        mPhoneNumberField.setText(mCurrentUser.getPhoneNumber());
        mLinkOneField.setText(mCurrentUser.getLinkOne());
        mLinkTwoField.setText(mCurrentUser.getLinkTwo());
        mLinkThreeField.setText(mCurrentUser.getLinkThree());
        mDescriptionField.setText(mCurrentUser.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_user_details, menu);
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
