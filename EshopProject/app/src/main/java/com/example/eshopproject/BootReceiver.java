package com.example.eshopproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {


    public BootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("onReceive", "started");

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Log.d("Boot completed", "code received, starting service");
            Intent serviceIntent = new Intent(context, NotificationService.class);
            context.startService(serviceIntent);

        }
    }
}
