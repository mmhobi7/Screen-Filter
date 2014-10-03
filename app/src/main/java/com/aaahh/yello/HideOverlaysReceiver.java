/*
* Copyright (C) 2012-2014 Jorrit "Chainfire" Jongma
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.aaahh.yello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class HideOverlaysReceiver extends BroadcastReceiver {
    public static final String ACTION_HIDE_OVERLAYS = "eu.chainfire.supersu.action.HIDE_OVERLAYS";
    public static final String CATEGORY_HIDE_OVERLAYS = Intent.CATEGORY_INFO;
    public static final String EXTRA_HIDE_OVERLAYS = "eu.chainfire.supersu.extra.HIDE";

    @Override
    public final void onReceive(Context context, Intent intent) {
        Log.d("Screen-Filter", "anti-tapkjack");
        if (Common.FilterYN.contains("Y")) {
            Log.d("Screen-Filter", "Overlay for anti-tapkjack");
            FilterService.setAlpha(0);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FilterService.setAlpha(Common.Alpha);
        }
    }
}