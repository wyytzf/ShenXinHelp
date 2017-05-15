package com.xd.shenxinhelp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.xd.shenxinhelp.R;

/**
 * Created by weiyuyang on 17-5-13.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("wyy", "receive start");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.mipmap.default_head_image, "玩手机超过1个小时啦，注意保护眼睛！", System.currentTimeMillis());
//        notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.default_head_image).setContentTitle("身心帮")
                .setAutoCancel(true)
                .setContentText("休息下眼睛吧")
                .setDefaults(Notification.DEFAULT_ALL);
        manager.notify(1, builder.build());

        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
