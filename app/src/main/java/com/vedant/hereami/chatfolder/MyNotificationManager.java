package com.vedant.hereami.chatfolder;

/**
 * Created by sunnybedi on 14/03/17.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.text.Html;
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
    public void showSmallNotification(String title, String message, Intent intent, String title8) {
        Intent intent2 = new Intent(mCtx, chatactivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent.putExtra("key_position", title8).putExtra("namenumber", title),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        String replyLabel = mCtx.getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_action_stat_reply, "reply to " + title,

                pendingIntent).addRemoteInput(remoteInput).build();
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File myDir = new File(filepath + "/.HereamI");
        Bitmap bMap = BitmapFactory.decodeFile(myDir + "/" + title + ".jpg");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        //Different Id's will show up as different notifications
        int mNotificationId = 1;

        //Some things we only have to set the first time.
        boolean firstTime = true;
        int progress = 1;
        Notification notification;
        if (numMessagesOne == 0) {
            mBuilder.setSmallIcon(R.drawable.image).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .addAction(action)
                    .setSmallIcon(R.drawable.image)
                    .setLargeIcon(bMap).setAutoCancel(true)

                    .setContentText(message).setNumber(++numMessagesOne);
            //  firstTime = false;
        } else {
            mBuilder.setContentText(message).setNumber(++numMessagesOne);
        }


        notification = mBuilder.build();
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notification != null) {
            notification.defaults |= Notification.DEFAULT_SOUND;
            //    noti.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //  long[] pattern = {500, 500, 500};
            //  noti.vibrate = pattern;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
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


/*
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

            int hour = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            String tsTemp = String.format("%02d:%02d", hour, minutes);
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

            mFirebaseMessagesChatreceipent.push().setValue(newMessage, index);

            mUserMessageChatText.setText("");


        }
    }

    private void sendSinglePush() {
        final String title = user.getEmail().replace(".","dot")+user.getDisplayName();
        final String message = senderMessage;
        // final String image;

        String[] parts = message1.replace("+", ":").split(":"); // escape .
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
                params.put("title", title);
                params.put("message", message);

                //     if (!TextUtils.isEmpty(image))
                //       params.put("image", image);

                params.put("email", email);
                return params;

            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }
*/
}