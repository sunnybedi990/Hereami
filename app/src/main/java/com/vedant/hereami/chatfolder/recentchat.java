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
import com.vedant.hereami.R;
import com.vedant.hereami.ViewPager.TabWOIconActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    private Map<String, Integer> countMap;
    private String temp5;
    private String todaycheck;
    private Calendar calendar1;

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

        TimeZone pdt = TimeZone.getDefault();
        calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();
        int date1 = calendar1.get(Calendar.DATE);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        todaycheck = date1 + "/" + month1 + "/" + year1;

        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
        mFirebaseMessagesChat = fb_parent.child("/message");
        currentuser = user.getEmail().replace(".", "dot") + user.getDisplayName();
        mFirebaseMessagesChatcurrent = mFirebaseMessagesChat.child(currentuser);


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
            Intent intent1 = new Intent(recentchat.this, TabWOIconActivity.class);
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
                //   String keyperson1 = "";
                // String keyperson = null;
                for (DataSnapshot currentuserchatdatasnapshot : dataSnapshot.getChildren()) {
                    if (currentuserchatdatasnapshot.getKey().contains(currentuser)) {

                        for (DataSnapshot current1 : currentuserchatdatasnapshot.getChildren()) {
                            for (DataSnapshot current : current1.getChildren()) {
                                MessageChatModel newMessage = current.getValue(MessageChatModel.class);

                                temp1 = newMessage.getMessage();
                                //  temp2 = newMessage.getRecipient();
                                temp2 = newMessage.getTimestamp();


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
                            lstmsg.add(temp1);
                            timestamp.add(temp5);


                            String keyname = String.valueOf(current1.getKey());

                            String chatuser = keyname.replace("-", "").replace(currentuser, "").replace("+", ":");
                            String[] parts = chatuser.split(":"); // escape .
                            String part1 = parts[0];
                            String part2 = parts[1];
                            String tendigitnumber = getLastThree(part2);
                            //    Log.e(">>>>>last", dataSnapshot.child(currentuser).getChildrenCount() + "");

                            hashMap1.put(tendigitnumber, keyname);


                            contactmatch = getContactDisplayNameByNumber(tendigitnumber);


                            lst.add(contactmatch);

                        }
                    }
                }

                //     Log.e(">>>>>dsp35", newList.size() + "");
                //       getNotification();
                recentchatadapter rec = new recentchatadapter(recentchat.this, lst, lstmsg, timestamp);
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
                        Intent intent4 = new Intent(recentchat.this, chatactivity.class).putExtra("key_position", hashMap1.get(hashMap.get(lst.get(position)))).putExtra("namenumber", lst.get(position) + "");
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
