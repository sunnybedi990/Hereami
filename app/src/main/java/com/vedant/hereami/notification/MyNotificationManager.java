package com.vedant.hereami.notification;

/**
 * Created by sunnybedi on 14/03/17.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.R;
import com.vedant.hereami.database.DBHelper;
import com.vedant.hereami.database.messagedatabse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;




public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public final int ID_SMALL_NOTIFICATION = 235;
    private int notificationIdOne = 111;
    private int notificationIdTwo = 112;
    private int numMessagesOne = 0;
    private int numMessagesTwo = 0;
    private String entityid;
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    //  private String senderMessage;
    //  private Firebase mFirebaseMessagesChatreceipent;

    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private Context mCtx;
    private EditText mUserMessageChatText;
    public int id = new Random().nextInt();
    private Notification notification;
    private NotificationManager notificationManager;
    private String part2;
    private int notificationNumber;
    private Intent intent2;
    private PendingIntent pendingIntent;
    private RemoteInput remoteInput;
    private static final String mypreference123 = "mypref123";
    private String myencryptionkey;
    public static boolean exampleBool = false;



    //New work
    private static final String GROUP_KEY = "Messenger";
    private static final String MESSAGES_KEY = "Messages";
    private static final String NOTIFICATION_ID = "com.stylingandroid.nougat.NOTIFICATION_ID";
    private static final int SUMMARY_ID = 0;
    private static final String EMPTY_MESSAGE_STRING = "[]";
    private messagedatabse mydb;
    private String encrytionprivatekey;
    private String ts;
    private String imagetype;


    // here it ends


    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent, String tendigitnumber) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.noti).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.drawable.noti)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.image))
                .setContentText(message).setPriority(Notification.PRIORITY_MAX)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification

    public static boolean returnExampleBool() {

        return exampleBool;
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.noti1 : R.drawable.noti;
    }

    public static String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory();

        File folder = new File(sdcard.getPath(), "/HereamI/pics");//the dot makes this directory hidden to the user
        folder.mkdir();
        File file = new File(folder.getPath(), filename + ".jpg");
        if (file.exists())
            return stored;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showSmallNotification(final String title, String messages, Intent intent, String title8, String titlenum, final String tendigitnumber, final String timestamp, final String sender, final String messa, final String message1, final String imageurl) {

        mydb = new messagedatabse(mCtx);
        final DBHelper mydbhelper = new DBHelper(mCtx);
        part2 = messages;
        id++;
        entityid = tendigitnumber;

        // id = idtaken;
        //    Log.e("notee", String.valueOf(tendigitnumber));
        //   id = Integer.valueOf(part2);
        //   int suaa = Integer.parseInt(part2);
        SharedPreferences prefs = mCtx.getSharedPreferences(MyNotificationManager.class.getSimpleName(), Context.MODE_PRIVATE);
        //   notificationNumber = prefs.getInt("notificationNumber", Integer.parseInt(titlenum));
        final SharedPreferences sharedpreferences = mCtx.getSharedPreferences(mypreference123, Context.MODE_PRIVATE);
        //    add data to recent chat when message comes
        myencryptionkey = sharedpreferences.getString("publickey", "");
        encrytionprivatekey = sharedpreferences.getString("privatekey", "");
        //  Log.e("title", title);
        //  Log.e("title8", title8);
        //  Log.e("sender", sender);
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //  Log.e("pass","passed notification manager");
                ArrayList<String> arrTblNames = new ArrayList<String>();
                SQLiteDatabase db = mydb.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        arrTblNames.add(c.getString(c.getColumnIndex("name")));
                        c.moveToNext();
                    }
                }
                imagetype = "text";
                if (!imageurl.equalsIgnoreCase("null")) {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    ts = tsLong.toString();
                    imagetype = "image";

                    URL url = null;
                    try {
                        url = new URL(imageurl);
                        URLConnection conn = url.openConnection();
                        Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());

                        saveToSdCard(bitmap, ts);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                String messagedecrypted = CallsFragment.decryptRSAToString(messa, encrytionprivatekey);

                if (arrTblNames.contains("table" + tendigitnumber)) {
                    Log.e("imageurl", imageurl);
                    Log.e("imagetype", imagetype);
                    if (mydb.insertContact(messa, message1, timestamp, sender, tendigitnumber, imageurl, ts, imagetype)) {
                        Intent intent1 = new Intent("message").putExtra("message1", tendigitnumber);
                        LocalBroadcastManager.getInstance(mCtx).sendBroadcast(intent1);
                        mydbhelper.updatemsgs(tendigitnumber, messagedecrypted, timestamp);
                        //    message mess = new message();
                        //  mess.setviewupdation(1);
                        //      Log.e("pass","passed table12 ");
                        //  Log.e("example", String.valueOf(exampleBool));
                    }


                } else {
                    mydb.AddDesiredTable(tendigitnumber);
                    if (mydb.insertContact(messa, message1, timestamp, sender, tendigitnumber, imageurl, ts, imagetype)) {
                        Intent intent1 = new Intent("message").putExtra("message1", tendigitnumber);
                        LocalBroadcastManager.getInstance(mCtx).sendBroadcast(intent1);
                        //   message mess = new message();
                        // mess.setviewupdation(1);
                        //     Log.e("pass","passed table ");
                    }
                    mydbhelper.insertContact(title, sender, tendigitnumber, messagedecrypted, timestamp);
                }
                //   Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }


        }, 1000);

        String replyLabel = mCtx.getResources().getString(R.string.reply_label);
        remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        pendingIntent = PendingIntent.getActivity(mCtx, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent2 = new Intent(mCtx, notifyme.class).putExtra("key_position", title8).putExtra("KEY_NOTIFICATION_ID", id).putExtra("tag", sender).putExtra("publickmykey", myencryptionkey).putExtra("number", tendigitnumber);
            Log.e("notee", String.valueOf(id));
        } else {
            intent2 = intent;
            //   notificationManager.cancel(id);
            //  nMgr.cancel(id);

        }

        // mCtx.sendBroadcast(intent2);
        PendingIntent resultPendingIntent =
                PendingIntent.getBroadcast(
                        mCtx,
                        0, intent2, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_action_stat_reply, "reply to " + title,
                resultPendingIntent).addRemoteInput(remoteInput).setAllowGeneratedReplies(true).build();


        String filepath = Environment.getExternalStorageDirectory().getPath();
        File myDir = new File(filepath + "/HereamI");
        Bitmap bMap = BitmapFactory.decodeFile(myDir + "/" + title + ".jpg");
        //   notificationManager = NotificationManagerCompat.from(mCtx);
        notificationManager = (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, NOTIFICATION_CHANNEL_ID);

        mBuilder.setTicker(title).setShowWhen(true).setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .addAction(action).setColor(Color.RED)
                .setSmallIcon(getNotificationIcon()).setPriority(Notification.PRIORITY_MAX)
                .setContentText(messages);
        //  firstTime = false;

        notification = mBuilder.build();

        if (notification != null) {
            notification.defaults |= Notification.DEFAULT_SOUND;

            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;


            notificationManager.notify(id, notification);


            SharedPreferences.Editor editor = prefs.edit();
            notificationNumber++;

            editor.putInt("notificationNumber", notificationNumber);
            editor.commit();


        }


    }

}