package com.vedant.hereami.tracking;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vedant.hereami.R;
import com.vedant.hereami.swiperefresh.CustomSwipeRefreshLayout;
import com.vedant.hereami.swiperefresh.MyCustomHeadView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CustomSwipeRefreshLayout.OnRefreshListener {
    public List<String> lst;
    public ListView listView;
    public String[] stockArr;
    public HashMap<String, String> hashMap;
    public HashMap<String, String> hashMap1;
    public Firebase fb_to_read;
    private SwipeRefreshLayout swipeLayout;
    private CustomSwipeRefreshLayout mCustomSwipeRefreshLayout;
    private ArrayAdapter<String> itemsAdapter;
    private View row;
    private TextView tv;
    private SpannableString wordtoSpan;
    private String contactmatch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Users");
        listView = (ListView) findViewById(R.id.listview1);
        mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.swipelayout);
        mCustomSwipeRefreshLayout.setOnRefreshListener(ListActivity.this);
        mCustomSwipeRefreshLayout.setCustomHeadview(new MyCustomHeadView(this));
        mCustomSwipeRefreshLayout.setProgressBarColorRes();

        // swipeLayout.setProgressBackgroundColor(android.R.color.transparent);
        Firebase.setAndroidContext(this);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        fb_to_read.addValueEventListener(new ValueEventListener() {


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void onDataChange(DataSnapshot result) {
                lst = new ArrayList<String>(); // Result will be holded Here
                for (DataSnapshot dsp : result.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey())); //add result into array list
                    //    stockArr = new String[lst.size()];
                    //  stockArr = lst.toArray(stockArr);
                }
            }


        });
        lst = new ArrayList<String>();
        final int a;
        lst.clear();
        getdata();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();


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

        fb_to_read.addListenerForSingleValueEvent(new ValueEventListener() {


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

                        contactmatch = getContactDisplayNameByNumber(tendigitnumber);
                        if (!contactmatch.equals("?")) {
                            lst.add(contactmatch);
                        }
                        //add result into array list


                        Log.e(">>>>>List Value", lst.size() + "");

                        itemsAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, lst);
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
                        if (wordtoSpan != null) {
                            tv.setText(wordtoSpan);
                        }
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
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_list, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                if (itemsAdapter != null) {
                    itemsAdapter.getFilter().filter(newText);

                    itemsAdapter.notifyDataSetChanged();
            /*        newText = newText.toLowerCase(Locale.getDefault());
                    lst.clear();
                    if (newText.length() == 0) {
                        lst.add(contactmatch);
                    }
                    else
                    {
                        for ( String wp : lst)
                        {
                            if (wp.toLowerCase(Locale.getDefault()).contains(newText))
                            {
                                lst.add(wp);
                            }
                        }
                    }

            */
                    System.out.println("on text chnge text: " + newText);
                    //        int i = lst.toString().toLowerCase(Locale.US).indexOf(newText.toLowerCase(Locale.US));
                    //        int endpos = i + newText.length();
                    //        if (i != -1) {
                    //             wordtoSpan = new SpannableString(itemsAdapter.toString());

                    //             wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), i, endpos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //           tv.setText(wordtoSpan);
                    //  if (tv.getText().toString().contains(newText)) {
                    //      tv.setTextColor(Color.YELLOW);

//                    tv.textc
                    //         }
                }
                return true;

            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
                itemsAdapter.getFilter().filter(query);
                System.out.println("on query submit: " + query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        // changeSearchViewTextColor(row);
        return super.onCreateOptionsMenu(menu);

    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.YELLOW);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
}


