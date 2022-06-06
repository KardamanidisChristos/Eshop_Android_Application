package com.example.eshopproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "11";
    private NotificationCompat.Builder notification_builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Notification service started", Toast.LENGTH_SHORT).show();
        Log.d("Notification service:", " started");
        //createNotification();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("Notification:", " created");
        Toast.makeText(this, "Notification Service created", Toast.LENGTH_SHORT).show();



        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startCheckingForOrders();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("Error", " in checking for new orders");
                }
            }
        });
        thread.start();



    }

    @Override
    public void onDestroy() {

        Log.d("Notification service" , " destroyed");
        Toast.makeText(this, "Notification Service destroyed", Toast.LENGTH_SHORT).show();
    }

    private void createNotification(int id, String orderType, String orderTime) {

        Log.d("Notification", " creating.....");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra("", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(getBaseContext(),"notification_id")
                .setSmallIcon(R.drawable.ic_icons8_box)
                .setContentTitle("New Order " + id)
                .setContentText("One new" + orderType + " order is shown still " + orderTime)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(id, notification);

    }

    private void startCheckingForOrders() throws InterruptedException {

        while (Thread.currentThread().isAlive()) {
            Log.d("Time====>", new Date().getTime() + "");

            Thread.sleep(5000);


            RestClient restClient = new RestClient();
            restClient.context = getApplicationContext();
            try {
                JSONObject jsonObject = restClient.doGetRequestSingleObject("https://192.168.1.70/ptyxiaki/index.php/api/v1/Customers/Orders/NewOrdersCheck");

                int newOrders = Integer.parseInt(jsonObject.get("newOrders").toString());

                Log.d("Orders===> ",  newOrders+ "");
                if (newOrders >0) {
                    Gson gson = new Gson();
                    JSONArray orders = restClient.doGetRequest("https://192.168.1.70/ptyxiaki/index.php/api/v1/Customers/Orders/New");
                    final Order[] ordersArray = gson.fromJson(orders.toString(), Order[].class);
                    for (Order o: ordersArray) {
                        Log.d("Order", "===>" + o.toString());
                        //Log.d("User detail", o.getUser().toString());
                    }
                    for (int i = 0; i < newOrders; i++) {
                        createNotification(i, ordersArray[i].getOrderType() ,ordersArray[i].getOrderTime());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
