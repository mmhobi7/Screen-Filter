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
import android.os.Handler;

public class HideOverlaysReceiver extends BroadcastReceiver {
    public static final String ACTION_HIDE_OVERLAYS = "eu.chainfire.supersu.action.HIDE_OVERLAYS";
    public static final String CATEGORY_HIDE_OVERLAYS = Intent.CATEGORY_INFO;
    public static final String EXTRA_HIDE_OVERLAYS = "eu.chainfire.supersu.extra.HIDE";
    private Handler mHandler = new Handler();

    @Override
    public final void onReceive(Context context, Intent intent) {
        final int H = FilterService.localLayoutParams.height;
        final int W = FilterService.localLayoutParams.width;
        FilterService.localLayoutParams.height = 1;
        FilterService.localLayoutParams.width = 1;
        FilterService.localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);
        mHandler.postDelayed(new Runnable() {
            public void run() {
                FilterService.localLayoutParams.height = H;
                FilterService.localLayoutParams.width = W;
                FilterService.localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);
            }
        }, 15000);
    }
}