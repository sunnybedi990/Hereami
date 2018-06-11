package com.vedant.hereami.Fragment;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;
import com.vedant.hereami.chatfolder.chatmain;
import com.vedant.hereami.chatfolder.recentchatadapter;
import com.vedant.hereami.chatfolder.viewcurrentuserprofile;
import com.vedant.hereami.database.DBHelper;
import com.vedant.hereami.database.message;
import com.vedant.hereami.database.messagedatabse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class ChatFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS = 5;
    private String mSenderUid;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ListView RecentUser;
    private Firebase mFirebaseMessagesChat;
    private String currentuser;
    private List<String> lst;
    private List<String> lstmsg;
    private List<String> lstreceptmsg;
    private ArrayAdapter<String> RecentmessagesAdapter;
    private TextView tv;
    private TextView tm;
    private HashMap<String, String> hashMap;
    private HashMap<String, String> hashMap1;
    private HashMap<String, String> hashMap2;
    private HashMap<String, String> hashMap3;
    private HashMap<String, String> hashMap4;
    public Context context;
    private String contactmatch;
    private String contactmatch1;
    private com.firebase.client.Query mFirebaseMessagesChatcurrent;
    private long sunn;
    private ProgressBar mProgressBarForUsers;
    private List<String> lstmsg1;
    private List<String> newList;
    private long countone;
    private List<String> newList1;
    private List<String> timestamp;
    private List<String> timestamp1;
    private long sun;
    private List<String> timestamplist;
    private Map<String, Integer> countMap;
    private Calendar calendar1;
    private String temp5;
    private String todaycheck;
    private recentchatadapter rec;
    public static final String mypreference123 = "mypref123";

    private Firebase mFirebaseMessagesChatconnectioncheck;
    private String username;
    private String publickey;
    private SharedPreferences sharedpreferences1;
    private String temp6;
    private String myencryptionkey;
    private String tendigitnumber;
    private DBHelper mydb;
    private Firebase fb_to_read;
    private List<String> lst1;
    private String tendigitnumber1;
    private ListView obj;
    private messagedatabse mydb1;
    private List<String> tablenames;


    public ChatFragment() {

    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Firebase.setAndroidContext(Objects.requireNonNull(getActivity()));
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        context = getContext();


        TimeZone pdt = TimeZone.getDefault();
        calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();
        int date1 = calendar1.get(Calendar.DATE);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        todaycheck = date1 + "/" + month1 + "/" + year1;


        mydb = new DBHelper(getActivity());
        mydb1 = new messagedatabse(getActivity());

        tablenames = new ArrayList<>();
        tablenames = mydb1.getalltable();
        Log.e("table", tablenames.toString());


        //  showmessages();

        //     chats();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        obj = view.findViewById(R.id.list_chat_fragment);
        showmessages();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chat) {
            //   LocationFound();
            Intent intent = new Intent(getActivity(), chatmain.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.setting) {
            Intent intent = new Intent(getActivity(), viewcurrentuserprofile.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static String getLastThree(String myString) {
        if (myString.length() > 10)
            return myString.substring(myString.length() - 10);
        else
            return myString;
    }

    public static String getfirstten(String myString1) {
        if (myString1.length() > 10)
            return myString1.substring((myString1.length() + 10) - myString1.length());
        else
            return myString1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = number;

        ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
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


    public void showmessages() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> array_list = mydb.getAllCotacts();
                ArrayList<String> array_list1 = mydb.getAllmsgs();
                ArrayList<String> array_list2 = mydb.getAlltimestamp();
                Log.e("array1", array_list.toString());
                Log.e("array2", array_list1.toString());
                Log.e("array3", array_list2.toString());

                rec = new recentchatadapter(getActivity(), array_list, array_list1, array_list2);

                obj.setAdapter(rec);

                rec.notifyDataSetChanged();

                obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {     // TODO Auto-generated method stub
                        Bundle dataBundle = new Bundle();
                        dataBundle.putString("tablename", "table" + mydb.getAllphone().get(position));
                        dataBundle.putString("name", array_list.get(position));
                        dataBundle.putString("number", mydb.getAllphone().get(position));
                        dataBundle.putString("username", mydb.getAllkey().get(position));
                        Intent intent = new Intent(context, message.class);

                        intent.putExtras(dataBundle);
                        startActivity(intent);
/*
                Intent intent4 = new Intent(saverecentmessage.this, chatactivity.class).putExtra("key_position", mydb.getAllkey().get(position)).putExtra("namenumber", array_list.get(position) + "").putExtra("number", mydb.getAllphone().get(position));
                startActivity(intent4);
  */
                    }
                });

            }
        };
        Thread t = new Thread(r);
        t.start();

    }


}







