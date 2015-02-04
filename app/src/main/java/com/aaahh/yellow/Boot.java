package com.aaahh.yellow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Aaahh on 10/9/14.
 * Starts the whole app
 */
public class Boot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(Common.PREFS_NAME, 0);
        if (settings.getBoolean("Boot", Common.Boot)) {
            Common.BootNow = true;
            context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            this.abortBroadcast();
        }
    }
}