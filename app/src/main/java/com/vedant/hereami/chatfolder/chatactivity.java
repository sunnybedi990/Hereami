package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.vedant.hereami.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class chatactivity extends Activity {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final String TAG = chatactivity.class.getSimpleName();

    private ListView mChatRecyclerView;
    private TextView mUserMessageChatText;
    private ArrayAdapter mMessageChatAdapter;

    /* Sender and Recipient status*/
    private static final int SENDER_STATUS = 0;
    private static final int RECIPIENT_STATUS = 1;

    /* Recipient uid */
    private String mRecipientUid;

    /* Sender uid */
    private String mSenderUid;

    /* unique Firebase ref for this chat */
    private Firebase mFirebaseMessagesChat;
    private Firebase mFirebaseMessagesChat12;
    private Firebase mFirebaseMessagesChatreceipent;
    private Firebase mFirebaseMessagesChatreceipentmessages;

    private FirebaseDatabase getdata;


    /* Listen to change in chat in firabase-remember to remove it */
    private ChildEventListener mMessageChatListener;
    public String message1;
    private String namenumber;
    public FirebaseUser user;
    public FirebaseAuth firebaseAuth;
    private MessageChatModel newMessage;
    public String currentuser;
    private String mail;
    private ArrayList<String> lst;
    private Message m;
    private int SENDERS;
    private Firebase mFirebaseMessagesChatreceipentmessagesnext;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = getIntent().getExtras();

        message1 = bundle.getString("key_position");
        namenumber = bundle.getString("namenumber");
        // Get information from the previous activity
        Intent getUsersData = getIntent();
        UsersChatModel usersDataModel = getUsersData.getParcelableExtra(ReferenceUrl.KEY_PASS_USERS_INFO);

        // Set recipient uid
        mRecipientUid = message1;

        // Set sender uid;
        mSenderUid = user.getEmail().replace(".", "dot") + user.getDisplayName();

        // Reference to recyclerView and text view
        mChatRecyclerView = (ListView) findViewById(R.id.chat_recycler_view);
        mUserMessageChatText = (TextView) findViewById(R.id.chat_user_message);

        // Set recyclerView and adapter


        // Initialize adapter


        // Set adapter to recyclerView


        // Initialize firebase for this chat
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
        mFirebaseMessagesChat = fb_parent.child("/message");

        currentuser = user.getEmail().replace(".", "dot") + user.getDisplayName();
        mFirebaseMessagesChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot last : dataSnapshot.getChildren()) {
                    for (DataSnapshot fireboy : last.getChildren()) {
                        for (DataSnapshot sunny : dataSnapshot.child(currentuser).getChildren()) {

                            //    Log.e(TAG, " I am onStart");
//                                Log.e(TAG, mFirebaseMessagesChat.child(currentuser).getPath().toString().contains(currentuser) + "");
//
                            //                              Log.e(TAG, String.valueOf(sunny.getKey()) + "");


                            //                            Log.e(TAG, mFirebaseMessagesChat.child(message1).getPath().toString());
                            //                           Log.e(TAG, mFirebaseMessagesChatreceipentmessages.child(currentuser).getPath().toString());
                            if (last.toString().contains(currentuser)) {
                                if (String.valueOf(sunny.getKey()).contains(message1)) {
                                    Log.e(TAG, "i am current");
                                    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(currentuser);
                                    mFirebaseMessagesChatreceipent = mFirebaseMessagesChat12.child(message1);


                                }

                            }

                        }
                        for (DataSnapshot receipientsmessages : dataSnapshot.child(message1).getChildren()) {
                            if (last.toString().contains(message1)) {
                                if (String.valueOf(receipientsmessages.getKey()).contains(currentuser)) {
                                    Log.e(TAG, " I am receipient");
                                    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(message1);
                                    mFirebaseMessagesChatreceipent = mFirebaseMessagesChat12.child(currentuser);

                                    Toast.makeText(chatactivity.this, "hogaya receipient", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                    }
                }
                if (mFirebaseMessagesChatreceipent == null) {
                    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(currentuser);
                    mFirebaseMessagesChatreceipent = mFirebaseMessagesChat12.child(message1);
                }
                mFirebaseMessagesChatreceipent.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }

                    public void onDataChange(DataSnapshot result) {

                        lst = new ArrayList<String>();
                        //  for (DataSnapshot dsp : result.getChildren()) {
                        for (DataSnapshot last : result.getChildren()) {

                            // HashMap<String, Object> yourData = (HashMap<String, Object>) last.getValue();
                            // Map<String, String> getmessage = new HashMap<String, String>();
                            // getmessage.get("receipents");

                            // String keyname = String.valueOf(last.getValue((GenericTypeIndicator<Object>) getmessage));
                            //  Message todo1 = new Message(message1);
                            Message todo2 = new Message();
                            String temp = todo2.setMessage(last.getValue(Message.class).getMessage());

                            Log.e(">>>>>>>>>", temp);
                            //  t1.setText();
                            //    m = last.getValue(Message.class);
                            System.out.println(">>>>>>>>" + todo2.getMessage());
                            lst.add(temp);


                            Log.e(">>>>>last", lst.size() + "");
                            Log.e(">>>>>dsp", last + "");
                            mMessageChatAdapter = new ArrayAdapter<String>(chatactivity.this, R.layout.activity_listfrag, lst) {
                                @NonNull
                                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    tv = (TextView) view.findViewById(R.id.text123);
                                    //   tv.setTextColor(Color.WHITE);


                                    return view;
                                }
                            };
                            mChatRecyclerView.setAdapter(mMessageChatAdapter);

                            mMessageChatAdapter.notifyDataSetChanged();
                            mChatRecyclerView.setSelection(mMessageChatAdapter.getCount() - 1);
                            //  mChatRecyclerView.scrollToPosition(mMessageChatAdapter.getCount() - 1);

                        }
                    }

                    //  }
                });


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.e(TAG, " I am onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "I am onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "I am onStop");

        // Remove listener
        if (mMessageChatListener != null) {
            // Remove listener
            mFirebaseMessagesChat.removeEventListener(mMessageChatListener);
        }
        // Clean chat message1
        //   mMessageChatAdapter.cleanUp();
        if (mMessageChatAdapter != null) {
            mMessageChatAdapter.clear();
        }
        this.finish();
    }


    public void sendMessageToFireChat(View sendButton) {
        String senderMessage = mUserMessageChatText.getText().toString();
        senderMessage = senderMessage.trim();

        if (!senderMessage.isEmpty()) {

            // Log.e(TAG, "send message1");

            // Send message1 to firebase
            Map<String, String> newMessage = new HashMap<String, String>();
            newMessage.put("sender", mSenderUid); // Sender uid
            newMessage.put("recipient", mRecipientUid); // Recipient uid
            newMessage.put("message", senderMessage); // Message


            mFirebaseMessagesChatreceipent.push().setValue(newMessage);

            mUserMessageChatText.setText("");
        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            this.onDestroy();
        }
        return super.onKeyDown(keycode, event);
    }
}



