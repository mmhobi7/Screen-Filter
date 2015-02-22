package com.aaahh.yellow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Aaahh on 8/26/14. Edited 9/13/14
 */
public class FilterService extends Service {
    public static FilterService mThis;
    public static View vw;
    public static View vw2;
    public static View vw3;
    private static WindowManager.LayoutParams localLayoutParams;
    private static WindowManager localWindowManager;
    private static WindowManager.LayoutParams localLayoutParams2;
    private static WindowManager localWindowManager2;
    private static WindowManager.LayoutParams localLayoutParams3;
    private static WindowManager localWindowManager3;
    private final IBinder rBinder = new LocalBinder();
    private final Handler mHandler = new Handler();
    private int buttonToggle;
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("android.intent.action.CONFIGURATION_CHANGED")) {
                Common.O = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                FilterService.mThis.setRotation();
            }
            if (intent.getAction().equalsIgnoreCase("eu.chainfire.supersu.extra.HIDE")) {
                vw.getBackground().setAlpha(0);
                vw2.getBackground().setAlpha(0);
                vw3.getBackground().setAlpha(0);
                localLayoutParams.height = 1;
                localLayoutParams.width = 1;
                localLayoutParams2.height = 1;
                localLayoutParams2.width = 1;
                localLayoutParams3.height = 1;
                localLayoutParams3.width = 1;
                localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);
                localWindowManager2.updateViewLayout(FilterService.vw2, FilterService.localLayoutParams2);
                localWindowManager3.updateViewLayout(FilterService.vw3, FilterService.localLayoutParams3);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        setRotation();
                    }
                }, 15000);
            }
            if (intent.getAction().equalsIgnoreCase("com.aaahh.yellow.Toggle")) {
                if (Common.toggle) {
                    setRotation();
                    buttonToggle = android.R.drawable.ic_media_play;
                    Common.toggle = false;
                } else {
                    vw.getBackground().setAlpha(0);
                    vw2.getBackground().setAlpha(0);
                    vw3.getBackground().setAlpha(0);
                    localLayoutParams.height = 1;
                    localLayoutParams.width = 1;
                    localLayoutParams2.height = 1;
                    localLayoutParams2.width = 1;
                    localLayoutParams3.height = 1;
                    localLayoutParams3.width = 1;
                    localWindowManager.updateViewLayout(FilterService.vw, FilterService.localLayoutParams);
                    localWindowManager2.updateViewLayout(FilterService.vw2, FilterService.localLayoutParams2);
                    localWindowManager3.updateViewLayout(FilterService.vw3, FilterService.localLayoutParams3);
                    buttonToggle = android.R.drawable.ic_media_pause;
                    Common.toggle = true;
                }
            }
        }
    };

    public void addView(int layer) {
        if (layer == 1) {
            vw = new View(this);
            localLayoutParams = new WindowManager.LayoutParams(1, 1, 2006, 1288, -3);
            localWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            vw.setRotation(0);
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | PixelFormat.RGBA_8888;
            localWindowManager.addView(vw, localLayoutParams);
        }
        if (layer == 2) {
            vw2 = new View(this);
            localLayoutParams2 = new WindowManager.LayoutParams(1, 1, 2006, 1288, -3);
            localWindowManager2 = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            vw2.setRotation(0);
            localLayoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | PixelFormat.RGBA_8888;
            localWindowManager2.addView(vw2, localLayoutParams2);
        }
        if (layer == 3) {
            vw3 = new View(this);
            localLayoutParams3 = new WindowManager.LayoutParams(1, 1, 2006, 1288, -3);
            localWindowManager3 = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            vw3.setRotation(0);
            localLayoutParams3.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    | PixelFormat.RGBA_8888;
            localWindowManager3.addView(vw3, localLayoutParams3);
        }
        if (!Common.Notif) {
            Notification();
        }
        setRotation();
        rotationReceiver();
    }

    public void endNotification() {
        startForeground(0, new Notification());
        if (Common.Hide) {
            if (!(Common.Boot)) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Hide", true);
                editor.apply();
                Common.Hide = true;
                PackageManager packageManager = this.getPackageManager();
                ComponentName componentName = new ComponentName(this,
                        Launcher.class);
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }

    public IBinder onBind(Intent paramIntent) {
        return this.rBinder;
    }

    public void onCreate() {
        super.onCreate();
        mThis = this;
    }

    public void onDestroy() {
        super.onDestroy();
        removeView();
    }

    public void removeView() {
        if (vw != null) {
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(vw);
        }
        vw = null;
    }

    public void setAlpha(int paramInt) {
        if (vw == null) {
            return;
        }
        vw.getBackground().setAlpha(paramInt);
    }

    void setRotation() {
        if (!(vw == null)) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
            float screenWidth = displaymetrics.widthPixels;
            float screenHeight = displaymetrics.heightPixels;
            int i = Common.Color;
            String hexColor = String.format("#%06X", (0xFFFFFF & i));
            String fade = hexColor.replace("#", "#00");
            GradientDrawable gt;
            int height = Common.Height;
            int area = Common.Area;
            if (Common.O == 0) {
                if (Common.Gradient > (-1)) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                        localLayoutParams.y = 0;
                    }
                    vw.setBackground(gt);
                } else {
                    localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                    localLayoutParams.width = (int) screenWidth;
                    localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    localLayoutParams.x = 0;
                    vw.setBackgroundColor(i);
                }
            }
            if (Common.O == 1) {
                if (Common.Gradient > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) (((((area - 75) * 2) / 100f)) * (screenWidth / 2));
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    vw.setBackground(gt);
                } else {
                    localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams.height = (int) screenHeight;
                    localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                    localLayoutParams.y = 0;
                    vw.setBackgroundColor(i);
                }
            }
            if (Common.O == 2) {
                if (Common.Gradient > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.y = 0;
                        localLayoutParams.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    }
                    vw.setBackground(gt);
                } else {
                    localLayoutParams.height = ((int) ((height / 100f) * screenHeight));
                    localLayoutParams.width = (int) screenWidth;
                    localLayoutParams.x = 0;
                    localLayoutParams.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    vw.setBackgroundColor(i);
                }
            }
            if (Common.O == 3) {
                if (Common.Gradient > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) (((((((area - 75) * 2) / 100f)) * (screenWidth / 2))) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.x = 0;
                    }
                    vw.setBackground(gt);
                } else {
                    localLayoutParams.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams.height = (int) screenHeight;
                    localLayoutParams.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                    localLayoutParams.y = 0;
                    vw.setBackgroundColor(i);
                }
            }
            localWindowManager.updateViewLayout(vw, localLayoutParams);
            vw.getBackground().setAlpha(Common.Alpha);
        }
        if (!(vw2 == null)) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
            float screenWidth = displaymetrics.widthPixels;
            float screenHeight = displaymetrics.heightPixels;
            int i = Common.Color2;
            String hexColor = String.format("#%06X", (0xFFFFFF & i));
            String fade = hexColor.replace("#", "#00");
            GradientDrawable gt;

            int height = Common.Height2;
            int area = Common.Area2;

            if (Common.O == 0) {
                if (Common.Gradient2 > (-1)) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient2 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams2.x = 0;
                    }
                    if (Common.Gradient2 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams2.x = 0;
                    }
                    if (Common.Gradient2 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams2.x = 0;
                    }
                    if (Common.Gradient2 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                        localLayoutParams2.y = 0;
                    }
                    vw2.setBackground(gt);
                } else {
                    localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                    localLayoutParams2.width = (int) screenWidth;
                    localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    localLayoutParams2.x = 0;
                    vw2.setBackgroundColor(i);
                }
            }
            if (Common.O == 1) {
                if (Common.Gradient2 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient2 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) (((((area - 75) * 2) / 100f)) * (screenWidth / 2));
                        localLayoutParams2.x = 0;
                    }
                    if (Common.Gradient2 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams2.x = 0;
                    }
                    vw2.setBackground(gt);
                } else {
                    localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams2.height = (int) screenHeight;
                    localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                    localLayoutParams2.y = 0;
                    vw2.setBackgroundColor(i);
                }
            }
            if (Common.O == 2) {
                if (Common.Gradient2 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient2 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams2.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.x = 0;
                        localLayoutParams2.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient2 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams2.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.x = 0;
                        localLayoutParams2.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient2 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams2.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.x = 0;
                        localLayoutParams2.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient2 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams2.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.y = 0;
                        localLayoutParams2.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient2 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams2.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = 0;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    }
                    vw2.setBackground(gt);
                } else {
                    localLayoutParams2.height = ((int) ((height / 100f) * screenHeight));
                    localLayoutParams2.width = (int) screenWidth;
                    localLayoutParams2.x = 0;
                    localLayoutParams2.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    vw2.setBackgroundColor(i);
                }
            }
            if (Common.O == 3) {
                if (Common.Gradient2 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient2 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams2.height = (int) screenHeight;
                        localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams2.y = 0;
                    }
                    if (Common.Gradient2 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) (((((((area - 75) * 2) / 100f)) * (screenWidth / 2))) * -1);
                        localLayoutParams2.x = 0;
                    }
                    if (Common.Gradient2 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams2.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams2.width = (int) screenWidth;
                        localLayoutParams2.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams2.x = 0;
                    }
                    vw2.setBackground(gt);
                } else {
                    localLayoutParams2.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams2.height = (int) screenHeight;
                    localLayoutParams2.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                    localLayoutParams2.y = 0;
                    vw2.setBackgroundColor(i);
                }
            }
            localWindowManager2.updateViewLayout(vw2, localLayoutParams2);
            vw2.getBackground().setAlpha(Common.Alpha2);
        }
        if (!(vw3 == null)) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
            float screenWidth = displaymetrics.widthPixels;
            float screenHeight = displaymetrics.heightPixels;
            int i = Common.Color3;
            String hexColor = String.format("#%06X", (0xFFFFFF & i));
            String fade = hexColor.replace("#", "#00");
            GradientDrawable gt;
            int height = Common.Height;
            int area = Common.Area;
            if (Common.Layer == 2) {
                height = Common.Height2;
                area = Common.Area2;
            }
            if (Common.Layer == 3) {
                height = Common.Height3;
                area = Common.Area3;
            }
            if (Common.O == 0) {
                if (Common.Gradient3 > (-1)) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient3 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams3.x = 0;
                    }
                    if (Common.Gradient3 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams3.x = 0;
                    }
                    if (Common.Gradient3 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams3.x = 0;
                    }
                    if (Common.Gradient3 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                        localLayoutParams3.y = 0;
                    }
                    vw3.setBackground(gt);
                } else {
                    localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                    localLayoutParams3.width = (int) screenWidth;
                    localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    localLayoutParams3.x = 0;
                    vw3.setBackgroundColor(i);
                }
            }
            if (Common.O == 1) {
                if (Common.Gradient3 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient3 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) (((((area - 75) * 2) / 100f)) * (screenWidth / 2));
                        localLayoutParams3.x = 0;
                    }
                    if (Common.Gradient3 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams3.x = 0;
                    }
                    vw3.setBackground(gt);
                } else {
                    localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams3.height = (int) screenHeight;
                    localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                    localLayoutParams3.y = 0;
                    vw3.setBackgroundColor(i);
                }
            }
            if (Common.O == 2) {
                if (Common.Gradient3 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient3 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams3.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.x = 0;
                        localLayoutParams3.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient3 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams3.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.x = 0;
                        localLayoutParams3.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient3 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams3.height = ((int) ((height / 100f) * screenHeight));
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.x = 0;
                        localLayoutParams3.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient3 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams3.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.y = 0;
                        localLayoutParams3.x = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient3 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams3.width = ((int) ((height / 100f) * screenWidth));
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = 0;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    }
                    vw3.setBackground(gt);
                } else {
                    localLayoutParams3.height = ((int) ((height / 100f) * screenHeight));
                    localLayoutParams3.width = (int) screenWidth;
                    localLayoutParams3.x = 0;
                    localLayoutParams3.y = (int) (((((area - 75) * 2) / 100f)) * (screenHeight / 2));
                    vw3.setBackgroundColor(i);
                }
            }
            if (Common.O == 3) {
                if (Common.Gradient3 > -1) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient3 == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                        localLayoutParams3.height = (int) screenHeight;
                        localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams3.y = 0;
                    }
                    if (Common.Gradient3 == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) (((((((area - 75) * 2) / 100f)) * (screenWidth / 2))) * -1);
                        localLayoutParams3.x = 0;
                    }
                    if (Common.Gradient3 == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        localLayoutParams3.height = (int) ((height / 100f) * screenHeight);
                        localLayoutParams3.width = (int) screenWidth;
                        localLayoutParams3.y = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams3.x = 0;
                    }
                    vw3.setBackground(gt);
                } else {
                    localLayoutParams3.width = (int) ((height / 100f) * screenWidth);
                    localLayoutParams3.height = (int) screenHeight;
                    localLayoutParams3.x = (int) ((((((area - 75) * 2) / 100f)) * (screenWidth / 2)));
                    localLayoutParams3.y = 0;
                    vw3.setBackgroundColor(i);
                }
            }
            localWindowManager3.updateViewLayout(vw3, localLayoutParams3);
            vw3.getBackground().setAlpha(Common.Alpha3);
        }
    }

    public void Notification() {
        Intent localIntent = new Intent(getApplicationContext(), MainActivity.class);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent localPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, localIntent, 0);
        Intent deleteIntent = new Intent("com.aaahh.yellow.Toggle");
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager n = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification localNotification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buttonToggle = Common.toggle
                    ? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause;
            localNotification = new Notification.Builder(this)
                    .setContentTitle("Filter Screen")
                    .setContentText("Activated")
                    .addAction(buttonToggle, "Toggle", pendingIntentCancel)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(localPendingIntent)
                    .setStyle(new Notification.MediaStyle()
                            .setShowActionsInCompactView(0))
                            //  .setColor(Color.rgb(223,223,223))
                    .setColor(Color.rgb(33, 150, 243))
                    .setPriority(-2)
                    .build();
        } else {
            localNotification = new Notification.Builder(this)
                    .setContentTitle("Filter Screen")
                    .setContentText("Activated")
                    .addAction(android.R.drawable.ic_media_pause, "Toggle", pendingIntentCancel)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(localPendingIntent)
                    .setPriority(-2)
                    .build();
        }
        n.notify(1, localNotification);
        startForeground(1, localNotification);
        Common.Notif = true;
        if (Common.ToHide) {
            if (!(Common.Hide)) {
                Common.Hide = true;
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Hide", true);
                editor.apply();
                Common.Hide = true;
                PackageManager packageManager = this.getPackageManager();
                ComponentName componentName = new ComponentName(this,
                        Launcher.class);
                packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }

    void rotationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("eu.chainfire.supersu.extra.HIDE");
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        filter.addAction("com.aaahh.yellow.Toggle");
        if (Common.Receiver) {
            registerReceiver(myReceiver, filter);
        }
        if (!Common.Receiver) {
            unregisterReceiver(myReceiver);
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        FilterService getService() {
            return FilterService.this;
        }
    }
}