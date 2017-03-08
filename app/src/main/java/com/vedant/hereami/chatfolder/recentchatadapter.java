package com.vedant.hereami.chatfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vedant.hereami.R;

import java.util.List;

/**
 * Created by sunnybedi on 04/03/17.
 */

public class recentchatadapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<String> values1;

    public recentchatadapter(Context context, List<String> values, List<String> values1) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.values1 = values1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_listfrag, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text123);
        TextView textView1 = (TextView) rowView.findViewById(R.id.text1234);

        textView.setText(values.get(position));
        // change the icon for Windows and iPhone
        String s = values1.get(position);
        textView1.setText(s);

        return rowView;
    }

}
