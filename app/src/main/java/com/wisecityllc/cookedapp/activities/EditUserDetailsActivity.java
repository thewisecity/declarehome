package com.wisecityllc.cookedapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.User;
import com.wisecityllc.cookedapp.utilities.Validation;

import java.io.ByteArrayOutputStream;

public class EditUserDetailsActivity extends AppCompatActivity {

    private final static int REQUEST_PHOTO_CAPTURE = 100;

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

    private Bitmap mImageForUpload;

    private ParseFile mProfilePicUploadFile;


    private ProgressBar mLoadingIndicator;

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
        mLoadingIndicator = (ProgressBar) findViewById(R.id.user_details_loading_indicator);


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

        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateFields() == true) {
                    //All fields are valid
                    mCurrentUser.setDescription(mDescriptionField.getText().toString());
                    mCurrentUser.setEmail(mEmailAddressField.getText().toString());
                    mCurrentUser.setDisplayName(mNameField.getText().toString());
                    mCurrentUser.setPhoneNumber(mPhoneNumberField.getText().toString());
                    mCurrentUser.setLinkOne(mLinkOneField.getText().toString());
                    mCurrentUser.setLinkTwo(mLinkTwoField.getText().toString());
                    mCurrentUser.setLinkThree(mLinkThreeField.getText().toString());

                    if(mProfilePicUploadFile != null)
                        mCurrentUser.setProfilePic(mProfilePicUploadFile);

                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null)
                                Toast.makeText(App.getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(App.getContext(), "Error while saving. Please try again", Toast.LENGTH_SHORT).show();

                            // Either way...
                            if(mLoadingIndicator != null)
                                mLoadingIndicator.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });


        mUploadNewProfilePictureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_PHOTO_CAPTURE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        switch(requestCode) {
            case REQUEST_PHOTO_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = returnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mProfilePictureView.setImageBitmap(imageBitmap);
                    mImageForUpload = imageBitmap;

                    //Format image for Parse upload
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    // get byte array here
                    byte[] bytearray= stream.toByteArray();
                    mProfilePicUploadFile = new ParseFile("profilePic.jpg", bytearray);
                }
                break;
            default:
                break;
        }
    }

    private boolean validateFields() {
        boolean allFieldsValid = true;
        String errorToastText = "";
        if(Validation.validateDisplayName(mNameField.getText().toString()) == false) {
            errorToastText += "Name must be at least one character\n";
            allFieldsValid = false;
        }

        if(Validation.validatePhoneNumber(mPhoneNumberField.getText().toString()) == false) {
            errorToastText += "Phone number must be at least 7 digits\n";
            allFieldsValid = false;
        }

        if(Validation.validateEmail(mEmailAddressField.getText().toString()) == false){
            errorToastText += "Please enter a valid email address\n";
            allFieldsValid = false;
        }

        if(allFieldsValid == false)
            Toast.makeText(this, errorToastText, Toast.LENGTH_SHORT).show();

        return allFieldsValid;
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
