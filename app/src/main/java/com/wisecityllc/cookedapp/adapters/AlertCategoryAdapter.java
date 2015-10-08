package com.wisecityllc.cookedapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wisecityllc.cookedapp.R;
import com.wisecityllc.cookedapp.adapters.delegates.AlertCategoryAdapterDelegate;
import com.wisecityllc.cookedapp.parseClasses.AlertCategory;

/**
 * Created by dexterlohnes on 10/5/15.
 */
public class AlertCategoryAdapter extends ParseQueryAdapter<AlertCategory>{

    AlertCategoryAdapterDelegate mDelegate;

    public AlertCategoryAdapter(Context context, AlertCategoryAdapterDelegate delegate) {

        super(context, new ParseQueryAdapter.QueryFactory<AlertCategory>() {
            public ParseQuery<AlertCategory> create() {

                ParseQuery query = new ParseQuery("AlertCategory");

                return query;
            }
        });


        mDelegate = delegate;
    }

    @Override
    public View getItemView(final AlertCategory category, View v, ViewGroup parent) {

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_list_alert_category, null);
        }

        super.getItemView(category, v, parent);

        //TODO: Color according to colors retrieved
        TextView titleTextView = (TextView) v.findViewById(R.id.alert_category_title);
        titleTextView.setText(category.getTitle());

        LinearLayout body = (LinearLayout) v
                .findViewById(R.id.alert_category_list_item_body);

        body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDelegate.categoryTapped(category);
            }
        });


        return v;
    }
}
