package com.aaahh.yello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;


public class MainActivity extends Activity {

    public static MainActivity mThis;
    public static ToggleButton ToggleButton2;
    public TextView TextPercent;
    public ToggleButton ToggleButton1;
    public Button SelectButton;
    public SeekBar Slider;
    public SeekBar Sliderb;
    public SeekBar Sliderc;
    public ImageView ColorView;
    FilterService rService;
    public boolean first;

    private final ServiceConnection rConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FilterService.LocalBinder localLocalBinder = (FilterService.LocalBinder) iBinder;
            MainActivity.this.rService = localLocalBinder.getService();
            if ((Common.FilterYN.equals("Y")) && (FilterService.vw == null)) {
                MainActivity.this.startService(new Intent(MainActivity.mThis, FilterService.class));
                MainActivity.this.rService.addView();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mThis = this;
        Common.passedonce = "Y";
        SharedPreferences prefs = this.getSharedPreferences(
                getString(R.string.preference_first_key, R.string.preference_Height_key, R.string.preference_Height_key, R.string.preference_Boot_key, R.string.preference_Alpha_key), Context.MODE_WORLD_WRITEABLE);
        //     R.string.preference_Area_key),
        //    getString(R.string.preference_Color_key),
        //  getString(R.string.preference_FilterYN_key),
        //getString(R.string.preference_first_key), Context.MODE_WORLD_WRITEABLE);

        if (prefs.getString(getString(R.string.preference_first_key), "a").contains("1")) {
            first = true;
        } else {
            first = false;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.preference_first_key), "1");
            editor.apply();
        }
        if (prefs.getString(getString(R.string.preference_Boot_key), "a").contains("1")) {
            //moveTaskToBack(true);
        } else {
        }
        Log.d("o", prefs.getString(getString(R.string.preference_Boot_key), "a"));
        Log.d("p", prefs.getString(getString(R.string.preference_Height_key), "a"));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        SelectButton = ((Button) findViewById(R.id.button));
        Slider = ((SeekBar) findViewById(R.id.seekBar));
        Sliderb = ((SeekBar) findViewById(R.id.seekBar4));
        Sliderc = ((SeekBar) findViewById(R.id.seekBar5));
        ColorView = ((ImageView) findViewById(R.id.textureView));
        /*if (first) {
            Slider.setProgress(50);
            Sliderb.setProgress(50);
            Sliderc.setProgress(50);
        } else {
            Log.d("e", "2");
            Cursor c7 = db.getTitle(7);
           .// Cursor c6 = db.getTitle(6);
            Cursor c8 = db.getTitle(8);
            Slider.setProgress(Integer.parseInt(c7.getString(3)));
            Sliderb.setProgress(Integer.parseInt(c8.getString(3)));
            Sliderc.setProgress(Integer.parseInt(c6.getString(3)));
            Log.d("1", String.valueOf(Integer.parseInt(c6.getString(3))));
            Log.d("2", String.valueOf(Integer.parseInt(c8.getString(3))));
            Log.d("3", String.valueOf(Integer.parseInt(c7.getString(3))));
        }

        Cursor c2 = db.getTitle(2);
        Common.FilterYN = c2.getString(3);
        Cursor c3 = db.getTitle(3);
        Common.GradientYN = c3.getString(3);
        Cursor c4 = db.getTitle(4);
        Common.GradientType = c4.getString(3);
        Cursor c5 = db.getTitle(5);
        // Common.Color = Integer.parseInt(c5.getString(3));
        // Cursor c6 = db.getTitle(6);
        //
        //  String a = (c6.getString(3));
        //
//        TextPercent.setText(a + "%");
        // Common.Alpha = (200- a *2);
        Cursor c8 = db.getTitle(8);
        int b = Integer.parseInt(c8.getString(3));
        */
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        float screenHeight = displaymetrics.heightPixels;
        //Common.Height = (int) ((b / 100f) * screenHeight);
        // Cursor c9 = db.getTitle(9);
