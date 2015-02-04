package com.aaahh.yellow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by aaahh on 1/30/15.
 * TODO: Optimize...
 */
public class Toggle extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Common.toggle) {
            FilterService.vw.getBackground().setAlpha(Common.Alpha);
            FilterService.localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);

            Common.toggle = false;
        } else {
            FilterService.vw.getBackground().setAlpha(0);
            FilterService.localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);
            Common.toggle = true;
        }
    }
}