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
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Aaahh on 8/26/14. Edited 9/13/14
 */
public class FilterService extends Service {
    public static View vw;
    private static WindowManager.LayoutParams localLayoutParams;
    private static WindowManager localWindowManager;
    private final IBinder rBinder = new LocalBinder();
    private static NotificationManager n;
    private Notification.Builder localNotification;
    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("com.aaahh.yellow.Toggle")) {
                if (Common.toggle) {
                    vw.getBackground().setAlpha(Common.Alpha);
                    localWindowManager.updateViewLayout(vw, FilterService.localLayoutParams);
                    Common.toggle = false;
                    NotificationBuilder();
                    n.notify(1, localNotification.build());
                } else {
                    vw.getBackground().setAlpha(0);
                    localWindowManager.updateViewLayout(vw, FilterService.localLayoutParams);
                    Common.toggle = true;
                    NotificationBuilder();
                    n.notify(1, localNotification.build());
                }
            }
        }
    };

    public void addView() {
        vw = new View(this);
        localLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN),
                PixelFormat.RGBA_8888);
        localWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        localWindowManager.addView(vw, localLayoutParams);
        if (!Common.Notif) {
            Notification();
        }
        setRotation(this, vw);
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

    static void setRotation(Context mContext, View vw) {
        if (!(vw == null)) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
            float screenWidth = displaymetrics.widthPixels;
            float screenHeight = displaymetrics.heightPixels;
            int i = Common.Color;
            String hexColor = String.format("#%06X", (0xFFFFFF & i));
            String fade = hexColor.replace("#", "#00");
            GradientDrawable gt;
            Common.O = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            if (Common.O == 0) {
                if (Common.Gradient > (-1)) {
                    int b = (Color.parseColor(fade));
                    gt = new GradientDrawable();
                    if (Common.Gradient == 0) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
                        localLayoutParams.y = 0;
                    }
                    vw.setBackground(gt);
                    vw.getBackground().setDither(true);
                } else {
                    localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                    localLayoutParams.width = (int) screenWidth;
                    localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
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
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2));
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                        localLayoutParams.x = 0;
                    }
                    vw.setBackground(gt);
                    vw.getBackground().setDither(true);
                } else {
                    localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                    localLayoutParams.height = (int) screenHeight;
                    localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
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
                        gt.setDither(true);
                        localLayoutParams.height = ((int) ((Common.Height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = ((int) ((Common.Height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = ((int) ((Common.Height / 100f) * screenHeight));
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = ((int) ((Common.Height / 100f) * screenWidth));
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.y = 0;
                        localLayoutParams.x = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = ((int) ((Common.Height / 100f) * screenWidth));
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = 0;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                    }
                    vw.setBackground(gt);
                    vw.getBackground().setDither(true);
                } else {
                    localLayoutParams.height = ((int) ((Common.Height / 100f) * screenHeight));
                    localLayoutParams.width = (int) screenWidth;
                    localLayoutParams.x = 0;
                    localLayoutParams.y = (int) (((((Common.Area - 75) * 2) / 100f)) * (screenHeight / 2));
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
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 1) {
                        int colors[] = {b, i, b};
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 2) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                        localLayoutParams.height = (int) screenHeight;
                        localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.y = 0;
                    }
                    if (Common.Gradient == 3) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) (((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2))) * -1);
                        localLayoutParams.x = 0;
                    }
                    if (Common.Gradient == 4) {
                        int colors[] = {b, i};
                        gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                        gt.setColors(colors);
                        gt.setDither(true);
                        localLayoutParams.height = (int) ((Common.Height / 100f) * screenHeight);
                        localLayoutParams.width = (int) screenWidth;
                        localLayoutParams.y = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)));
                        localLayoutParams.x = 0;
                    }
                    vw.setBackground(gt);
                    vw.getBackground().setDither(true);
                } else {
                    localLayoutParams.width = (int) ((Common.Height / 100f) * screenWidth);
                    localLayoutParams.height = (int) screenHeight;
                    localLayoutParams.x = (int) ((((((Common.Area - 75) * 2) / 100f)) * (screenWidth / 2)));
                    localLayoutParams.y = 0;
                    vw.setBackgroundColor(i);
                }
            }
            if (!Common.toggle) {
                vw.getBackground().setAlpha(Common.Alpha);
            } else {
                vw.getBackground().setAlpha(0);
            }
            localWindowManager.updateViewLayout(vw, localLayoutParams);
        }
    }

    private void NotificationBuilder() {
        Intent localIntent = new Intent(getApplicationContext(), MainActivity.class);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent localPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, localIntent, 0);
        Intent deleteIntent = new Intent("com.aaahh.yellow.Toggle");
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int buttonToggle = Common.toggle
                ? android.R.drawable.ic_media_play : android.R.drawable.ic_media_pause;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //new Notification.Action.Builder(buttonToggle, "Toggle", pendingIntentCancel); google deprecated in api 25, dont know what the replacement is
            localNotification = new Notification.Builder(this)
                    .setContentTitle("Screen Filter")
                    .setContentText("Activated")
                    .addAction(buttonToggle, "Toggle", pendingIntentCancel)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(localPendingIntent)
                    .setStyle(new Notification.MediaStyle()
                            .setShowActionsInCompactView(0))
                    //  .setColor(Color.rgb(223,223,223))
                    .setColor(Color.rgb(33, 150, 243))
                    .setPriority(Notification.PRIORITY_MIN);
        } else {
            localNotification = new Notification.Builder(this)
                    .setContentTitle("Screen Filter")
                    .setContentText("Activated")
                    .addAction(buttonToggle, "Toggle", pendingIntentCancel)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(localPendingIntent)
                    .setPriority(Notification.PRIORITY_MIN);
        }
    }

    public void Notification() {
        n = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        NotificationBuilder();
        n.notify(1, localNotification.build());
        startForeground(1, localNotification.build());
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

    private void rotationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.aaahh.yellow.Toggle");
        if (Common.Receiver) {
            registerReceiver(myReceiver, filter);
        }
        if (!Common.Receiver) {
            unregisterReceiver(myReceiver);
        }
    }

    public class LocalBinder extends Binder {
        FilterService getService() {
            return FilterService.this;
        }
    }
}