package com.kr.android.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.kr.android.R;
/**
*  푸쉬 알람 설정
* @author LCY
* @version 1.0.0
* @since 2020-07-07 오전 11:22
**/

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("TOKEN",mToken);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(3000);


        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        sendNotification(title, body);


    }

    private void sendNotification(String title, String messageBody) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            /**
             * 누가버전 이하 노티처리
             */
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setWhen(System.currentTimeMillis());

            NotificationManager notificationManager =

                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /**
             * 오레오 이상 노티처리
             */
            /**
             * 오레오 버전부터 노티를 처리하려면 채널이 존재해야합니다.
             */

            int importance = NotificationManager.IMPORTANCE_HIGH;
            String Noti_Channel_ID = "Noti";
            String Noti_Channel_Group_ID = "Noti_Group";

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID, Noti_Channel_Group_ID, importance);

//                    notificationManager.deleteNotificationChannel("testid"); 채널삭제

            /**
             * 채널이 있는지 체크해서 없을경우 만들고 있으면 채널을 재사용합니다.
             */
            if (notificationManager.getNotificationChannel(Noti_Channel_ID) != null) {
                Log.wtf("getNotificationChannel", "채널이 이미 존재합니다.");
            } else {
                Log.wtf("getNotificationChannel", "채널이 없어서 만듭니다.");
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setWhen(System.currentTimeMillis())
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    ;

            notificationManager.notify(0, builder.build());

        }


    }


}
