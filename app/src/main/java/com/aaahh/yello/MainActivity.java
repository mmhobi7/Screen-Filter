package com.aaahh.yello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
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
    public boolean first;
    FilterService rService;
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
        //CAN NOT INTERWEAVE WITH OTHER \/
        // SharedPreferences fp = this.getSharedPreferences(getString(R.string.preference_first_key), 0);
        // SharedPreferences bp = this.getSharedPreferences(getString(R.string.preference_Boot_key), 0);
        //     R.string.preference_Area_key),
        //    getString(R.string.preference_Color_key),
        //  getString(R.string.preference_FilterYN_key),
        //getString(R.string.preference_first_key), Context.MODE_WORLD_WRITEABLE);
        //  SharedPreferences.Editor editor = bp.edit();
        //  editor.putString(getString(R.string.preference_Boot_key), "0");
        //   editor.commit();
        //   if (fp.getString(getString(R.string.preference_first_key), "a").contains("1")) {
        //     first = true;
        // } else {
        //     first = false;
        //SharedPreferences.Editor editor2 = fp.edit();
        //  editor2.putString(getString(R.string.preference_first_key), "1");
        //editor2.commit();
        //  }

        SharedPreferences bp = this.getSharedPreferences(getString(R.string.preference_Boot_key), 0);
        if (bp.getString(getString(R.string.preference_Boot_key), "a").contains("1")) {
            moveTaskToBack(true);
        }
        Log.d("o", bp.getString(getString(R.string.preference_Boot_key), "a"));
        //   Log.d("p", fp.getString(getString(R.string.preference_Height_key), "a"));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        SelectButton = ((Button) findViewById(R.id.button));
        Slider = ((SeekBar) findViewById(R.id.seekBar));
        Sliderb = ((SeekBar) findViewById(R.id.seekBar4));
        Sliderc = ((SeekBar) findViewById(R.id.seekBar5));
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
            set Common.Area Hieght and Alpha
            //        TextPercent.setText(a + "%");
        }
        */
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

                    //  "8",
                    //    "Height",
                    // Integer.toString(b));
                    Common.Height = paramAnonymousSeekBar.getProgress();
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
                //   DatabaseActivity db = new DatabaseActivity(mThis);
                // db.open();
                // / db.updateTitle(2,
                //      "6",
                //        "Area",
                //    Integer.toString(c));
                Common.Area = paramAnonymousSeekBar.getProgress();
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
