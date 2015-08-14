package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.Message;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class MessageWallAdapter extends ParseQueryAdapter<Message>{

    public MessageWallAdapter(Context context, final String groupId) {
        super(context, new ParseQueryAdapter.QueryFactory<Message>() {
            public ParseQuery<Message> create() {
                ParseQuery query = new ParseQuery("Message");
                query.orderByDescending("createdAt");

                ParseObject groupProxy = ParseObject.createWithoutData("Group", groupId);
                query.whereEqualTo("group", groupProxy);
                return query;
            }
        });
    }


    @Override
    public View getItemView(Message message, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_group, null);
        }

        super.getItemView(message, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.group_list_title);
        titleTextView.setText(message.getAuthor().getUsername());

        TextView purposeTextView = (TextView) v
                .findViewById(R.id.group_list_purpose);
        purposeTextView.setText(message.getBody());
        return v;
    }

}
