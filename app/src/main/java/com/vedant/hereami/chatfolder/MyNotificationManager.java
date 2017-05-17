package com.vedant.hereami.chatfolder;

/**
 * Created by sunnybedi on 14/03/17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.vedant.hereami.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Ravi on 31/03/15.
 */

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public final int ID_SMALL_NOTIFICATION = 235;
    private int notificationIdOne = 111;
    private int notificationIdTwo = 112;
    private int numMessagesOne = 0;
    private int numMessagesTwo = 0;
    //  private String senderMessage;
    //  private Firebase mFirebaseMessagesChatreceipent;

    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private Context mCtx;
    private EditText mUserMessageChatText;
    public int id;
    private Notification notification;
    private NotificationManager notificationManager;
    private String part2;
    private int notificationNumber;
    private Intent intent2;
    private PendingIntent pendingIntent;
    private RemoteInput remoteInput;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
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
        notification = mBuilder.setSmallIcon(R.drawable.image).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.drawable.image)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.image))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent, String title8, String titlenum) {


        part2 = message;

        //   id = Integer.valueOf(part2);
        //   int suaa = Integer.parseInt(part2);
        SharedPreferences prefs = mCtx.getSharedPreferences(MyNotificationManager.class.getSimpleName(), Context.MODE_PRIVATE);
        //   notificationNumber = prefs.getInt("notificationNumber", Integer.parseInt(titlenum));


        Log.e("notee", titlenum);

        String replyLabel = mCtx.getResources().getString(R.string.reply_label);
        remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        pendingIntent = PendingIntent.getActivity(mCtx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent2 = new Intent(mCtx, notifyme.class).putExtra("key_position", title8).putExtra("KEY_NOTIFICATION_ID", id).putExtra("tag", message);
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
        File myDir = new File(filepath + "/.HereamI");
        Bitmap bMap = BitmapFactory.decodeFile(myDir + "/" + title + ".jpg");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);


        mBuilder.setSmallIcon(R.drawable.image).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .addAction(action)
                    .setSmallIcon(R.drawable.image)

                    .setLargeIcon(bMap).setAutoCancel(true)

                    .setContentText(message).setNumber(++numMessagesOne);
            //  firstTime = false;

        notification = mBuilder.build();
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notification != null) {
            notification.defaults |= Notification.DEFAULT_SOUND;
            //    noti.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //  long[] pattern = {500, 500, 500};
            //  noti.vibrate = pattern;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//id = Integer.valueOf(part2);
            notificationManager.notify(part2, id, notification);
            SharedPreferences.Editor editor = prefs.edit();
            notificationNumber++;
            editor.putInt("notificationNumber", notificationNumber);
            editor.commit();
        }
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



}