package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.Main;
import com.vedant.hereami.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class recentchat extends Activity {
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
    public HashMap<String, String> hashMap;
    public HashMap<String, String> hashMap1;
    public Context context;
    private String contactmatch;
    private String contactmatch1;
    private Firebase mFirebaseMessagesChatcurrent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentchat);
        setTitle("Chats");
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        Firebase.setAndroidContext(this);
        RecentUser = (ListView) findViewById(R.id.list_recent);
        lst = new ArrayList<String>();
        lstmsg = new ArrayList<String>();
        lstreceptmsg = new ArrayList<String>();
        lstmsg1 = new ArrayList<String>();
        timestamp = new ArrayList<String>();
        timestamp1 = new ArrayList<String>();
        newList = new ArrayList<String>();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
        mProgressBarForUsers = (ProgressBar) findViewById(R.id.progress_bar_users1);
        showProgressBarForUsers();
        // Bundle bundle = getIntent().getExtras();

        // message1 = bundle.getString("key_position");
        //  namenumber = bundle.getString("namenumber");
        //  setTitle(namenumber);

        // Get information from the previous activity
        Intent getUsersData = getIntent();
        UsersChatModel usersDataModel = getUsersData.getParcelableExtra(ReferenceUrl.KEY_PASS_USERS_INFO);

        // Set recipient uid


        // Set sender uid;
        mSenderUid = user.getEmail().replace(".", "dot") + user.getDisplayName();

        // Reference to recyclerView and text view
        //  mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        //  mUserMessageChatText = (TextView) findViewById(R.id.chat_user_message);

        // mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  mChatRecyclerView.setHasFixedSize(true);
        // Initialize adapter
        //  List<MessageChatModel> emptyMessageChat=new ArrayList<MessageChatModel>();
        //  mMessageChatAdapter=new MessageChatAdapter(emptyMessageChat);

        // Set adapter to recyclerView
        //  mChatRecyclerView.setAdapter(mMessageChatAdapter);

        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
        mFirebaseMessagesChat = fb_parent.child("/message");
        mFirebaseMessagesChatcurrent = mFirebaseMessagesChat.child("/" + currentuser);

        currentuser = user.getEmail().replace(".", "dot") + user.getDisplayName();
        chats();
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

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = number;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chat) {
            LocationFound();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void LocationFound() {
        Intent intent = new Intent(this, chatmain.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent(recentchat.this, Main.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        return super.onKeyDown(keycode, event);
    }

    private void showProgressBarForUsers() {
        mProgressBarForUsers.setVisibility(View.VISIBLE);
    }


    private void hideProgressBarForUsers() {
        if (mProgressBarForUsers.getVisibility() == View.VISIBLE) {
            mProgressBarForUsers.setVisibility(View.GONE);
        }
    }

    public void chats() {
        mFirebaseMessagesChat.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String temp1 = "";
                String temp2 = "";
                String temp3 = "";
                String temp4 = "";
                String keyperson1 = "";
                String keyperson = null;
                for (DataSnapshot currentuserchatdatasnapshot : dataSnapshot.child(currentuser).getChildren()) {


                    for (DataSnapshot current : currentuserchatdatasnapshot.getChildren()) {
                        MessageChatModel newMessage = current.getValue(MessageChatModel.class);
                        temp1 = newMessage.getMessage();
                        //  temp2 = newMessage.getRecipient();
                        temp2 = newMessage.getTimestamp();

                    }

                    lstmsg.add(temp1);
                    timestamp.add(temp2);
                    for (DataSnapshot currentreceipent : dataSnapshot.getChildren()) {

                        //  if (!currentreceipent.toString().equals(currentuser)) {
                        //         keyperson = String.valueOf(currentreceipent.getKey());


                        //       sunn = keyperson.length();
                        //         lst.add(keyperson);


                        if (!String.valueOf(currentreceipent.getKey()).contains(currentuser)) {
                            keyperson = currentreceipent.getKey().trim();
                            countone = (currentreceipent.getChildrenCount());
                            Log.e("temp312345", currentreceipent.getKey());
                            for (DataSnapshot currentreceipentcild2 : currentreceipent.child(currentuser).getChildren()) {
                                //        Log.e("tempnonono", String.valueOf(currentreceipentcild2.getKey().contains(currentuser)));
                                //      Log.e("ye dekh", String.valueOf(currentreceipentcild2.getValue()));


                                MessageChatModel newMessage = currentreceipentcild2.getValue(MessageChatModel.class);
                                temp3 = newMessage.getMessage().trim();
                                //  temp4 = newMessage.getRecipient();
                                temp4 = newMessage.getTimestamp();


                            }
                            Log.e("msgs34", temp3);
                            lstmsg1.add(temp3);
                            timestamp1.add(temp4);

                        }
                        //                 if (!keyperson.equals(currentuser)) {

                        if (keyperson != null) {
                            Log.e("temp3123111", keyperson);
                            //   keyperson = "";
                            for (DataSnapshot findit : dataSnapshot.child(keyperson).getChildren()) {
                                if (findit.toString().contains(currentuser)) {
                                    Log.e("temp3123", findit.toString());
                                    keyperson1 = keyperson.replace("+", ":");
                                    Log.e("keyperson1", keyperson1);
                                    String[] parts = keyperson1.split(":"); // escape .
                                    String part1 = parts[0];
                                    String part2 = parts[1];
                                    String tendigitnumber = getLastThree(part2);
                                    contactmatch1 = getContactDisplayNameByNumber(tendigitnumber);
                                    Log.e("keyperson2", contactmatch1);
                                    hashMap1.put(tendigitnumber, keyperson1.replace(":", "+"));
                                    sunn = countone - 1;
                                    //  Log.e("temp312345cou", countone + "");
                                    Log.e("temp312345cou", countone + "");
                                    //
                                }
                                if (findit.toString().contains(currentuser)) {
                                    if (lstreceptmsg.size() > sunn) {
                                        lstreceptmsg.clear();
                                        lstreceptmsg.add(contactmatch1);
                                    } else {


                                        Log.e("msgs1", contactmatch1);
                                        lstreceptmsg.add(contactmatch1);

                                        //


                                    }
                                }
                            }
                            Log.e(">>>>>dsp7", lstreceptmsg.toString());
                        }
                        Log.e(">>>>>dsp9", lstreceptmsg.toString());

                    }
                    Log.e(">>>>>dsp10", lstreceptmsg.toString());


                    Log.e(">>>>>dsp11", lstreceptmsg.toString());


                    //   Log.e("temp312345cou", countone + "");
                    //  Log.e("temp31111", keyperson1);
                    Log.e("temp3000", temp1);


                    newList1 = new ArrayList<String>(lstmsg);
                    newList1.addAll(lstmsg1);
                    timestamplist = new ArrayList<String>(timestamp);
                    timestamplist.addAll(timestamp1);

                    String keyname = String.valueOf(currentuserchatdatasnapshot.getKey()).replace("+", ":");


                    String[] parts = keyname.split(":"); // escape .
                    String part1 = parts[0];
                    String part2 = parts[1];
                    String tendigitnumber = getLastThree(part2);
                    //    Log.e(">>>>>last", dataSnapshot.child(currentuser).getChildrenCount() + "");

                    hashMap1.put(tendigitnumber, keyname.replace(":", "+"));

                    long sun1 = dataSnapshot.child(currentuser).getChildrenCount();
                    sun = dataSnapshot.child(currentuser).getChildrenCount() + sunn;
                    Log.e(">>>>>dsp1", sun + "");

                    contactmatch = getContactDisplayNameByNumber(tendigitnumber);
                    Log.e(">>>>>dsp49", lst.size() + "");
                    if (lst.size() > sun1) {
                        Log.e(">>>>>dsp50", lst.size() + "");
                        lst.clear();
                    } else {


                        lst.add(contactmatch);
                        Log.e(">>>>>dsp51", lst.size() + "");


                    }
                }
                Log.e("msgs2", lst.toString());
                //       newList.clear();
                //       lst.add(currentuserchatdatasnapshot.getKey());


                //    Log.e(">>>>>last", currentuserchatdatasnapshot.getChildren() + "");
                //    Log.e(">>>>>last123", currentuserchatdatasnapshot.getKey() + "");
                //    Log.e(">>>>>dsp", currentuserchatdatasnapshot + "");
                //   newList.addAll(lst);
                //   newList.addAll(lstreceptmsg);

                if (newList.size() > sun) {
                    Log.e(">>>>>dsp5", newList.size() + "");
                    newList.clear();
                    //   newList.addAll(lst);
                    //       Log.e(">>>>>dsp32", newList.size() + "");
                    //  newList.addAll(lstreceptmsg);
                } else {
                    //   newList.clear();
                    //         Log.e(">>>>>dsp31", newList.size() + "");
                    newList.addAll(lst);
                    //       Log.e(">>>>>dsp32", newList.size() + "");
                    Log.e(">>>>>dsp33", lstreceptmsg.toString());
                    Log.e(">>>>>dsp33", lstreceptmsg.toString());
                    newList.addAll(lstreceptmsg);
                    Log.e(">>>>>dsp33", lstreceptmsg.toString());
                    //  Log.e(">>>>>dsp4", newList.toString() + "");
                    Log.e(">>>>>dsp34", newList.size() + "");

                }

                Log.e(">>>>>dsp35", newList.size() + "");
                //       getNotification();
                recentchatadapter rec = new recentchatadapter(recentchat.this, newList, newList1, timestamplist);
                //  RecentmessagesAdapter = new ArrayAdapter<String>(recentchat.this,android.R.layout.simple_list_item_1,android.R.id.text1);
                RecentUser.setAdapter(rec);

                rec.notifyDataSetChanged();
                // RecentUser.setSelection(RecentmessagesAdapter.getCount() - 1);
                hideProgressBarForUsers();


                RecentUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //TextView t1 = (TextView) findViewById(android.R.id.text1);
//                            Log.e(">>>>asd", lst.get(position) + "");

                        //                          Log.e(">>>>>NAME_NUMBER", hashMap.get(lst.get(position)) + "");

                        //                        Log.e(">>>>>NUMBER_KEY", hashMap1.get(hashMap.get(lst.get(position))) + "");
                        Intent intent4 = new Intent(recentchat.this, chatactivity.class).putExtra("key_position", hashMap1.get(hashMap.get(newList.get(position)))).putExtra("namenumber", newList.get(position) + "");
                        startActivity(intent4);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                });

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                noti = new Notification.Builder(this)
                        .setContentTitle(newList.toString())
                        .setContentText(newList1.toString()).setSmallIcon(R.drawable.image)
                        .setContentIntent(pIntent)
                        .addAction(R.drawable.image, "Call", pIntent)
                        .addAction(R.drawable.image, "More", pIntent)
                        .addAction(R.drawable.image, "And more", pIntent).build();
            }
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }
}
