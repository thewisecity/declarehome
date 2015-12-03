package com.wisecityllc.cookedapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.segment.analytics.Properties;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.MessageWallAdapter;
import com.wisecityllc.cookedapp.fragments.PostMessageUIFragment;
import com.wisecityllc.cookedapp.parseClasses.AlertCategory;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.Message;
import com.wisecityllc.cookedapp.utilities.Stats;

import java.util.ArrayList;
import java.util.List;

public class MessageWallActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, PostMessageUIFragment.OnPostMessageUIInteractionListener {

    private static String GROUP_ID_EXTRA = "GroupId";

    private PostMessageUIFragment mMessageUIFragment;
    private MessageWallAdapter mMessagesAdapter;
    private ListView mMessagesListView;
    private TextView mNoMessagesTextView;
    private ProgressBar mLoadingIndicator;
    private Group mGroup;

    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_wall);

        getSupportActionBar().hide();

        Intent intent = getIntent();

        mLoadingIndicator = (ProgressBar) findViewById(R.id.messages_activity_loading_indicator);

        String groupId = intent.getStringExtra("groupId");
        mMessagesAdapter = new MessageWallAdapter(this, groupId);

        mMessagesAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Message>() {
            @Override
            public void onLoading() {
                mMessagesListView.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mNoMessagesTextView.setVisibility(View.GONE);
            }

            @Override
            public void onLoaded(List<Message> list, Exception e) {
                mLoadingIndicator.setVisibility(View.GONE);
                mNoMessagesTextView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                mMessagesListView.setVisibility(View.VISIBLE);
            }
        });

        if(mMessagesListView == null){
            mMessagesListView = (ListView)findViewById(R.id.messages_list_view);
        }



        mNoMessagesTextView = (TextView) findViewById(R.id.messages_activity_no_messages_text_view);


//        mMessageUIFragment = (PostMessageUIFragment) findViewById(R.id.post_message_ui_fragment);
        mMessageUIFragment = (PostMessageUIFragment) getFragmentManager().findFragmentById(R.id.post_message_ui_fragment);


        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(getString(R.string.broadcast_message_saved_success))) {
                    mMessagesAdapter.loadObjects();
                }
            }
        };


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(getString(R.string.broadcast_message_saved_success)));

        mMessagesListView.setAdapter(mMessagesAdapter);
        mMessagesListView.setOnItemClickListener(this);
        mMessagesListView.setOnItemLongClickListener(this);

        mMessagesAdapter.loadObjects();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_message_wall, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Message selectedMessage = mMessagesAdapter.getItem(position);
        selectedMessage.copyMessageText();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(this, "Tapped " + ((Message) mMessagesAdapter.getItem(i)).getBody(), Toast.LENGTH_SHORT).show();
//        Message selectedMessage = mMessagesAdapter.getItem(i);
        mMessageUIFragment.cancelMessageCreation();
    }

    @Override
    public void postNewMessage(String message) {
        Group grp = ParseObject.createWithoutData(Group.class, getIntent().getStringExtra("groupId"));
        Message.postNewMessage(ParseUser.getCurrentUser(), grp, message);
    }

    @Override
    public void postNewAlert(String message, ArrayList<Group> groups, AlertCategory category) {
        Message.postNewAlert(ParseUser.getCurrentUser(), groups, message, category);
    }

    public static void startMessageWallActivityForGroup (Context context, Group group){
        Intent viewGroupMessageWallIntent = new Intent(context, MessageWallActivity.class);
        viewGroupMessageWallIntent.putExtra("groupId", group.getObjectId());
        viewGroupMessageWallIntent.putExtra("groupName", group.getName());
        viewGroupMessageWallIntent.putExtra("city", group.getCity());
        viewGroupMessageWallIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(viewGroupMessageWallIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Stats.ScreenMessageWall(new Properties().putValue(GROUP_ID_EXTRA, getIntent().getStringExtra("groupId")));
    }
}
