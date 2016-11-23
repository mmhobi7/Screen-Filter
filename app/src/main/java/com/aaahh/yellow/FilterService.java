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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.aaahh.yellow.MainActivity.Layersx;
import static com.aaahh.yellow.MainActivity.currentlayer;

/**
 * Created by Aaahh on 8/26/14. Edited 9/13/14
 */
public class FilterService extends Service {
    public static ArrayList<View> vw;
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
                    if (vw != null) {
                        for (View aVw : vw.toArray(new View[vw.size()])) {
                            aVw.getBackground().setAlpha(Common.Alpha.get(currentlayer));
                            localWindowManager.updateViewLayout(aVw, FilterService.localLayoutParams);
                        }
                        Common.toggle = false;
                        NotificationBuilder();
                        n.notify(1, localNotification.build());
                    }
                } else {
                    if (vw != null) {
                        for (View aVw : vw.toArray(new View[vw.size()])) {
                            aVw.getBackground().setAlpha(0);
                            localWindowManager.updateViewLayout(aVw, FilterService.localLayoutParams);
                        }
                        Common.toggle = true;
                        NotificationBuilder();
                        n.notify(1, localNotification.build());
                    }
                }
            }
        }
    };

    public void addView() {
        Log.d(TAG, "addView: ?");
        if (vw != null) {
            removeView();
            Log.d(TAG, "addView: w?");
        }
        Log.d(TAG, "addView: ?w");
        vw = new ArrayList<>();
        for (int i = 0; i < Layersx.size(); i++) {
            vw.add(i, new View(this));
            localLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN),
                    PixelFormat.RGBA_8888);
            localWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            localWindowManager.addView(vw.get(i), localLayoutParams);
            Log.d(TAG, "addView: w?");
        }
        setRotation(this, vw.toArray(new View[vw.size()]));
        if (!Common.Notif) {
            Notification();
        }
        rotationReceiver();
        Log.d(TAG, "addView: ss?");
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
            for (View avw : vw.toArray(new View[vw.size()])) {
                if (avw != null) {
                    ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(avw);
                }
                avw = null;
            }
        }
        vw = null;
    }

    public void setAlpha(int paramInt) {
        if (vw != null) {
            for (View avw : vw.toArray(new View[vw.size()])) {
                if (avw == null) {
                    return;
                }
                avw.getBackground().setAlpha(paramInt);
            }
        }
    }

    static void setRotation(Context mContext, View[] vww) {
        Log.d(TAG, "setRotation: a");
        if (vww != null) {
            Log.d(TAG, "setRotation: aq");
            int clayer = 0;
            for (View vw : vww) {
                Log.d(TAG, "setRotation: a1");
                if (clayer < currentlayer + 1) {
                    if (!(vw == null)) {
                        Log.d(TAG, String.valueOf(currentlayer));
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                        float screenWidth = displaymetrics.widthPixels;
                        float screenHeight = displaymetrics.heightPixels;
                        int i = Common.Color.get(clayer);
                        String hexColor = String.format("#%06X", (0xFFFFFF & i));
                        String fade = hexColor.replace("#", "#00");
                        GradientDrawable gt;
                        Common.O = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                        if (Common.O == 0) {
                            if (Common.Gradient.get(clayer) > (-1)) {
                                int b = (Color.parseColor(fade));
                                gt = new GradientDrawable();
                                if (Common.Gradient.get(clayer) == 0) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 1) {
                                    int colors[] = {b, i, b};
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 2) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 3) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 4) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                vw.setBackground(gt);
                                vw.getBackground().setDither(true);
                            } else {
                                localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                vw.setBackgroundColor(i);
                            }
                        }
                        if (Common.O == 1) {
                            if (Common.Gradient.get(clayer) > -1) {
                                int b = (Color.parseColor(fade));
                                gt = new GradientDrawable();
                                if (Common.Gradient.get(clayer) == 0) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 1) {
                                    int colors[] = {b, i, b};
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 2) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 3) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2));
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 4) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                vw.setBackground(gt);
                                vw.getBackground().setDither(true);
                            } else {
                                localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                vw.setBackgroundColor(i);
                            }
                        }
                        if (Common.O == 2) {
                            if (Common.Gradient.get(clayer) > -1) {
                                int b = (Color.parseColor(fade));
                                gt = new GradientDrawable();
                                if (Common.Gradient.get(clayer) == 0) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = ((int) ((Common.Height.get(clayer) / 100f) * screenHeight));
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                }
                                if (Common.Gradient.get(clayer) == 1) {
                                    int colors[] = {b, i, b};
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = ((int) ((Common.Height.get(clayer) / 100f) * screenHeight));
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                }
                                if (Common.Gradient.get(clayer) == 2) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = ((int) ((Common.Height.get(clayer) / 100f) * screenHeight));
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                }
                                if (Common.Gradient.get(clayer) == 3) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = ((int) ((Common.Height.get(clayer) / 100f) * screenWidth));
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.x = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                }
                                if (Common.Gradient.get(clayer) == 4) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = ((int) ((Common.Height.get(clayer) / 100f) * screenWidth));
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2)) * -1);
                                }
                                vw.setBackground(gt);
                                vw.getBackground().setDither(true);
                            } else {
                                localLayoutParams.height = ((int) ((Common.Height.get(clayer) / 100f) * screenHeight));
                                localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                localLayoutParams.y = (int) (((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenHeight / 2));
                                vw.setBackgroundColor(i);
                            }
                        }
                        if (Common.O == 3) {
                            if (Common.Gradient.get(clayer) > -1) {
                                int b = (Color.parseColor(fade));
                                gt = new GradientDrawable();
                                if (Common.Gradient.get(clayer) == 0) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)));
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 1) {
                                    int colors[] = {b, i, b};
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)));
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 2) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)));
                                    localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 3) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) (((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2))) * -1);
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                if (Common.Gradient.get(clayer) == 4) {
                                    int colors[] = {b, i};
                                    gt.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                                    gt.setColors(colors);
                                    gt.setDither(true);
                                    localLayoutParams.height = (int) ((Common.Height.get(clayer) / 100f) * screenHeight);
                                    localLayoutParams.width = (int) ((Common.Width.get(clayer) / 100f) * screenWidth);
                                    localLayoutParams.y = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)));
                                    localLayoutParams.x = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                }
                                vw.setBackground(gt);
                                vw.getBackground().setDither(true);
                            } else {
                                localLayoutParams.width = (int) ((Common.Height.get(clayer) / 100f) * screenWidth);
                                localLayoutParams.height = (int) ((Common.Width.get(clayer) / 100f) * screenHeight);
                                localLayoutParams.x = (int) ((((((Common.AreaY.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)));
                                localLayoutParams.y = (int) ((((((Common.AreaX.get(clayer) - 75) * 2) / 100f)) * (screenWidth / 2)) * -1);
                                vw.setBackgroundColor(i);
                            }
                        }
                        if (!Common.toggle) {
                            vw.getBackground().setAlpha(Common.Alpha.get(clayer));
                        } else {
                            vw.getBackground().setAlpha(0);
                        }
                        localWindowManager.updateViewLayout(vw, localLayoutParams);
                    }
                    clayer++;
                }
            }
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