package com.vedant.hereami.chatfolder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.NotificationCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recentnotification extends Service {
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
    private long sunn = 0;
    private ProgressBar mProgressBarForUsers;
    private List<String> lstmsg1;
    private List<String> newList;
    private long countone;
    private List<String> newList1;


    public Recentnotification() {
    }


    public void onCreate() {
        super.onCreate();
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        Firebase.setAndroidContext(this);
        //  RecentUser = (ListView) findViewById(R.id.list_recent);
        lst = new ArrayList<String>();
        lstmsg = new ArrayList<String>();
        lstreceptmsg = new ArrayList<String>();
        lstmsg1 = new ArrayList<String>();
        newList = new ArrayList<String>();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
        //  mProgressBarForUsers = (ProgressBar)findViewById(R.id.progress_bar_users1);
        //  showProgressBarForUsers();
        // Bundle bundle = getIntent().getExtras();

        // message1 = bundle.getString("key_position");
        //  namenumber = bundle.getString("namenumber");
        //  setTitle(namenumber);

        // Get information from the previous activity
        //  Intent getUsersData = getIntent();
        // UsersChatModel usersDataModel = getUsersData.getParcelableExtra(ReferenceUrl.KEY_PASS_USERS_INFO);

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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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


    //  private void showProgressBarForUsers() {
    //      mProgressBarForUsers.setVisibility(View.VISIBLE);
    //   }


    //  private void hideProgressBarForUsers() {
    //     if (mProgressBarForUsers.getVisibility() == View.VISIBLE) {
    //         mProgressBarForUsers.setVisibility(View.GONE);
    //     }
    //   }

    public void chats() {
        mFirebaseMessagesChat.addChildEventListener(new ChildEventListener() {



            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getNotification();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
        Intent intent1 = new Intent(this, recentchat.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, 0);
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.image);

        // Build notification
        // Actions are just fake

        NotificationCompat.Builder noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                noti = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setContentTitle("Recent User")
                        .setContentText("New Message").setSmallIcon(R.drawable.image)
                        .setLargeIcon(picture)
                        .setContentIntent(pIntent1).setDefaults(NotificationCompat.DEFAULT_ALL);

            }
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected


        // notificationManager.notify(0, noti);

        int unOpenCount = AppUtill.getPreferenceInt("NOTICOUNT", this);
        unOpenCount = unOpenCount + 1;

        AppUtill.savePreferenceLong("NOTICOUNT", unOpenCount, this);
        notificationManager.notify(0 /* ID of notification */, noti.build());

// This is for bladge on home icon
        //  BadgeUtils.setBadge(Recentnotification.this,(int)unOpenCount);


    }
}


