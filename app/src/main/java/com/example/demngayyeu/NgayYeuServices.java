package com.example.demngayyeu;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class NgayYeuServices extends Service {
    Calendar calendar;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ngayDaChon = intent.getStringExtra("inputExtra");
        int ngay = Integer.parseInt(ngayDaChon.substring(0,2));
        int thang = Integer.parseInt(ngayDaChon.substring(3,5));
        int nam = Integer.parseInt(ngayDaChon.substring(6,10));
        calendar = Calendar.getInstance();
        calendar.set(nam,thang-1,ngay);
        long ngayYeu = (Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis())/(1000*60*60*24);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Số ngày yêu của 2 bạn <3")
                .setContentText(String.valueOf(ngayYeu))
                .setSmallIcon(R.drawable.tim4)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);



        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    long ngayYeu = (Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis())/(1000*60*60*24);
                    try {
                        Thread.sleep(2000*60*60);
                        Log.d("BBB", "Test2");
                        Intent notificationIntent = new Intent(NgayYeuServices.this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(NgayYeuServices.this,
                                0, notificationIntent, 0);

                        Notification notification = new NotificationCompat.Builder(NgayYeuServices.this, App.CHANNEL_ID)
                                .setContentTitle("Số ngày yêu của 2 bạn <3")
                                .setContentText(String.valueOf(ngayYeu))
                                .setSmallIcon(R.drawable.tim4)
                                .setContentIntent(pendingIntent)
                                .build();
                        startForeground(1, notification);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
