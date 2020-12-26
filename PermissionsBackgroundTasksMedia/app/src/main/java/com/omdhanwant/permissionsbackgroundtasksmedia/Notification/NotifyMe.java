package com.omdhanwant.permissionsbackgroundtasksmedia.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.omdhanwant.permissionsbackgroundtasksmedia.R;

public class NotifyMe extends AppCompatActivity {
    NotificationManager  manager;
    NotificationHelper mNotificationHelper;
    int NotificationId = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_me);

        Button button = (Button) findViewById(R.id.buNotify);
//        manager =  createNotificationChannel();
        mNotificationHelper = new NotificationHelper(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifyMe(View view) {
        NotificationId += 1;
       if(manager != null) {
           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Channel 1");
           mNotificationHelper.getNotification(NotificationHelper.Channel1);

           builder.setContentTitle("My Noti").setContentText("Hello").setSmallIcon(R.drawable.list_item_shape);
           manager.notify(NotificationId, builder.build());
       }

    }

//    private NotificationManager createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("Channel 1", "Channel 1", importance);
//            channel.setDescription("Channel 1");
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            return notificationManager;
//        }
//
//        return null;
//    }
}
