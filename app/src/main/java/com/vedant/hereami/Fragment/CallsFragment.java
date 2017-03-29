package com.vedant.hereami.Fragment;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.vedant.hereami.chatfolder.chatactivity;
import com.vedant.hereami.swiperefresh.CustomSwipeRefreshLayout;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {
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
    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    //    listView = (ListView) getActivity().findViewById(R.id.listview_chatmain1);
        // listView = getListView(); //EX:
      //  listView.setTextFilterEnabled(true);
      //  registerForContextMenu(listView);
      //  super.onActivityCreated(savedInstanceState);

        Firebase.setAndroidContext(getActivity());
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        lst = new ArrayList<String>();
        final int a;
        lst.clear();
        getdata();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
       getdata();
    }



    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        listView = (ListView) view.findViewById(R.id.listview_chatmain1);
    //    mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipelayout);

    //    view = inflater.inflate(android.R.layout.list_content, null);
      //   listView = (ListView) view.findViewById(android.R.id.list);
return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calls_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

                        itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lst);


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
                        Intent intent4 = new Intent(getActivity(), chatactivity.class).putExtra("key_position", hashMap1.get(hashMap.get(lst.get(position)))).putExtra("namenumber", lst.get(position) + "");
                        startActivity(intent4);
                      //  overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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

        ContentResolver contentResolver = getActivity().getContentResolver();
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