//        String q = c9.getString(3);
        //   Common.Area = (int) ((((q - 50) * 2) / 100f) * (screenHeight / 2) * -1);
        ToggleButton2.setChecked(false);
        if (Common.FilterYN.equals("Y")) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        }
        if (Common.GradientYN.equals("Y")) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        //   int j = Common.converToDecimalFromHex(Common.BgColor);
        //   ColorView.setBackgroundColor(j);
        ToggleButton2.setChecked(false);
        Slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                TextPercent.setText(paramAnonymousInt + "%");
                Common.Alpha = 200 - paramAnonymousInt * 2;
                rService.setAlpha(Common.Alpha);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int a = paramAnonymousSeekBar.getProgress();
                    TextPercent.setText(a + "%");
                    Common.Alpha = 200 - a * 2;
                    //  "7",
                    //    "Alpha",
                    //      Integer.toString(a));
                    rService.setAlpha(Common.Alpha);
                } catch (IllegalStateException ignored) {
                }
            }
        });
        Sliderb.setMax(100);
        Sliderb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.Height = paramAnonymousInt;
                rService.setHeight(Common.Height);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int b = paramAnonymousSeekBar.getProgress();

                    //  "8",
                    //    "Height",
                    // Integer.toString(b));
                    Common.Height = b;
                    rService.setHeight(Common.Height);
                } catch (IllegalStateException ignored) {
                }
            }
        });
        Sliderc.setMax(100);
        Sliderc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.Area = paramAnonymousInt;
                rService.setArea(Common.Area);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                int c = paramAnonymousSeekBar.getProgress();
                //   DatabaseActivity db = new DatabaseActivity(mThis);
                // db.open();
                // / db.updateTitle(2,
                //      "6",
                //        "Area",
                //    Integer.toString(c));
                Common.Area = c;
                rService.setArea(Common.Area);
            }
        });
        TextPercent.setText(Slider.getProgress() + "%");
    }

    public void StartToggle(View view) {
        if (ToggleButton1.isChecked()) {
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
            rService.startNotification();
//                    "2",
            //                  "FilterYN",
            //                "Y");
            startService(new Intent(this, FilterService.class));
            this.rService.addView();
        } else {
            ToggleButton2.setEnabled(true);
            Common.Receiver = false;
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
            this.rService.endNotification();
            Common.Notif = false;
            //          "2",
            //         "FilterYN",
            //       "N");
            this.rService.removeView();
            stopService(new Intent(this, FilterService.class));
        }
    }

    public void GradientToggle(View view) {
        if (ToggleButton2.isChecked()) {
            Log.d("e", Common.GradientType);
            if (ToggleButton2.isChecked()) {
//                        "3",
                //                       "GradientYN",
                //                     "Y");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] Lists = {"Top only", "All", "Bottom only"};
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Where")
                                .setItems(Lists, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            Common.GradientType = String.valueOf(1);
//                                                    "4",
                                            //                                                  "GradientType",
                                            //                                                "1");
                                        } else {
                                            if (i == 1) {
                                                Common.GradientType = String.valueOf(2);
                                                //               db.updateTitle(3,
                                                //                     "4",
                                                //                   "GradientType",
                                                //                 "2");
                                            } else {
                                                if (i == 2) {
                                                    Common.GradientType = String.valueOf(3);
                                                    //           db.updateTitle(3,
                                                    //                 "4",
                                                    //               "GradientType",
                                                    //             "3");
                                                }
                                            }
                                        }
                                    }
                                })
                                .show();
                    }
                });
            } else {
                //     db.updateTitle(3,
                //           "3",
                //         "GradientYN",
                //       "Y");
                //db.close();
            }
        }
    }

    public void ColorPicker(View view) {
        Intent intent = new Intent(this, Color.class);
        this.startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        Common.Receiver = false;
        unbindService(rConnection);
    }
    public void onPause() {
        super.onPause();
    }
    public void onResume() {
        super.onResume();
    }
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, FilterService.class), this.rConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        // Stop closes on back button.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try {
                Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //settings
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
