package com.vedant.hereami.firebasepushnotification;

/**
 * Created by bedi on 3/25/2017.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vedant.hereami.R;

/**
 * Created by Belal on 9/22/2015.
 */

public class CustomList extends ArrayAdapter<String> {
    private String[] ids;
    private String[] names;
    private String[] emails;
    private Activity context;

    public CustomList(Activity context, String[] ids, String[] names, String[] emails) {
        super(context, R.layout.list_view_layout, ids);
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.emails = emails;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_layout, parent, false);
            TextView textViewId = (TextView) convertView.findViewById(R.id.textViewId);
            TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            TextView textViewEmail = (TextView) convertView.findViewById(R.id.textViewEmail);

            textViewId.setText(ids[position]);
            textViewName.setText(names[position]);
            textViewEmail.setText(emails[position]);
        }
        return convertView;
    }
}