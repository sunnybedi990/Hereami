package com.vedant.hereami.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.RemoteInput;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vedant.hereami.R;
import com.vedant.hereami.chatfolder.Message;
import com.vedant.hereami.chatfolder.MessageChatAdapter;
import com.vedant.hereami.chatfolder.MessageChatModel;
import com.vedant.hereami.chatfolder.ReferenceUrl;
import com.vedant.hereami.chatfolder.chatactivity;
import com.vedant.hereami.firebasepushnotification.EndPoints;
import com.vedant.hereami.firebasepushnotification.MyVolley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by bedi on 4/1/2017.
 */

public class notificationchat extends Activity {
    private Context ctx;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final String TAG = chatactivity.class.getSimpleName();

    private RecyclerView mChatRecyclerView;
    private TextView mUserMessageChatText;
    private MessageChatAdapter mMessageChatAdapter;

    /* Sender and Recipient status*/
    private static final int SENDER_STATUS = 0;
    private static final int RECIPIENT_STATUS = 1;

    /* Recipient uid */
    private String mRecipientUid;

    /* Sender uid */
    private String mSenderUid, senderMessage;

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
    private String update_str, connectionstatus;
    public int index = 0;
    private Firebase mFirebaseMessagesChatconnection;
    private TextView mUserMessageChatconnection;
    private Firebase mFirebaseMessagesChatconnectioncheck;
    private String connectionstatus1;
    private String connectionstatus2;
    private Bitmap image;
    private byte[] imageAsBytes;
    private String connectionstatus3;
    private Firebase myConnectionsStatusRef2;
    private Firebase mFireChatUsersRef;
    private static String KEY_TEXT_REPLY = "key_text_reply";
    private Bundle remoteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emptylayout);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = this.getIntent();
        remoteInput = RemoteInput.getResultsFromIntent(intent);


        Bundle bundle = getIntent().getExtras();

        message1 = bundle.getString("key_position");
        namenumber = bundle.getString("namenumber");
        // setTitle(namenumber);

        mRecipientUid = message1;

        // Set sender uid;
        mSenderUid = user.getEmail().replace(".", "dot") + user.getDisplayName();

        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
        mFirebaseMessagesChat = fb_parent.child("/message");
        mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");
        mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
        Log.e(">>connect", mFirebaseMessagesChatconnectioncheck.getKey());
//        mFirebaseMessagesChatconnection = fb_parent.child("/users").child(message1).child(ReferenceUrl.CHILD_CONNECTION);
        //      if (mFirebaseMessagesChatconnection != null)


        currentuser = user.getEmail().replace(".", "dot") + user.getDisplayName();
        if (remoteInput != null) {


            startchat();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendnotification();
                    //Do something after 100ms
                }
            }, 5000);


        }
    }

    public void startchat() {
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

                                    // Toast.makeText(notificationchat.this, "hogaya receipient", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                    }
                }
                if (mFirebaseMessagesChatreceipent == null) {
                    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(currentuser);
                    mFirebaseMessagesChatreceipent = mFirebaseMessagesChat12.child(message1);
                }

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void sendnotification() {
        //   TextView myTextView = (TextView) findViewById(R.id.text123456);
        String inputString = remoteInput.getCharSequence(
                KEY_TEXT_REPLY).toString();
        senderMessage = inputString;

        if (!senderMessage.isEmpty()) {

            //  String ids = TimeZone.getDefault();
            // if no ids were returned, something is wrong. get out.
            //  if (ids.length == 0)
            //     System.exit(0);

            // begin output
            // System.out.println("Current Time");

            TimeZone pdt = TimeZone.getDefault();

            // set up rules for Daylight Saving Time
            //      pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
            //     pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
            Calendar calendar = new GregorianCalendar(pdt);
            Date trialTime = new Date();
            calendar.setTime(trialTime);
            Date now = new Date();

            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            String tsTemp = String.format("%02d:%02d", hour, minutes);
            //    SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            //    String formattedTime = sdf.format(tsTemp);

            // Log.e(TAG, "send message1");
            //   int sendersize = mUserMessageChatText.getText().length();
            //   if (sendersize < 6) {
            //       senderMessage = senderMessage + "       ";
            //    }
            // Send message1 to firebase
            Map<String, String> newMessage = new HashMap<String, String>();
            newMessage.put("sender", mSenderUid); // Sender uid
            newMessage.put("recipient", mRecipientUid); // Recipient uid
            newMessage.put("message", senderMessage);// Message
            newMessage.put("timestamp", tsTemp); // Time stamp
            newMessage.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
            sendSinglePush();
            if (mFirebaseMessagesChatreceipent == null) {
                startchat();
                sendnotification();
            } else {
                mFirebaseMessagesChatreceipent.push().setValue(newMessage, index);
                finish();
            }
            //    mUserMessageChatText.setText("");


        }
    }

    private void sendSinglePush() {
        final String title = user.getEmail().replace(".", "dot") + user.getDisplayName();
        final String message = senderMessage;
        // final String image;

        String[] parts = message1.replace("+", ":").split(":"); // escape .
        String part1 = parts[0];
//          String part2 = parts[1];
        //  String tendigitnumber = getLastThree(part2);
        final String email = part1.replace("dot", ".");

        // Log.e("email bol", reverseWords2(email));

//        progressDialog.setMessage("Sending Push");
        //      progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //                    progressDialog.dismiss();

                        //   Toast.makeText(chatactivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("message", message);

                //     if (!TextUtils.isEmpty(image))
                //       params.put("image", image);

                params.put("email", email);
                return params;

            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }
}
