package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.delegates.GroupsCheckboxAdapterDelegate;
import com.wisecityllc.cookedapp.parseClasses.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class GroupsCheckboxAdapter extends ParseQueryAdapter<Group>{

    GroupsCheckboxAdapterDelegate mDelegate;

    public GroupsCheckboxAdapter(Context context, GroupsCheckboxAdapterDelegate delegate) {
        super(context, new QueryFactory<Group>() {
            public ParseQuery<Group> create() {

                ParseQuery adminOfQuery = ParseUser.getCurrentUser().getRelation("adminOf").getQuery();
                ParseQuery memberOfQuery = ParseUser.getCurrentUser().getRelation("memberOf").getQuery();

                List<ParseQuery<Group>> queries = new ArrayList<ParseQuery<Group>>();
                queries.add(adminOfQuery);
                queries.add(memberOfQuery);

                ParseQuery query = (ParseQuery<Group>) ParseQuery.or(queries);

                query.orderByDescending("city");

                return query;
            }
        });

        mDelegate = delegate;

    }

    @Override
    public View getItemView(final Group group, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_group_checkbox, null);
        }

        super.getItemView(group, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.group_list_title);
        titleTextView.setText(group.getName());

        final CheckBox checkbox = (CheckBox) v.findViewById(R.id.group_list_item_checkbox);


        LinearLayout body = (LinearLayout) v
                .findViewById(R.id.group_list_item_body);


        //If we touch the body, check or uncheck the checkbox by setting it to what it is not ~!~!~WOAH MAN~!~!~
        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkbox.setChecked(!checkbox.isChecked());
                if (checkbox.isChecked()) {
                    mDelegate.groupChecked(group);
                } else {
                    mDelegate.groupUnchecked(group);
                }
            }
        });

        boolean isChecked = mDelegate.isGroupChecked(group);
        checkbox.setChecked(isChecked);

        return v;
    }

}
