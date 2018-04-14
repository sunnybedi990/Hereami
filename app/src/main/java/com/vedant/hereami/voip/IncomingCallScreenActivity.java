package com.vedant.hereami.voip;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.vedant.hereami.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static com.vedant.hereami.Fragment.ChatFragment.getLastThree;
import static com.vedant.hereami.chatfolder.chatactivity.mypreference123;

public class IncomingCallScreenActivity extends BaseActivity implements SinchService.StartFailedListener {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private String mCallLocation;
    public HashMap<String, String> hashMap;
    private AudioPlayer mAudioPlayer;
    private String tendigitnumber;
    private String contactmatch;
    private Bitmap myBitmap;
    private ImageView myImage;
    private String title;
    private SinchClient msnichclient;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference123, Context.MODE_PRIVATE);

        username = sharedpreferences.getString("username", "");
        Button answer = findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);
        myImage = findViewById(R.id.imageview_incomingcall);
        hashMap = new HashMap<>();

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        //mCallLocation = getIntent().getStringExtra(SinchService.LOCATION);
    }

    @Override
    public void onServiceConnected() {


        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            String chatuser = call.getRemoteUserId().replace("+", ":");
            String[] parts = chatuser.split(":"); // escape .
            String part1 = parts[0];
            String part2 = parts[1];
            tendigitnumber = getLastThree(part2);
            //    Log.e(">>>>>last", dataSnapshot.child(currentuser).getChildrenCount() + "");

            //   hashMap1.put(tendigitnumber, keyname);


            contactmatch = getContactDisplayNameByNumber(tendigitnumber);
            if (contactmatch == null) {
                contactmatch = tendigitnumber;
            }
            if (!contactmatch.equals(tendigitnumber)) {
                title = contactmatch;
            }
            File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/HereamI/" + title + "1.jpg");
            if (imgFile.exists()) {

                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myImage.setImageBitmap(myBitmap);
            } else {
                myImage.setImageResource(R.drawable.headshot_7);
            }
            TextView remoteUser = findViewById(R.id.remoteUser);
            remoteUser.setText(contactmatch);
            TextView remoteUserLocation = findViewById(R.id.remoteUserLocation);
            //    remoteUserLocation.setText("Calling from " + mCallLocation);
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        getSinchServiceInterface().startClient(username);
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = number;

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
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

