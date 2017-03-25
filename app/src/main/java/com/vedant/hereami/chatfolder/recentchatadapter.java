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
    private final List<String> values2;

    public recentchatadapter(Context context, List<String> values, List<String> values1, List<String> values2) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.values1 = values1;
        this.values2 = values2;
    }

    private class ViewHolder {

        TextView textView;
        TextView textView1;
        TextView textViewtimestamp;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_listfrag, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text123);
            holder.textView1 = (TextView) convertView.findViewById(R.id.text1234);
            holder.textViewtimestamp = (TextView) convertView.findViewById(R.id.text12345);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(values.get(position));
        // change the icon for Windows and iPhone
        String s = values1.get(position);
        holder.textView1.setText(s);
        String time = values2.get(position);
        holder.textViewtimestamp.setText(time);

        return convertView;
    }

}
