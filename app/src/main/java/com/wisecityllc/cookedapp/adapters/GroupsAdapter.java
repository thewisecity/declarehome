package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.GroupDetailsActivity;
import com.wisecityllc.cookedapp.activities.MessageWallActivity;
import com.wisecityllc.cookedapp.parseClasses.Group;

import java.util.ArrayList;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class GroupsAdapter extends ArrayAdapter<Group>{

    private ArrayList<Group> groups;

    public GroupsAdapter(Context context, int resource, int textViewResourceId, ArrayList<Group> groups) {
        super(context, resource, textViewResourceId);
        this.groups = groups;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public int getPosition(Group item) {
        return groups.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_list_group, null);
        }

        super.getView(position, convertView, parent);

        final Group group = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.group_list_title);
        titleTextView.setText(group.getName());

        TextView purposeTextView = (TextView) convertView
                .findViewById(R.id.group_list_purpose);
        purposeTextView.setText(group.getPurpose());


        LinearLayout body = (LinearLayout) convertView
                .findViewById(R.id.group_list_item_body);

        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isMember = group.isCurrentUserMember();
                boolean isAdmin = group.isCurrentUserAdmin();

                if(isAdmin || isMember) {
                    MessageWallActivity.startMessageWallActivityForGroup(getContext(), group);
                }else{
                    GroupDetailsActivity.startGroupDetailActivity(getContext(), group);
                }
            }
        });

        return convertView;
    }


}
