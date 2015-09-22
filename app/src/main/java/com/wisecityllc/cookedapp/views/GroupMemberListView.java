package com.wisecityllc.cookedapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.wisecityllc.cookedapp.App;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.GroupMemberListAdapter;
import com.wisecityllc.cookedapp.parseClasses.Group;

/**
 * TODO: document your custom view class.
 */
public class GroupMemberListView extends ListView {

    private boolean mIncludeMembers;
    private boolean mIncludeAdmins;
    private boolean mIncludePendingNeedsApproval;
    private boolean mIncludePendingNeedsToAccept;

    private Group mGroup;

    private GroupMemberListAdapter mAdapter;

    public GroupMemberListView(Context context) {
        super(context);
    }

    public GroupMemberListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public GroupMemberListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GroupMemberListView,
                0, 0);

        try {
            mIncludeMembers = a.getBoolean(R.styleable.GroupMemberListView_includeMembers, false);
            mIncludeAdmins = a.getBoolean(R.styleable.GroupMemberListView_includeAdmins, false);
            mIncludePendingNeedsApproval = a.getBoolean(R.styleable.GroupMemberListView_includePendingAwaitingApproval, false);
            mIncludePendingNeedsToAccept = a.getBoolean(R.styleable.GroupMemberListView_includePendingInvitationSent, false);
        } finally {
            a.recycle();
        }


    }

    public void setGroup(Group group) {
        mGroup = group;

        updateAdapter();

    }

    private void updateAdapter() {
        mAdapter =
                new GroupMemberListAdapter(
                        App.getContext(),
                        mGroup,
                        mIncludeMembers,
                        mIncludeAdmins,
                        mIncludePendingNeedsApproval,
                        mIncludePendingNeedsToAccept);

        setAdapter(mAdapter);

        mAdapter.loadObjects();

    }

}
