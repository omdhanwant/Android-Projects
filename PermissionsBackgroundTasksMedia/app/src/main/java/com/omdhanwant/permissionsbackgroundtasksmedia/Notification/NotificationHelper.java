package com.omdhanwant.permissionsbackgroundtasksmedia.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.omdhanwant.permissionsbackgroundtasksmedia.R;


public class NotificationHelper {
    Context mContext;
    NotificationManager manager;
    final static String Channel1 = "Channel 1";
    final static String Channel2 = "Channel 2";

    final static int CHANNEL1_ID = 1001;
    final static int CHANNEL2_ID = 1002;



    public NotificationHelper(Context context) {
        mContext = context;

        // create channels
        createNotificationChannel(Channel1);
        createNotificationChannel(Channel2);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel getNotification(String ChannelId) {
        NotificationChannel channel = null;
        if(manager != null) {

            if(ChannelId.equalsIgnoreCase(Channel1)) {
                manager.getNotificationChannel(Channel1);
            }
            else if(ChannelId.equalsIgnoreCase(Channel2)) {
                manager.getNotificationChannel(Channel2);
            }

        }

        return channel;
    }

    private void createNotificationChannel(String channelName) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelName, channelName, importance);
            channel.setDescription(channelName);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

}
