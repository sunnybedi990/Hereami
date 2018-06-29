package com.vedant.hereami.chatfolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vedant.hereami.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sunnybedi on 04/03/17.
 */

public class recentchatadapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<String> values1;
    private final List<String> values2;
    protected String encryptedkey;
    private String s;
    private final String todaycheck;
    TimeZone pdt = TimeZone.getTimeZone("UTC");


    public recentchatadapter(Context context, List<String> values, List<String> values1, List<String> values2) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.values1 = values1;
        this.values2 = values2;
        Calendar calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();
        int date1 = calendar1.get(Calendar.DATE);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        todaycheck = date1 + "/" + month1 + "/" + year1;

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
            holder.textView = convertView.findViewById(R.id.text123);
            holder.textView1 = convertView.findViewById(R.id.text1234);
            holder.textViewtimestamp = convertView.findViewById(R.id.text12345);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(values.get(position));
        // change the icon for Windows and iPhone


        s = values1.get(position);

        holder.textView1.setText(s);

        String time = values2.get(position);
        String time1 = "";
        if (time != null) {

            String[] parts1 = time.split("%");
            String part4 = parts1[0];
            String part5 = parts1[1];
            String timecheck = null;
            String dateset = null;

            SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            //    SimpleDateFormat df1 = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

            try {
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = df.parse(time.replace("%", " "));
                df1.setTimeZone(TimeZone.getDefault());
                timecheck = df1.format(date);
                df2.setTimeZone(TimeZone.getDefault());
                dateset = df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!part5.equals(todaycheck)) {
                //   time1 = part5;
                time1 = dateset;
            } else {
                //   time1 = part4;
                time1 = timecheck;
            }

        }
        holder.textViewtimestamp.setText(time1);


        return convertView;
    }

}
