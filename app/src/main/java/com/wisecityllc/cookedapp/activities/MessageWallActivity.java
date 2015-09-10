package com.wisecityllc.cookedapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.MessageWallAdapter;
import com.wisecityllc.cookedapp.fragments.PostMessageUIFragment;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.Message;

public class MessageWallActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PostMessageUIFragment.OnPostMessageUIInteractionListener {

    private MessageWallAdapter mMessagesAdapter;
    private ListView mMessagesListView;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_wall);

        getSupportActionBar().hide();

        Intent intent = getIntent();

        String groupId = intent.getStringExtra("groupId");
        mMessagesAdapter = new MessageWallAdapter(this, groupId);

        if(mMessagesListView == null){
            mMessagesListView = (ListView)findViewById(R.id.messages_list_view);
        }

        mMessagesListView.setAdapter(mMessagesAdapter);
        mMessagesListView.setOnItemClickListener(this);

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
        //TODO: Overwrite this to actually post a message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
