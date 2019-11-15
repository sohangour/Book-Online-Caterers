package com.example.sohancaterers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String LOGIN="Log In Successfullly";
    public static final String ORDER_PLACED="Order PLcaed";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    private void createNotification() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){


            NotificationChannel notificationChannel1=new NotificationChannel(LOGIN,"Login", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.setDescription("Login Successfuly");

            NotificationChannel notificationChannel2=new NotificationChannel(ORDER_PLACED,"order_placed", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel2.setDescription("Order Placed");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel1);
            manager.createNotificationChannel(notificationChannel2);
        }

    }
}
