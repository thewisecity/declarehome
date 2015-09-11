package com.wisecityllc.cookedapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.MessageWallAdapter;
import com.wisecityllc.cookedapp.fragments.PostMessageUIFragment;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.Message;

import java.util.List;

public class MessageWallActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PostMessageUIFragment.OnPostMessageUIInteractionListener {

    private MessageWallAdapter mMessagesAdapter;
    private ListView mMessagesListView;
    private TextView mNoMessagesTextView;
    private ProgressBar mLoadingIndicator;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_wall);

        getSupportActionBar().hide();

        Intent intent = getIntent();

        String groupId = intent.getStringExtra("groupId");
        mMessagesAdapter = new MessageWallAdapter(this, groupId);

        mMessagesAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Message>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Message> list, Exception e) {
                mLoadingIndicator.setVisibility(View.GONE);
                mNoMessagesTextView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        if(mMessagesListView == null){
            mMessagesListView = (ListView)findViewById(R.id.messages_list_view);
        }

        mMessagesListView.setAdapter(mMessagesAdapter);
        mMessagesListView.setOnItemClickListener(this);

        mNoMessagesTextView = (TextView) findViewById(R.id.messages_activity_no_messages_text_view);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.messages_activity_loading_indicator);

        mMessagesAdapter.loadObjects();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_wall, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "Tapped " + ((Message) mMessagesAdapter.getItem(i)).getBody(), Toast.LENGTH_SHORT).show();
        Message selectedMessage = mMessagesAdapter.getItem(i);
    }

    @Override
    public void postNewMessage(String message) {
        Group grp = ParseObject.createWithoutData(Group.class, getIntent().getStringExtra("groupId"));
        Message.postNewMessage(ParseUser.getCurrentUser(), grp, message);
    }

    public static void startMessageWallActivityForGroup (Context context, Group group){
        Intent viewGroupMessageWallIntent = new Intent(context, MessageWallActivity.class);
        viewGroupMessageWallIntent.putExtra("groupId", group.getObjectId());
        viewGroupMessageWallIntent.putExtra("groupName", group.getName());
        viewGroupMessageWallIntent.putExtra("city", group.getCity());
        viewGroupMessageWallIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(viewGroupMessageWallIntent);
    }

}
