package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
    private Firebase mFirebaseMessagesChatcurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentchat);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        Firebase.setAndroidContext(this);
        RecentUser = (ListView) findViewById(R.id.list_recent);
        lst = new ArrayList<String>();
        lstmsg = new ArrayList<String>();
        lstreceptmsg = new ArrayList<String>();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
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
        mFirebaseMessagesChat.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String temp1 = "";
                String temp2 = "";
                String temp3 = "";
                String temp4 = "";
                String keyperson1 = "";
                String keyperson = "";
                for (DataSnapshot sunny : dataSnapshot.child(currentuser).getChildren()) {


                    for (DataSnapshot current : sunny.getChildren()) {
                        MessageChatModel newMessage = current.getValue(MessageChatModel.class);
                        temp1 = newMessage.getMessage();
                        temp2 = newMessage.getRecipient();
                        //while(temp1)
                        // Log.e(">>>>>>",temp1);
                        // Log.e(">>>>>last1111", newMessage.getMessage() + "");
                        //    Message todo1 = new Message();
                        //Message todo2 = new Message();
                        //String temp = todo2.setMessage(current.getValue(Message.class).getMessage());
                        //String sender = todo2.setSender(current.getValue(Message.class).getSender());
                        //Log.e(">>>>>last1111222222", temp + "");
                    }
                    for (DataSnapshot currentreceipent : dataSnapshot.getChildren()) {

                        //  if (!currentreceipent.toString().equals(currentuser)) {
                        keyperson = String.valueOf(currentreceipent.getKey());
                        if (!keyperson.equals(currentuser)) {
                            lstreceptmsg.add(keyperson);
                            Log.e("temp3123", keyperson);


                            for (DataSnapshot currentreceipentcild1 : currentreceipent.child(currentuser).getChildren()) {
                                Log.e("temp31111", String.valueOf(currentreceipent.child(currentuser).getValue()));


                                //  if (currentreceipentcild.hasChild(currentuser)) {
                                //   if (currentreceipentcild.toString().equals(currentuser)) {
                                //       keyperson1 = String.valueOf(currentreceipentcild.getKey());
                                //   if (keyperson1.equals(currentuser)) {

                                // }
                                //     Log.e("keyperson1", keyperson1);


                                MessageChatModel newMessage = currentreceipentcild1.getValue(MessageChatModel.class);
                                temp3 = newMessage.getMessage();
                                temp4 = newMessage.getRecipient();

                                //  lstreceptmsg.add(temp3);
                                //  Log.e("temp3", lstreceptmsg);
                                //while(temp1)
                                // Log.e(">>>>>>",temp1);
                                // Log.e(">>>>>last1111", newMessage.getMessage() + "");
                                //    Message todo1 = new Message();
                                //Message todo2 = new Message();
                                //String temp = todo2.setMessage(current.getValue(Message.class).getMessage());
                                //String sender = todo2.setSender(current.getValue(Message.class).getSender());
                                //Log.e(">>>>>last1111222222", temp + "");

                            }
                        }
                    }


                    //  Log.e("temp31111", keyperson1);
                    Log.e("temp3000", keyperson);

                    Log.e(">>>>>>>>temp", temp3);
                    lstmsg.add(temp1);


                    String keyname = String.valueOf(sunny.getKey()).replace("+", ":");


                    String[] parts = keyname.split(":"); // escape .
                    String part1 = parts[0];
                    String part2 = parts[1];
                    String tendigitnumber = getLastThree(part2);
                    Log.e(">>>>>last", dataSnapshot.child(currentuser).getChildrenCount() + "");

                    hashMap1.put(tendigitnumber, keyname.replace(":", "+"));

                    long sun = dataSnapshot.child(currentuser).getChildrenCount();
                    if (lst.size() > sun) {
                        lst.clear();
                    } else {

                        contactmatch = getContactDisplayNameByNumber(tendigitnumber);
                        if (!contactmatch.equals("?")) {
                            lst.add(contactmatch);

                        }

                        //       lst.add(sunny.getKey());


                        Log.e(">>>>>last", sunny.getChildren() + "");
                        Log.e(">>>>>last123", sunny.getKey() + "");
                        Log.e(">>>>>dsp", sunny + "");
                        recentchatadapter rec = new recentchatadapter(recentchat.this, lst, lstmsg);
                        //  RecentmessagesAdapter = new ArrayAdapter<String>(recentchat.this,android.R.layout.simple_list_item_1,android.R.id.text1);
                        RecentUser.setAdapter(rec);

                        rec.notifyDataSetChanged();
                        // RecentUser.setSelection(RecentmessagesAdapter.getCount() - 1);

                    }


                    RecentUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //TextView t1 = (TextView) findViewById(android.R.id.text1);
                            Log.e(">>>>asd", lst.get(position) + "");

                            Log.e(">>>>>NAME_NUMBER", hashMap.get(lst.get(position)) + "");

                            Log.e(">>>>>NUMBER_KEY", hashMap1.get(hashMap.get(lst.get(position))) + "");
                            Intent intent4 = new Intent(recentchat.this, chatactivity.class).putExtra("key_position", hashMap1.get(hashMap.get(lst.get(position)))).putExtra("namenumber", lst.get(position) + "");
                            startActivity(intent4);
                        }
                    });
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent(recentchat.this, Main.class);
            startActivity(intent1);
        }
        return super.onKeyDown(keycode, event);
    }
}
