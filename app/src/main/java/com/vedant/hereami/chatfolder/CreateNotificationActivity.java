package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.vedant.hereami.R;
import com.vedant.hereami.notification.NotificationReceiverActivity;

public class CreateNotificationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
    }

    public void createNotification(View view) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                noti = new Notification.Builder(this)
                        .setContentTitle("New mail from " + "test@gmail.com")
                        .setContentText("Subject").setSmallIcon(R.drawable.image)
                        .setContentIntent(pIntent)
                        .addAction(R.drawable.image, "Call", pIntent)
                        .addAction(R.drawable.image, "More", pIntent)
                        .addAction(R.drawable.image, "And more", pIntent).build();
            }
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

}
