package com.vedant.hereami.chatfolder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vedant.hereami.firebasepushnotification.EndPoints;
import com.vedant.hereami.firebasepushnotification.MyVolley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class notifyme extends BroadcastReceiver {
    private static String KEY_TEXT_REPLY = "key_text_reply";
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
    private Bundle remoteInput;
    private String message2;
    private RemoteInput.Builder intent;
    private Context mCtx;
    private int messageId;
    private CharSequence message;
    private Bundle bundle;
    private Firebase mFirebaseMessagesChat13;
    private String tag;


    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mSenderUid = user.getEmail().replace(".", "dot") + user.getDisplayName();
        // Set recipient uid

        Firebase.setAndroidContext(context);
        remoteInput = RemoteInput.getResultsFromIntent(intent);
        bundle = intent.getExtras();
        if (remoteInput != null) {
            Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
            mFirebaseMessagesChat = fb_parent.child("/message");

            message2 = bundle.getString("key_position");
            tag = bundle.getString("tag");
            messageId = bundle.getInt("KEY_NOTIFICATION_ID");
            message = remoteInput.getCharSequence(KEY_TEXT_REPLY);
            if (message2 != null) {
                mRecipientUid = message2.replace("-", "").replace(mSenderUid, "");
                //    message1 = message2;
                //    message3 = message2;
                mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(mSenderUid).child(mRecipientUid);
                mFirebaseMessagesChat13 = mFirebaseMessagesChat.child(mRecipientUid).child(mSenderUid);
                //  mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(message2);
            }
            this.mCtx = context;
            sendnotification();

            Log.e(">>dadds", message2);

            mRecipientUid = message2.replace("-", "").replace(mSenderUid, "");
            message1 = mRecipientUid;
        }

        //    Toast.makeText(context, "Message ID: " + messageId + "\nMessage: " + message,
        //          Toast.LENGTH_SHORT).show();

        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

    }

    public void sendnotification() {

        senderMessage = message.toString();

        TimeZone pdt = TimeZone.getDefault();

        Calendar calendar = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        Date now = new Date();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        String tsTemp = String.format("%02d:%02d", hour, minutes) + "%" + date + "/" + month + "/" + year;
        if (senderMessage.length() < 6) {
            senderMessage = senderMessage + "       ";
        }
        Map<String, String> newMessage = new HashMap<String, String>();
        newMessage.put("sender", mSenderUid); // Sender uid
        newMessage.put("recipient", mRecipientUid); // Recipient uid
        newMessage.put("message", senderMessage);// Message
        newMessage.put("timestamp", tsTemp); // Time stamp
        newMessage.put("devicetoken", FirebaseInstanceId.getInstance().getToken());

        if (mFirebaseMessagesChat12 != null) {
            mFirebaseMessagesChat12.push().setValue(newMessage, index);
            mFirebaseMessagesChat13.push().setValue(newMessage, index);
            NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(tag, messageId);
            sendSinglePush();
        }

        //    mUserMessageChatText.setText("");


    }

    private void sendSinglePush() {
        final String title = user.getEmail().replace(".", "dot") + user.getDisplayName();
        final String message = senderMessage;
        final String title1 = message2.replace("-", "").replace(title, "");
        // final String image;

        String[] parts = message2.replace("-", "").replace(title, "").replace("+", ":").split(":"); // escape .
        String part1 = parts[0];
//          String part2 = parts[1];
        //  String tendigitnumber = getLastThree(part2);
        final String email = part1.replace("dot", ".");

        Log.e("email bol", title);
        Log.e("email bol1", message);
        Log.e("email bol2", title1);
        Log.e("email bol3", email);
        // Log.e("email bol", reverseWords2(email));

//        progressDialog.setMessage("Sending Push");
        //      progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_SINGLE_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //                    progressDialog.dismiss();

                        //     Toast.makeText(notifyme.this, response, Toast.LENGTH_LONG).show();
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
                params.put("title", title1 + mSenderUid);
                params.put("message", message);

                //     if (!TextUtils.isEmpty(image))
                //       params.put("image", image);

                params.put("email", email);
                return params;

            }
        };

        MyVolley.getInstance(mCtx.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

}
