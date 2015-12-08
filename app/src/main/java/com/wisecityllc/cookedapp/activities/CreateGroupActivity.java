package com.wisecityllc.cookedapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.utilities.Stats;

public class CreateGroupActivity extends ActionBarActivity {

    private EditText field_name;
    private EditText field_purpose;
    private EditText field_neighberhoods;
    private EditText field_address;
    private EditText field_city;
    private EditText field_state;
    private EditText field_website;
    private EditText field_facebook;
    private EditText field_twitter;
    private Button button_createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    @Override
    protected void onStart() {
        super.onStart();
        field_name = (EditText)findViewById(R.id.create_group_group_name);
        field_purpose = (EditText)findViewById(R.id.create_group_group_purpose);
        field_neighberhoods = (EditText)findViewById(R.id.create_group_neighberhoods);
        field_address = (EditText)findViewById(R.id.create_group_group_street_address);
        field_city = (EditText)findViewById(R.id.create_group_city);
        field_state = (EditText)findViewById(R.id.create_group_state);
        field_website = (EditText)findViewById(R.id.create_group_website);
        field_facebook = (EditText)findViewById(R.id.create_group_facebook);
        field_twitter = (EditText)findViewById(R.id.create_group_twitter);
        button_createGroup = (Button) findViewById(R.id.create_group_done_button);

        button_createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGroupForCreation();
            }
        });
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

    protected void submitGroupForCreation() {
        if(field_name.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Group Name cannot be empty", Toast.LENGTH_SHORT).show();
        } else if(field_purpose.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "Group Purpose cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            Group.createGroup(field_name.getText().toString().trim(), field_purpose.getText().toString().trim(), field_neighberhoods.getText().toString().trim(), field_address.getText().toString().trim(), field_city.getText().toString().trim(), field_state.getText().toString().trim(), field_website.getText().toString().trim(), field_facebook.getText().toString().trim(), field_twitter.getText().toString().trim());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Stats.ScreenCreateGroup();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stats.TrackGroupCreationCancelled();
    }
}
