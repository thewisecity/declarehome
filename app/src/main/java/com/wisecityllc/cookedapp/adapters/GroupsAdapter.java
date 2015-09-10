package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.parseClasses.Group;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class GroupsAdapter extends ParseQueryAdapter<Group>{

    public static final int ALL_GROUPS = 0;
    public static final int MEMBER_AND_ADMIN_ONLY = 1;
    public static final int ADMIN_ONLY = 2;

    public GroupsAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<Group>() {
            public ParseQuery<Group> create() {
                ParseQuery query = new ParseQuery("Group");
                query.orderByDescending("city");
                return query;
            }
        });
    }

    @Override
    public View getItemView(Group group, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_group, null);
        }

        super.getItemView(group, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.group_list_title);
        titleTextView.setText(group.getName());

        TextView purposeTextView = (TextView) v
                .findViewById(R.id.group_list_purpose);
        purposeTextView.setText(group.getPurpose());
        return v;
    }

}
