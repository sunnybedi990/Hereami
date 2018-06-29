/**
 * Copyright Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vedant.hereami.notification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.Call;
import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.R;
import com.vedant.hereami.database.message;
import com.vedant.hereami.login.SplashScreen;
import com.vedant.hereami.voip.SinchService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public HashMap<String, String> hashMap = new HashMap<>();
    private String contactmatch;
    private String tendigitnumber;
    private Bitmap image;
    private byte[] imageAsBytes;
    private String connectionstatus2;
    private String sunny;
    private Firebase mFirebaseMessagesChatconnectioncheck;
    private Bitmap _bitmapScaled;
    private String title;
    private String title1;
    private String title2;
    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String titlenum;
    private Context mcontext;

    public static final String mypreference123 = "mypref123";
    private SharedPreferences sharedpreferences;
    private SinchClient sinchClient;
    private Intent intent;
    private SinchService sinch;
    private Call call;
    private NotificationResult result;
    private SinchService.SinchServiceInterface sinchServiceInterface;
    private String username;
    private SinchService.SinchServiceInterface mSinchServiceInterface;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mcontext = getApplicationContext();
        //Displaying data in log
        //It is optional
        Firebase.setAndroidContext(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        sharedpreferences = getSharedPreferences(mypreference123, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username", "");

        Log.e(TAG, "From: " + username);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        if (SinchHelpers.isSinchPushPayload(remoteMessage.getData())) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            if (isAppIsInBackground(getApplicationContext())) {

                startService(new Intent(MyFirebaseMessagingService.this, SinchService.class));
                //  sinch.start(user.getEmail().replace(".", "dot") + user.getDisplayName());
                // if (sinchClient.isStarted())
                result = SinchHelpers.queryPushNotificationPayload(getApplicationContext(), remoteMessage.getData());
                if (result.getCallResult().isCallCanceled()) {
                    String messagebody = "Missed Call";
                    String title = result.getCallResult().getRemoteUserId();
                    String category = NotificationCompat.CATEGORY_MESSAGE;
                    shownotification(messagebody, title, category);
                } else {
                    String mess = result.getCallResult().getCallId();
                    Log.e("id", mess);
                    Intent intent1 = new Intent(MyFirebaseMessagingService.this, SplashScreen.class).putExtra("callid", mess).putExtra("flag", "A");
                    startActivity(intent1);

                    String messagebody = "Calling";
                    String title = result.getCallResult().getRemoteUserId();
                    String category = NotificationCompat.CATEGORY_CALL;
                    shownotification(messagebody, title, category);
                }
            }


        } else {


            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                try {
                    final JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    Log.e("notidekhbaba", "notification aaya");
                    final JSONObject data = json.getJSONObject("data");
                    final String sender = data.getString("incoming");
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (sender.equals("incoming")) {
                                Log.e("callincomeing", "callincoming");
                            } else {
                                sendPushNotification(json);
                            }
                        }
                    }, 1000);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
            //Calling method to generate notification
//        sendNotification(remoteMessage.getNotification().getBody());
        }
    }





    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String sender = data.getString("sender");
            String timestamp = data.getString("timestamp");
            String message1 = data.getString("message1");
            title1 = data.getString("title");
            String title2 = data.getString("title");
            String titletonotifyme = title2.replace("-", "").replace(user.getEmail().replace(".", "dot") + user.getDisplayName(), "");
            String[] parts1 = titletonotifyme.replace("+", ":").split(":"); // escape .
            String part5 = parts1[0];
            titlenum = parts1[1];
            Log.e(TAG, title1);

            String[] parts = title1.replace("-", "").replace(user.getEmail().replace(".", "dot") + user.getDisplayName(), "").replace("+", ":").split(":"); // escape .
            String part1 = parts[0];
            String part2 = parts[1];
            //  String tendigitnumber = getLastThree(part2);
            sunny = part1.replace("dot", ".");

            tendigitnumber = getLastThree(part2);
            contactmatch = getContactDisplayNameByNumber(tendigitnumber);
            title = contactmatch;
            //getdp();
            String encrytionprivatekey = sharedpreferences.getString("privatekey", "");
            String messagebody = data.getString("message");
            String decryptedmessage = CallsFragment.decryptRSAToString(messagebody, encrytionprivatekey);
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), message.class).putExtra("username", titletonotifyme).putExtra("name", title).putExtra("number", tendigitnumber).putExtra("tablename", "table" + tendigitnumber);
            String title8 = title1.replace(".", "dot");
            //if there is no image

                //displaying small notification

            mNotificationManager.showSmallNotification(title, decryptedmessage, intent, title8, titlenum, tendigitnumber, timestamp, sender, messagebody, message1, imageUrl);

                Log.e("pass", "passed");

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            Log.e(TAG, "yahadelh");
        }
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

    public static String getLastThree(String myString) {
        if (myString.length() > 10)
            return myString.substring(myString.length() - 10);
        else
            return myString;
    }






    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void shownotification(String message, String title, String category) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.noti1)
                        .setContentTitle(title)
                        .setContentText(message).setCategory(category);

        Intent notificationIntent = new Intent(this, SplashScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

}