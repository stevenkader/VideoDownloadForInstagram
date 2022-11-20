package com.video.downloader.ig.reels.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {


    @Override
    public void onReceive(Context ctx, Intent intent) {
        try {
            Log.d("VideoDownloaderIG", "Received broadcast");
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

                Log.d("VideoDownloaderIG", "starting clipboard service ");

/**
 if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

 Intent myService = new Intent(ctx, ClipboardListenerService.class);
 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 ContextCompat.startForegroundService(ctx, myService);
 } else {
 ctx.startService(myService);
 }
 }
 **/

            }
        } catch (Exception e) {
        }

    }

}
