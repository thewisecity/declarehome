package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.activities.GroupDetailsActivity;
import com.wisecityllc.cookedapp.activities.MessageWallActivity;
import com.wisecityllc.cookedapp.parseClasses.Group;
import com.wisecityllc.cookedapp.parseClasses.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexterlohnes on 6/30/15.
 */
public class GroupsQueryAdapter extends ParseQueryAdapter<Group>{

    public static final int ALL_GROUPS = 0;
    public static final int MEMBER_AND_ADMIN_ONLY = 1;
    public static final int ADMIN_ONLY = 2;

    private int mMode;

    public GroupsQueryAdapter(Context context, final int mode) {
        super(context, new ParseQueryAdapter.QueryFactory<Group>() {
            public ParseQuery<Group> create() {

                ParseQuery query = null;

                query = new ParseQuery("Group");

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (mode == ALL_GROUPS){
                    query = new ParseQuery("Group");

                } else if (mode == MEMBER_AND_ADMIN_ONLY) {

                    ParseRelation adminRel = currentUser.getRelation("adminOf");
                    ParseRelation memberRel = currentUser.getRelation("memberOf");

                    ParseQuery adminOfQuery = adminRel.getQuery();
                    ParseQuery memberOfQuery = memberRel.getQuery();

                    String className = adminOfQuery.getClassName();
                    String otherName = adminOfQuery.getClass().getName();

                    if(adminOfQuery.getClassName().equalsIgnoreCase("Group") == false) {
                        try {
                            currentUser.fetch();
                            adminRel = currentUser.getRelation("adminOf");
                            memberRel = currentUser.getRelation("memberOf");

                            adminOfQuery = adminRel.getQuery();
                            memberOfQuery = memberRel.getQuery();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    List<ParseQuery<Group>> queries = new ArrayList<ParseQuery<Group>>();
                    queries.add(adminOfQuery);
                    queries.add(memberOfQuery);

                    query = (ParseQuery<Group>) ParseQuery.or(queries);
                } else if (mode == ADMIN_ONLY) {
                    ParseRelation<Group> rel = currentUser.getRelation("adminOf");
                    query = rel.getQuery();
                }

                if (query != null) {
                    query.orderByDescending("city");
                    query.include(Group._MEMBERS_ARRAY);
                    query.include(Group._ADMINS_ARRAY);
                    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
                }
                return query;
            }
        });
        mMode = mode;

    }

    public GroupsQueryAdapter(Context context, final int mode, final User user) {
        super(context, new ParseQueryAdapter.QueryFactory<Group>() {
            public ParseQuery<Group> create() {

                ParseQuery query = null;

                query = new ParseQuery("Group");

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (mode == ALL_GROUPS){
                    query = new ParseQuery("Group");

                } else if (mode == MEMBER_AND_ADMIN_ONLY) {

                    ParseRelation adminRel = currentUser.getRelation("adminOf");
                    ParseRelation memberRel = currentUser.getRelation("memberOf");

                    ParseQuery adminOfQuery = adminRel.getQuery();
                    ParseQuery memberOfQuery = memberRel.getQuery();

                    if(adminOfQuery.getClassName().equalsIgnoreCase("Group") == false) {
                        try {
                            currentUser.fetch();
                            adminRel = currentUser.getRelation("adminOf");
                            memberRel = currentUser.getRelation("memberOf");

                            adminOfQuery = adminRel.getQuery();
                            memberOfQuery = memberRel.getQuery();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    List<ParseQuery<Group>> queries = new ArrayList<ParseQuery<Group>>();
                    queries.add(adminOfQuery);
                    queries.add(memberOfQuery);

                    query = (ParseQuery<Group>) ParseQuery.or(queries);
                } else if (mode == ADMIN_ONLY) {
                    query = ParseUser.getCurrentUser().getRelation("adminOf").getQuery();
                }

                if (query != null) {
                    query.orderByDescending("city");
                    query.include(Group._MEMBERS_ARRAY);
                    query.include(Group._ADMINS_ARRAY);
                    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
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
