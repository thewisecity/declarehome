package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.AlertCategory;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.Message;
import com.wisecityllc.cookedapp.views.ClickableUserPortrait;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class AlertWallAdapter extends ParseQueryAdapter<Message>{

    public AlertWallAdapter(Context context) {

        super(context, new QueryFactory<Message>() {
            public ParseQuery<Message> create() {
                ParseQuery query = new ParseQuery("Message");
                query.orderByDescending("createdAt");


                query.whereEqualTo("isAlert", true);
                ParseQuery adminOfQuery = ParseUser.getCurrentUser().getRelation("adminOf").getQuery();
                ParseQuery memberOfQuery = ParseUser.getCurrentUser().getRelation("memberOf").getQuery();

                List<ParseQuery<Group>> queries = new ArrayList<ParseQuery<Group>>();
                queries.add(adminOfQuery);
                queries.add(memberOfQuery);

                //A query of all the groups of which currentUser is an admin or member
                ParseQuery< Group> groupsQuery = (ParseQuery<Group>) ParseQuery.or(queries);


                query.whereMatchesQuery("groups", groupsQuery);


//              ParseObject groupProxy = ParseObject.createWithoutData("Group", groupId);
//                query.whereEqualTo("group", groupProxy);

                query.include(Message._CATEGORY);
                query.include(Message._AUTHOR);
                return query;
            }
        });

        addOnQueryLoadListener(new OnQueryLoadListener<Message>() {
            @Override
            public void onLoading() {
                Log.d("Dex", "Loading");
            }

            @Override
            public void onLoaded(List<Message> objects, Exception e) {

            }
        });
    }

    @Override
    public View getItemView(Message message, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_message, null);
        }

        super.getItemView(message, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.message_item_author_name);
        titleTextView.setText(message.getAuthor().getString("displayName"));




        TextView bodyTextView = (TextView) v
                .findViewById(R.id.message_item_body_text);

        AlertCategory alertCategory = message.getCategory();
        if(alertCategory != null) {
            Spannable modifiedText = new SpannableString(alertCategory.getTitle() + " " + message.getBody());
            modifiedText.setSpan(new ForegroundColorSpan(Color.rgb(alertCategory.getTextColorR().intValue(), alertCategory.getTextColorG().intValue(), alertCategory.getTextColorB().intValue())), 0, alertCategory.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            modifiedText.setSpan(new BackgroundColorSpan(Color.rgb(80, 80, 80)), 0, alertCategory.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            bodyTextView.setText(modifiedText);
        }else{
            bodyTextView.setText(message.getBody());
        }


        ClickableUserPortrait authorImage = (ClickableUserPortrait) v.findViewById(R.id.message_item_author_image);
        authorImage.setUser(message.getAuthor(), true);


        return v;
    }

}
