package com.aaahh.yello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aaahh on 10/9/14.
 * Starts the whole app
 */
public class Boot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("m", "pie");
        Intent serviceIntent = new Intent(context, Minimal.class);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(serviceIntent);

    }
}