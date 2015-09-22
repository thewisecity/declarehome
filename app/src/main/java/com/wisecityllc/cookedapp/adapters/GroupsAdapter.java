package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.GroupDetailsActivity;
import com.wisecityllc.cookedapp.activities.MessageWallActivity;
import com.wisecityllc.cookedapp.parseClasses.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class GroupsAdapter extends ParseQueryAdapter<Group>{

    public static final int ALL_GROUPS = 0;
    public static final int MEMBER_AND_ADMIN_ONLY = 1;
    public static final int ADMIN_ONLY = 2;

    private int mMode;

    public GroupsAdapter(Context context, final int mode) {
        super(context, new ParseQueryAdapter.QueryFactory<Group>() {
            public ParseQuery<Group> create() {

                ParseQuery query = null;

                if (mode == ALL_GROUPS){
                    query = new ParseQuery("Group");

                } else if (mode == MEMBER_AND_ADMIN_ONLY) {
                    ParseQuery adminOfQuery = ParseUser.getCurrentUser().getRelation("adminOf").getQuery();
                    ParseQuery memberOfQuery = ParseUser.getCurrentUser().getRelation("memberOf").getQuery();

                    List<ParseQuery<Group>> queries = new ArrayList<ParseQuery<Group>>();
                    queries.add(adminOfQuery);
                    queries.add(memberOfQuery);

                    query = (ParseQuery<Group>) ParseQuery.or(queries);
                } else if (mode == ADMIN_ONLY) {
                    query = ParseUser.getCurrentUser().getRelation("adminOf").getQuery();
                }

                if (query != null) {
                    query.orderByDescending("city");
                }
                return query;
            }
        });
        mMode = mode;
    }

    @Override
    public View getItemView(final Group group, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_group, null);
        }

        super.getItemView(group, v, parent);

        TextView titleTextView = (TextView) v.findViewById(R.id.group_list_title);
        titleTextView.setText(group.getName());

        TextView purposeTextView = (TextView) v
                .findViewById(R.id.group_list_purpose);
        purposeTextView.setText(group.getPurpose());


        LinearLayout body = (LinearLayout) v
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


        // We only want to show this button if we're in ALL mode and we are a member or admin

        Button detailsButton = (Button) v
                .findViewById(R.id.group_list_item_details_button);
        if(mMode == ALL_GROUPS && (group.isCurrentUserMember() || group.isCurrentUserAdmin())){

            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupDetailsActivity.startGroupDetailActivity(getContext(), group);
                }
            });
            detailsButton.setVisibility(View.VISIBLE);
        } else {
            // Sometimes we are recycling an old view so we ALWAYS want to remove the details button or we get funky beavior
            detailsButton.setVisibility(View.GONE);
        }


        return v;
    }

}
