package com.aaahh.yello;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by aaahh on 10/14/14. mango
 */
public class Minimal extends Service {
    public static Minimal mThis;
    public static View vw;
    public static GradientDrawable gt;
    public static WindowManager.LayoutParams localLayoutParams;
    public static WindowManager localWindowManager;
    public int Area;
    public int Alpha;
    public int Height;
    public int Color;
    public boolean FilterYN;
    public int Gradient;
    public int O;
    public boolean Notif = false;
    //dont ask why \/
    public boolean Receiver = true;
    private Handler mHandler = new Handler();
    public final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("android.intent.action.CONFIGURATION_CHANGED")) {
                O = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                Minimal.mThis.setRotation();
            }
            if (intent.getAction().equalsIgnoreCase("eu.chainfire.supersu.extra.HIDE")) {
                final int H = Minimal.localLayoutParams.height;
                final int W = Minimal.localLayoutParams.width;
                Minimal.localLayoutParams.height = 1;
                Minimal.localLayoutParams.width = 1;
                Minimal.localWindowManager.updateViewLayout(Minimal.vw, Minimal.localLayoutParams);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        Minimal.localLayoutParams.height = H;
                        Minimal.localLayoutParams.width = W;
                        Minimal.localWindowManager.updateViewLayout(Minimal.vw, Minimal.localLayoutParams);
                    }
                }, 15000);
            }
        }
    };
    private final IBinder rBinder = new LocalBinder();

    public void addView() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        float screenWidth = displaymetrics.widthPixels;
        float screenHeight = displaymetrics.heightPixels;
        vw = new View(this);
        localLayoutParams = new WindowManager.LayoutParams((int) screenWidth, (int) screenHeight, 2006, 1288, -3);
        localWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        localLayoutParams.height = (int) ((Height / 100f) * screenHeight);
        localLayoutParams.width = (int) screenWidth;
        localLayoutParams.y = (int) ((((((Area - 50) * 2) / 100f)) * (screenHeight / 2)) * -1);
        localLayoutParams.x = 0;
        int i = Color;
        String hexColor = String.format("#%06X", (0xFFFFFF & i));
        String fade = hexColor.replace("#", "#00");
        if (Gradient > 0) {
            int b = (android.graphics.Color.parseColor(fade));
            gt = new GradientDrawable();
            if (Gradient == 1) {
                int colors[] = {b, i};
                gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                gt.setColors(colors);
            }
            if (Gradient == 2) {
                int colors[] = {b, i, b};
                gt.setColors(colors);
            }
            if (Gradient == 3) {
                int colors[] = {b, i};
                gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                gt.setColors(colors);
            }
            vw.setBackground(gt);
        } else {
            vw.setBackgroundColor(i);
        }
        vw.getBackground().setAlpha(Alpha);
        vw.getBackground().setDither(true);
        vw.setRotation(0);
        localWindowManager.addView(vw, localLayoutParams);
        if (!Notif) {
            startNotification();
        }
        rotationReceiver();
    }

    public IBinder onBind(Intent paramIntent) {
        Log.d("mango", "m");
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        Area = settings.getInt("Area", 50);
        Alpha = 200 - (settings.getInt("Alpha", 50)) * 2;
        Height = settings.getInt("Height", 50);
        Color = settings.getInt("Color", 1);
        Gradient = settings.getInt("Gradient", 1);
        FilterYN = settings.getBoolean("FilterYN", true);
        addView();
        return this.rBinder;
    }

    public void onCreate() {
        Log.d("mango", "m2");
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        Area = settings.getInt("Area", 50);
        Alpha = 200 - (settings.getInt("Alpha", 50)) * 2;
        Height = settings.getInt("Height", 50);
        Color = settings.getInt("Color", 1);
        Gradient = settings.getInt("Gradient", 1);
        FilterYN = settings.getBoolean("FilterYN", true);
        addView();
        startNotification();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("FilterYN", true);
        editor.apply();
    }

    public void startNotification() {
        Intent localIntent = new Intent(getApplicationContext(), MainActivity.class);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent localPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, localIntent, 0);
        NotificationManager n = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        Notification localNotification = new Notification.Builder(this)
                .setContentTitle("Filter Screen")
                .setContentText("Activated")
                .setSmallIcon(android.R.drawable.ic_input_get)
                .setContentIntent(localPendingIntent)
                .setPriority(-2)
                .build();
        n.notify(1, localNotification);
        startForeground(1, localNotification);
        Notif = true;
    }

    public void rotationReceiver() {
        IntentFilter filter1 = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
        IntentFilter filter2 = new IntentFilter("eu.chainfire.supersu.extra.HIDE");
        if (Receiver) {
            registerReceiver(myReceiver, filter1);
            registerReceiver(myReceiver, filter2);
        }
        if (!Receiver) {
            unregisterReceiver(myReceiver);
        }
    }

    public void setRotation() {
        if (!(vw == null)) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
            float screenWidth = displaymetrics.widthPixels;
            float screenHeight = displaymetrics.heightPixels;
            int i = Color;
            String hexColor = String.format("#%06X", (0xFFFFFF & i));
            String fade = hexColor.replace("#", "#00");
            Log.d("2", String.valueOf(i));
            Log.d("h", String.valueOf(O));
            if (O == 0) {
                localLayoutParams.height = (int) ((Height / 100f) * screenHeight);
                localLayoutParams.width = (int) screenWidth;
                localLayoutParams.y = (int) ((((((Area - 50) * 2) / 100f)) * (screenHeight / 2)) * -1);
                localLayoutParams.x = 0;

                if (Gradient > 0) {
                    int b = (android.graphics.Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Gradient == 1) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                    }
                    if (Gradient == 2) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                    }
                    if (Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                    }
                    vw.setBackground(gt);
                } else {
                    vw.setBackgroundColor(i);
                }
            }
            if (O == 1) {
                localLayoutParams.width = (int) ((Height / 100f) * screenWidth);
                localLayoutParams.height = (int) screenHeight;
                localLayoutParams.x = (int) ((((((Area - 50) * 2) / 100f)) * (screenWidth / 2)) * -1);
                localLayoutParams.y = 0;

                if (Gradient > 0) {
                    int b = (android.graphics.Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Gradient == 1) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                    }
                    if (Gradient == 2) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    }
                    if (Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                    }
                    vw.setBackground(gt);
                } else {
                    vw.setBackgroundColor(i);
                }
            }
            if (O == 2) {
                localLayoutParams.height = ((int) ((Height / 100f) * screenHeight));
                localLayoutParams.width = (int) screenWidth;
                localLayoutParams.x = 0;
                localLayoutParams.y = (int) (((((Area - 50) * 2) / 100f)) * (screenHeight / 2) * -1);

                if (Gradient > 0) {
                    int b = (android.graphics.Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Gradient == 1) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                    }
                    if (Gradient == 2) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                    }
                    if (Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                    }
                    vw.setBackground(gt);
                } else {
                    vw.setBackgroundColor(i);
                }
            }
            if (O == 3) {
                localLayoutParams.width = (int) ((Height / 100f) * screenWidth);
                localLayoutParams.height = (int) screenHeight;
                localLayoutParams.x = (int) ((((((Area - 50) * 2) / 100f)) * (screenWidth / 2)));
                localLayoutParams.y = 0;
                Log.d("m", String.valueOf(screenHeight));
                if (Gradient > 0) {
                    int b = (android.graphics.Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Gradient == 1) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                    }
                    if (Gradient == 2) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                    }
                    if (Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                    }
                    vw.setBackground(gt);
                } else {
                    vw.setBackgroundColor(i);

                }
            }
            localWindowManager.updateViewLayout(vw, localLayoutParams);
            vw.getBackground().setAlpha(Alpha);
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        Minimal getService() {
            return Minimal.this;
        }
    }
}
