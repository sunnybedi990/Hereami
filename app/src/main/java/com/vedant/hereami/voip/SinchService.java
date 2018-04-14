package com.vedant.hereami.voip;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

import java.util.Map;

import static com.vedant.hereami.chatfolder.chatactivity.mypreference123;

public class SinchService extends Service {

    public static final String APP_KEY = "d3972181-08b2-40bb-aad0-ba8dbaef60ff";
    public static final String APP_SECRET = "7sjI/UGgT0KjkLSEYNFdhg==";
    public static final String ENVIRONMENT = "clientapi.sinch.com";

    public static final String LOCATION = "LOCATION";
    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();

    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;

    private StartFailedListener mListener;
    private String username;
    public boolean isclientstopped;

    @Override
    public void onCreate() {
        SharedPreferences sharedpreferences = getSharedPreferences(mypreference123, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", "");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //  startTimer();
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            //    mSinchClient.terminate();
            Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
            sendBroadcast(broadcastIntent);
        }

        super.onDestroy();
    }

    public void start(String userName) {
        if (mSinchClient == null) {
            mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.setSupportActiveConnectionInBackground(true);
            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.setSupportManagedPush(true);
            mSinchClient.setSupportPushNotifications(true);
            mSinchClient.start();
            isclientstopped = false;
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            //     mSinchClient.terminate();
            //   mSinchClient = null;
            isclientstopped = true;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    public class SinchServiceInterface extends Binder {

        public Call callPhoneNumber(String phoneNumber) {
            return mSinchClient.getCallClient().callPhoneNumber(phoneNumber);
        }

        public Call callUser(String userId) {
            return mSinchClient.getCallClient().callUser(userId);
        }

        public Call callUser(String userId, Map<String, String> headers) {
            return mSinchClient.getCallClient().callUser(userId, headers);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }
    }

    public interface StartFailedListener {
        void onServiceConnected();

        void onStartFailed(SinchError error);

        void onStarted();

        void onServiceConnected(ComponentName componentName, IBinder iBinder);
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;

            mUserId = username;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(username)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();

            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            if (mSinchClient == null) {
                mUserId = username;
                mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(username)
                        .applicationKey(APP_KEY)
                        .applicationSecret(APP_SECRET)
                        .environmentHost(ENVIRONMENT).build();

                mSinchClient.setSupportCalling(true);
                mSinchClient.startListeningOnActiveConnection();

                mSinchClient.addSinchClientListener(new MySinchClientListener());
                mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
                mSinchClient.start();
                Log.d(TAG, "SinchClient stopped");
            }
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d(TAG, "Incoming call");
            Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
            intent.putExtra(CALL_ID, call.getCallId());
            //     intent.putExtra(LOCATION, call.getHeaders().get("location"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchService.this.startActivity(intent);
        }
    }

}