package com.vedant.hereami;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnRefreshListener {
    public List<String> lst;
    public ListView listView;
    public String[] stockArr;
    public HashMap<String, String> hashMap;
    public HashMap<String, String> hashMap1;
    public Firebase fb_to_read;
    private SwipeRefreshLayout swipeLayout;
    private CustomSwipeRefreshLayout mCustomSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listview1);
        mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipelayout);

        mCustomSwipeRefreshLayout.setOnRefreshListener(ListActivity.this);
        mCustomSwipeRefreshLayout.setCustomHeadview(new MyCustomHeadView(this));
        // swipeLayout.setProgressBackgroundColor(android.R.color.transparent);
        Firebase.setAndroidContext(this);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        lst = new ArrayList<String>();
        final int a;
        lst.clear();
        getdata();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();

        // FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        //FirebaseApp app = FirebaseApp.initializeApp(this, options);


        //Log.e(">>>>>List size", lst.size() + "");

//        Log.e(">>>>>stock size", stockArr.length + "");

        //Log.e(">>>>>List ", stockArr[0] + "");
//            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,lst);
//
        //          listView.setAdapter(itemsAdapter);
//
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lst.clear();
                getdata();
                mCustomSwipeRefreshLayout.setRefreshing(false);
                Log.e(">>>>>List Valueeeeeeee", "");

            }

        }, 5000);
    }

    public void getdata() {

        fb_to_read.addValueEventListener(new ValueEventListener() {


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void onDataChange(DataSnapshot result) {
                // Result will be holded Here

                for (DataSnapshot dsp : result.getChildren()) {
                    String keyname = String.valueOf(dsp.getKey()).replace("+", ":");

                    String[] parts = keyname.split(":"); // escape .
                    String part1 = parts[0];
                    String part2 = parts[1];
                    String tendigitnumber = getLastThree(part2);

                    hashMap1.put(tendigitnumber, keyname.replace(":", "+"));

                    long sun = result.getChildrenCount();
                    if (lst.size() > sun) {
                        lst.clear();
                    } else {

                        String contactmatch = getContactDisplayNameByNumber(tendigitnumber);
                        if (!contactmatch.equals("?")) {
                            lst.add(contactmatch);
                        }
                        //add result into array list


                        Log.e(">>>>>List Value", lst.size() + "");

                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, lst);
                        final Collator col = Collator.getInstance();
                        itemsAdapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return col.compare(lhs, rhs);
                            }
                        });


                        itemsAdapter.notifyDataSetChanged();


                        listView.setAdapter(itemsAdapter);
                        Log.e(">>>>asdd", lst + "");


                    }

                }


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //TextView t1 = (TextView) findViewById(android.R.id.text1);
                        Log.e(">>>>asd", lst.get(position) + "");

                        Log.e(">>>>>NAME_NUMBER", hashMap.get(lst.get(position)) + "");

                        Log.e(">>>>>NUMBER_KEY", hashMap1.get(hashMap.get(lst.get(position))) + "");
                        Intent intent4 = new Intent(ListActivity.this, TestActivity.class).putExtra("key_position", hashMap1.get(hashMap.get(lst.get(position)))).putExtra("namenumber", lst.get(position) + "");
                        startActivity(intent4);
                    }
                });


            }


        });
    }
    public static String getLastThree(String myString) {
        if (myString.length() > 10)
            return myString.substring(myString.length() - 10);
        else
            return myString;
    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                hashMap.put(name, number);
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }

}


