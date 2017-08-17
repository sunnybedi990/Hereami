package com.vedant.hereami.chatfolder;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vedant.hereami.ViewPager.TabWOIconActivity;
import com.vedant.hereami.firebasepushnotification.EndPoints;
import com.vedant.hereami.firebasepushnotification.MyVolley;
import com.vedant.hereami.secureencryption.AESHelper;
import com.vedant.hereami.secureencryption.InsecureSHA1PRNGKeyDerivator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;


public class chatactivity extends AppCompatActivity {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final String TAG = chatactivity.class.getSimpleName();
    public static String seedValue = "youcantseeme@9900";
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
    private Firebase mFirebaseMessagesChat13;
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
    private String message2;
    private CoordinatorLayout coordinatorLayout3;
    private ActionBar actionBar;
    private String todaycheck;
    private String message3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        coordinatorLayout3 = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayoutmain3);
        actionBar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = this.getIntent();
        remoteInput = RemoteInput.getResultsFromIntent(intent);


        Bundle bundle = getIntent().getExtras();

        message1 = bundle.getString("key_position1");
        message2 = bundle.getString("key_position");
        namenumber = bundle.getString("namenumber");

        setTitle(namenumber);

        if (!isInternetOn()) {


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout3, "Check Your internet connection", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        } else {

            // Get information from the previous activity
            Intent getUsersData = getIntent();
            UsersChatModel usersDataModel = getUsersData.getParcelableExtra(ReferenceUrl.KEY_PASS_USERS_INFO);
            mSenderUid = user.getEmail().replace(".", "dot") + user.getDisplayName();

            Log.e("getname1", message1 + ")");
            // Set recipient uid


            TimeZone pdt = TimeZone.getDefault();
            Calendar calendar1 = new GregorianCalendar(pdt);
            Date trialTime = new Date();
            calendar1.setTime(trialTime);
            Date now = new Date();
            int date1 = calendar1.get(Calendar.DATE);
            int month1 = calendar1.get(Calendar.MONTH);
            int year1 = calendar1.get(Calendar.YEAR);
            todaycheck = date1 + "/" + month1 + "/" + year1;
            // Set sender uid;


            // Reference to recyclerView and text view
            mChatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
            mUserMessageChatText = (TextView) findViewById(R.id.chat_user_message);
            mUserMessageChatconnection = (TextView) findViewById(R.id.text_connection);


            mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mChatRecyclerView.setHasFixedSize(true);
            // Initialize adapter
            List<MessageChatModel> emptyMessageChat = new ArrayList<MessageChatModel>();
            mMessageChatAdapter = new MessageChatAdapter(emptyMessageChat);

            // Set adapter to recyclerView
            mChatRecyclerView.setAdapter(mMessageChatAdapter);


            // Initialize firebase for this chat
            Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
            mFirebaseMessagesChat = fb_parent.child("/message");
            mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");
            mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
            Log.e(">>connect", mFirebaseMessagesChatconnectioncheck.getKey());
//        mFirebaseMessagesChatconnection = fb_parent.child("/users").child(message1).child(ReferenceUrl.CHILD_CONNECTION);
            //      if (mFirebaseMessagesChatconnection != null)
            if (message1 != null) {
                mRecipientUid = message1;
                message3 = message1;
                //   message1 = message2.replace("-", "").replace(mSenderUid, "");
                mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(mSenderUid).child(message1);
                mFirebaseMessagesChat13 = mFirebaseMessagesChat.child(message1).child(mSenderUid);
                messages();


            } else {
                if (message2 != null) {
                    mRecipientUid = message2;
                    //    message1 = message2;
                    message3 = message2;
                    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(mSenderUid).child(message2);
                    mFirebaseMessagesChat13 = mFirebaseMessagesChat.child(message2).child(mSenderUid);
                    messages();
                }
            }

        }
        currentuser = user.getEmail().replace(".", "dot") + user.getDisplayName();

        //    if (message2 != null) {
        //      mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(message2);
        //    messages();
        //  }
        //  else{
        //      startchat();
        //  }
        //       Log.e("getname1", "pasess1");
        //
        //   } else {
        //     startchat();
        //   }

    }

    public void startchat() {
        mFirebaseMessagesChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot last : dataSnapshot.getChildren()) {
                    //  mFirebaseMessagesChat12 = last.child()

                    //           Log.e("getname1", last.getKey().contains(currentuser + "-" + message1) + "");
                    //         Log.e("getname8", last.getKey());
                    //       if (last.getKey().contains(currentuser + "-" + message1)) {

                        mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(currentuser + "-" + message1);
                    //       Log.e("getname1", "pasess2");
                    //    } else {
                    //    Log.e("getname1", last.getKey().contains(message1 + "-" + currentuser) + "");
                    //  if (last.getKey().contains(message1 + "-" + currentuser)) {
                    //    mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(message1 + "-" + currentuser);
                    //     Log.e("getname1", "pasess3");
                    //  } else {
                    //      mFirebaseMessagesChat12 = mFirebaseMessagesChat.child(currentuser + "-" + message1);
                    //          if (message2 == null) {
                    //            message2 = currentuser + "-" + message1;
                    //      }
                    //    Log.e("getname1", "pasess4");
                    //   }


                    //   Log.e("getname4", mFirebaseMessagesChat12.toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        messages();
    }

    public void messages() {
        if (mFirebaseMessagesChat12 != null) {
            mFirebaseMessagesChat12.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot result) {

                    lst = new ArrayList<String>();
                    //  for (DataSnapshot dsp : result.getChildren()) {
                    for (DataSnapshot last : result.getChildren()) {


                        MessageChatModel newMessage = last.getValue(MessageChatModel.class);
                        if (newMessage.getSender().equals(currentuser)) {
                            newMessage.setRecipientOrSenderStatus(SENDER_STATUS);
                        } else {
                            newMessage.setRecipientOrSenderStatus(RECIPIENT_STATUS);
                        }

                        mMessageChatAdapter.refillAdapter(newMessage);
                        mChatRecyclerView.scrollToPosition(mMessageChatAdapter.getItemCount() - 1);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

                //  }
        });


        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {
                    if (connectionchild.getKey().contains(message3)) {


                        connectionstatus2 = dataSnapshot1.child(message3).child(ReferenceUrl.image).getValue().toString();
                        connectionstatus3 = dataSnapshot1.child(message3).child(ReferenceUrl.imagecheck).getValue().toString();

                        imageAsBytes = Base64.decode(connectionstatus2.getBytes(), Base64.DEFAULT);
                        image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                        Log.e("getname2", message3);
                        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                                | ActionBar.DISPLAY_SHOW_CUSTOM);
                        CircleImageView imageView = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            imageView = new CircleImageView(actionBar.getThemedContext());
                        }
//                        imageView.setScaleType(ImageView.ScaleType.CENTER);
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                                ActionBar.LayoutParams.WRAP_CONTENT,
                                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.END
                        );

                        layoutParams.rightMargin = 80;

                        imageView.setLayoutParams(layoutParams);
                        actionBar.setCustomView(imageView);
                        //   imageView.setOnClickListener();
                        Log.e("pic1", String.valueOf(image));

                        if (!connectionstatus3.equals(connectionstatus2)) {

                            myConnectionsStatusRef2 = mFireChatUsersRef.child(message3).child(ReferenceUrl.imagecheck);
                            myConnectionsStatusRef2.setValue(connectionstatus2);

                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

                            String filepath = Environment.getExternalStorageDirectory().getPath();
                            File myDir = new File(filepath + "/.HereamI");
                            Log.e("file", myDir.toString());
                            myDir.mkdirs();
                            String fname = namenumber + ".jpg";
//you can create a new file name "test.jpg" in sdcard folder.
                            File file = new File(myDir, fname);
                            if (file.exists()) file.delete();
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                out.write(bytes.toByteArray());
                                out.flush();
                                out.close();
                                Log.e("downloadstatus", "file downloaded");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        //   menu.getItem(0).setIcon(new BitmapDrawable(getResources(), image));


                        System.out.println("Downloaded image with length: " + imageAsBytes.length);

                        //       menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_launcher));
                        connectionstatus = dataSnapshot1.child(message3).child(ReferenceUrl.CHILD_CONNECTION).getValue().toString();
                        connectionstatus1 = dataSnapshot1.child(message3).child(ReferenceUrl.timestamp).getValue().toString();
                        String[] parts1 = connectionstatus1.split("%");
                        String part4 = parts1[0];
                        String part5 = parts1[1];
                        String s;
                        if (!part5.equals(todaycheck)) {
                            s = connectionstatus1;
                        } else {
                            s = part4;
                        }
                        if (connectionstatus.equals("offline")) {
                            mUserMessageChatconnection.setText("last seen at " + s);
                        } else

                            mUserMessageChatconnection.setText(connectionstatus);
                        Log.e(">>connect", connectionstatus);

                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
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
            // mMessageChatAdapter.clear();
            mMessageChatAdapter.cleanUp();
        }
        this.finish();
    }


    public void sendMessageToFireChat(View sendButton) {
        senderMessage = mUserMessageChatText.getText().toString();
        senderMessage = senderMessage.trim();

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
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            String tsTemp = String.format("%02d:%02d", hour, minutes) + "%" + date + "/" + month + "/" + year;
            //    SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            //    String formattedTime = sdf.format(tsTemp);

            // Log.e(TAG, "send message1");
            int sendersize = mUserMessageChatText.getText().length();
            if (sendersize < 6) {
                senderMessage = senderMessage + "       ";
            }

            // Send message1 to firebase
            Map<String, String> newMessage = new HashMap<String, String>();
            newMessage.put("sender", mSenderUid); // Sender uid
            newMessage.put("recipient", mRecipientUid); // Recipient uid
            newMessage.put("message", senderMessage);// Message
            newMessage.put("timestamp", tsTemp); // Time stamp
            newMessage.put("devicetoken", FirebaseInstanceId.getInstance().getToken());
            sendSinglePush();

            mFirebaseMessagesChat12.push().setValue(newMessage, index);
            mFirebaseMessagesChat13.push().setValue(newMessage, index);

            mUserMessageChatText.setText("");


        }
    }

    private void sendSinglePush() {
        final String title = user.getEmail().replace(".","dot")+user.getDisplayName();
        final String message = senderMessage;
        // final String image;

        final String title1 = message3;

        String[] parts = message3.replace("-", "").replace(title, "").replace("+", ":").split(":");  // escape .
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

                        Toast.makeText(chatactivity.this, response, Toast.LENGTH_LONG).show();
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

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            Intent intent1 = new Intent(chatactivity.this, TabWOIconActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
        return super.onKeyDown(keycode, event);
    }
    public static String reverseIt(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--) {
            dest.append(source.charAt(i));
        }

        return dest.toString();
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
            int date = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            String tsTemp = String.format("%02d:%02d", hour, minutes) + "%" + date + "/" + month + "/" + year;

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
            if (mFirebaseMessagesChat12 != null) {
                mFirebaseMessagesChat12.push().setValue(newMessage, index);

            } else {
                startchat();
                sendnotification();
            }
            //    mUserMessageChatText.setText("");


        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public String encryption(String strNormalText) {

        String normalTextEnc = "";
        try {
            normalTextEnc = AESHelper.encrypt(seedValue, strNormalText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalTextEnc;
    }

    public String decryption(String strEncryptedText) {
        String seedValue = "YourSecKey";
        String strDecryptedText = "";
        try {
            strDecryptedText = AESHelper.decrypt(seedValue, strEncryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDecryptedText;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static SecretKey deriveKeyInsecurely(String password, int
            keySizeInBytes) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.US_ASCII);
        return new SecretKeySpec(
                InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(
                        passwordBytes, keySizeInBytes),
                "AES");
    }
}