/*
    public void chats() {
        mFirebaseMessagesChat.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String temp1 = "";
                String temp2 = "";
                String temp3 = "";
                String temp4 = "";

                for (DataSnapshot currentuserchatdatasnapshot : dataSnapshot.getChildren()) {
                    if (currentuserchatdatasnapshot.getKey().contains(currentuser)) {
                        key();
                        //  Log.e(">>>>>last", currentuserchatdatasnapshot.toString() + "");

                        for (DataSnapshot current1 : currentuserchatdatasnapshot.getChildren()) {
                            //  Log.e(">>>>>last123", current.toString() + "");
                            for (DataSnapshot current : current1.getChildren()) {

                                MessageChatModel newMessage = current.getValue(MessageChatModel.class);
                                temp3 = newMessage.getSender();
                                temp1 = newMessage.getMessage1();
                                temp4 = newMessage.getMessage();
                                //  temp2 = newMessage.getRecipient();
                                temp2 = newMessage.getTimestamp();
                                countone = currentuserchatdatasnapshot.getChildrenCount();
                                //       Log.e("countone", String.valueOf(countone));
                                //      Log.e("countonecur", String.valueOf(currentuserchatdatasnapshot));
                                //      Log.e("countonecur1", String.valueOf(current1));

                            }
                            Log.e(">>>>>last", temp1 + "");
                            String[] parts1 = temp2.split("%");
                            String part4 = parts1[0];
                            String part5 = parts1[1];

                            if (!part5.equals(todaycheck)) {
                                temp5 = part5;
                            } else {
                                temp5 = part4;
                            }
                            //           Log.e("users", temp3);

                            temp6 = CallsFragment.decryptRSAToString(temp1, myencryptionkey) + CallsFragment.decryptRSAToString(temp1, publickey) + CallsFragment.decryptRSAToString(temp4, publickey) + CallsFragment.decryptRSAToString(temp4, myencryptionkey);


                            lstmsg.add(temp6);
                            timestamp.add(temp5);


                            String keyname = String.valueOf(current1.getKey());
                            username = keyname;
                            String chatuser = keyname.replace("-", "").replace(currentuser, "").replace("+", ":");
                            String[] parts = chatuser.split(":"); // escape .
                            String part1 = parts[0];
                            String part2 = parts[1];
                            tendigitnumber = getLastThree(part2);
                            //    Log.e(">>>>>last", dataSnapshot.child(currentuser).getChildrenCount() + "");

                            hashMap1.put(tendigitnumber, keyname);
                            hashMap2.put(tendigitnumber, temp6);
                            hashMap3.put(tendigitnumber, temp5);


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                contactmatch = getContactDisplayNameByNumber(tendigitnumber);
                            }
                            if (contactmatch == null) {
                                contactmatch = tendigitnumber;
                            }


                            if (lst.size() < countone) {
                                lst.add(contactmatch);
                                Log.e("data", lst.toString());
                            }

                        }
                    }
                }

                rec = new recentchatadapter(getActivity(), lst, lstmsg, timestamp);
                //  RecentmessagesAdapter = new ArrayAdapter<String>(recentchat.this,android.R.layout.simple_list_item_1,android.R.id.text1);
                RecentUser.setAdapter(rec);

                rec.notifyDataSetChanged();
                // RecentUser.setSelection(RecentmessagesAdapter.getCount() - 1);
                //  hideProgressBarForUsers();


                RecentUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //TextView t1 = (TextView) findViewById(android.R.id.text1);
//                            Log.e(">>>>asd", lst.get(position) + "");

                        //                          Log.e(">>>>>NAME_NUMBER", hashMap.get(lst.get(position)) + "");

                        //                        Log.e(">>>>>NUMBER_KEY", hashMap1.get(hashMap.get(lst.get(position))) + "");
                        Intent intent4 = new Intent(getActivity(), chatactivity.class).putExtra("key_position", hashMap1.get(hashMap.get(lst.get(position)))).putExtra("key_position3", hashMap1.get(lst.get(position))).putExtra("namenumber", lst.get(position) + "").putExtra("number", tendigitnumber);
                        startActivity(intent4);
                        // overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                });

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void key() {
        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {


            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {


                    String sunn = connectionchild.getKey();
                    //    Log.e("sunn", sunn);
                    publickey = dataSnapshot1.child(username).child("Publickey").getValue().toString();
                    //    Log.e("sunnq", publickey + "");

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
*/