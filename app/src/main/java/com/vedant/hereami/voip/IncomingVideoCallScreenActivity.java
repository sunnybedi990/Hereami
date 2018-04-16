package com.vedant.hereami.voip;

import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.vedant.hereami.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static com.vedant.hereami.Fragment.ChatFragment.getLastThree;

public class IncomingVideoCallScreenActivity extends BaseActivity {

    static final String TAG = IncomingVideoCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPlayer mAudioPlayer;
    public HashMap<String, String> hashMap;
    private String tendigitnumber;
    private String contactmatch;
    private String title;
    private Bitmap myBitmap;
    private ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_video_call_screen);
        myImage = findViewById(R.id.imageview_videocall);
        Button answer = findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);
        hashMap = new HashMap<>();
        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
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
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = findViewById(R.id.remoteUser);
            remoteUser.setText(contactmatch);

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, VideoCallScreenActivity.class);
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

    private class SinchCallListener implements VideoCallListener {

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
        public void onShouldSendPushNotification(Call call, List pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
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