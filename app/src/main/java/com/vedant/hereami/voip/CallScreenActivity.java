package com.vedant.hereami.voip;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.vedant.hereami.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.vedant.hereami.Fragment.ChatFragment.getLastThree;

public class CallScreenActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();
    public HashMap<String, String> hashMap;
    public HashMap<String, String> hashMap1;
    public AudioManager audioManager;
    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;
    private String mCallId;
    private long mCallStart = 0;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    private String tendigitnumber;
    private String contactmatch;
    private Bitmap myBitmap;
    private ImageView myImage;
    private String title;
    private Spinner spinner;
    private String[] audiomode;
    private Button speaker;
    private MusicIntentReceiver myReceiver;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);

        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = findViewById(R.id.callDuration);
        mCallerName = findViewById(R.id.remoteUser);
        mCallState = findViewById(R.id.callState);
        Button endCallButton = findViewById(R.id.hangupButton);
        speaker = findViewById(R.id.btn_speaker);
        Button mute = findViewById(R.id.btn_mute);
        spinner = findViewById(R.id.spinner_audio);
        myImage = findViewById(R.id.imageview_call);
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        spinner.setVisibility(View.GONE);
        myReceiver = new MusicIntentReceiver();


        getaudio();

        speaker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioManager.isSpeakerphoneOn()) {
                    AudioSourceUtil.connectEarpiece(audioManager);
                } else {
                    AudioSourceUtil.connectSpeaker(audioManager);
                }
            }
        });
        mute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioManager.isMicrophoneMute()) {
                    audioManager.setMicrophoneMute(false);
                } else {
                    audioManager.setMicrophoneMute(true);
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // your code here

                // Get selected row data to show on screen
                String Company = spinner.getSelectedItem().toString();
                if (Company.equalsIgnoreCase("Bluetooth")) {
                    AudioSourceUtil.connectBluetooth(audioManager);
                } else if (Company.equalsIgnoreCase("Headphones")) {
                    AudioSourceUtil.connectHeadphones(audioManager);
                } else if (Company.equalsIgnoreCase("Speaker")) {
                    AudioSourceUtil.connectSpeaker(audioManager);
                } else if (Company.equalsIgnoreCase("Ear piece")) {
                    AudioSourceUtil.connectEarpiece(audioManager);
                }

                Toast.makeText(
                        getApplicationContext(), Company, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        mCallStart = System.currentTimeMillis();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getaudio();
            }

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
            mCallerName.setText(contactmatch);
            mCallState.setText(call.getState().toString());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
        unregisterReceiver(myReceiver);

    }

    @Override
    public void onResume() {

        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);


        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        //   Intent intent1 = new Intent(CallScreenActivity.this, TabWOIconActivity.class);
        //   startActivity(intent1);
        finish();
    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isHeadphonesPlugged() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        for (AudioDeviceInfo deviceInfo : audioDevices) {
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isBluetoothPlugged() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        for (AudioDeviceInfo deviceInfo : audioDevices) {
            if (deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
                    || deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getaudio() {

        if (isHeadphonesPlugged()) {
            spinner.setVisibility(View.VISIBLE);
            speaker.setVisibility(View.GONE);
            audiomode = new String[]{"Headphones", "Speaker", "Ear piece"};
            if (spinner.getVisibility() == View.VISIBLE) {
                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, audiomode);
                langAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(langAdapter);
            }
        } else if (isBluetoothPlugged()) {
            spinner.setVisibility(View.VISIBLE);
            speaker.setVisibility(View.GONE);
            audiomode = new String[]{"Bluetooth", "Speaker", "Ear piece"};
            if (spinner.getVisibility() == View.VISIBLE) {
                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, audiomode);
                langAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(langAdapter);
            }
        } else if (isBluetoothPlugged() || isHeadphonesPlugged()) {
            spinner.setVisibility(View.VISIBLE);
            speaker.setVisibility(View.GONE);
            audiomode = new String[]{"Bluetooth", "Headphones", "Speaker", "Ear piece"};
            if (spinner.getVisibility() == View.VISIBLE) {
                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, audiomode);
                langAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(langAdapter);
            }

        } else {
            spinner.setVisibility(View.GONE);
            speaker.setVisibility(View.VISIBLE);
        }
    }

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    private class SinchCallListener implements CallListener {

        private SinchService.SinchServiceInterface callClient;

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            Toast.makeText(CallScreenActivity.this, endMsg, Toast.LENGTH_LONG).show();
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getaudio();
            }
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());


            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

            mCallStart = System.currentTimeMillis();
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getaudio();
            }
            mAudioPlayer.playProgressTone();
            mCallState.setText("Calling");

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //       Map<String,String> headers = new HashMap<>();
            //    headers.put("first value is", "@123");
            //   headers.put("custom value", "two");
            //   call = callClient.callUser(call.getRemoteUserId(), headers);
            // Send a push through your push provider here, e.g. GCM
        }
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        getaudio();
                        Log.d(TAG, "Headset is unplugged");
                        break;
                    case 1:
                        getaudio();
                        Log.d(TAG, "Headset is plugged");
                        break;
                    default:
                        Log.d(TAG, "I have no idea what the headset state is");
                }
            }

            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        getaudio();
                        //    setButtonText("Bluetooth off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        getaudio();
                        //   setButtonText("Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        getaudio();
                        //   setButtonText("Bluetooth on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        getaudio();
                        //   setButtonText("Turning Bluetooth on...");
                        break;
                }
            }


        }
    }

}
