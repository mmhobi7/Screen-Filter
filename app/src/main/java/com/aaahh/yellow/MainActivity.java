package com.aaahh.yellow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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

    private static MainActivity mThis;
    private static ToggleButton ToggleButton2;
    private TextView TextPercent;
    private ToggleButton ToggleButton1;
    private FilterService rService;
    private final ServiceConnection rConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FilterService.LocalBinder localLocalBinder = (FilterService.LocalBinder) iBinder;
            MainActivity.this.rService = localLocalBinder.getService();
            if ((Common.FilterYN) && (FilterService.vw == null)) {
                MainActivity.this.startService(new Intent(MainActivity.mThis, FilterService.class));
                MainActivity.this.rService.addView(Common.Layer);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private Button layerOne;
    private Button layerTwo;
    private Button layerThree;
    private SeekBar slider;
    private SeekBar sliderb;
    private SeekBar sliderc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.BootNow) {
            Common.BootNow = false;
            moveTaskToBack(true);
        }
        setContentView(R.layout.activity_main);
        mThis = this;
        //TODO: loop statment
        //
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        Common.Alpha = (200 - (settings.getInt("Alpha", 50)) * 2);
        Common.Area = settings.getInt("Area", 75);
        Common.Boot = settings.getBoolean("Boot", false);
        Common.Color = settings.getInt("Color", -8257792);
        Common.FilterYN = settings.getBoolean("FilterYN", false);
        Common.Gradient = settings.getInt("Gradient", -1);
        Common.Height = settings.getInt("Height", 50);
        Common.Hide = settings.getBoolean("Hide", false);
        Common.ToHide = settings.getBoolean("ToHide", false);
        SharedPreferences settings2 = getSharedPreferences(Common.PREFS_NAME2, 0);
        Common.Alpha2 = (200 - (settings2.getInt("Alpha", 50)) * 2);
        Common.Area2 = settings2.getInt("Area", 75);
        Common.Color2 = settings2.getInt("Color", -8257792);
        Common.FilterYN2 = settings2.getBoolean("FilterYN", false);
        Common.Gradient2 = settings2.getInt("Gradient", -1);
        Common.Height2 = settings2.getInt("Height", 50);
        SharedPreferences settings3 = getSharedPreferences(Common.PREFS_NAME3, 0);
        Common.Alpha3 = (200 - (settings3.getInt("Alpha", 50)) * 2);
        Common.Area3 = settings3.getInt("Area", 75);
        Common.Color3 = settings3.getInt("Color", -8257792);
        Common.FilterYN3 = settings3.getBoolean("FilterYN", false);
        Common.Gradient3 = settings3.getInt("Gradient", -1);
        Common.Height3 = settings3.getInt("Height", 50);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        layerOne = (Button) findViewById(R.id.button2);
        //layerOne.setFocusable(true);
        ////layerOne.setFocusableInTouchMode(true);
        layerOne.requestFocus();
        layerTwo = (Button) findViewById(R.id.button5);
        //layerTwo.setFocusable(true);
        //layerTwo.setFocusableInTouchMode(true);
        layerThree = (Button) findViewById(R.id.button6);
        //layerThree.setFocusable(true);
        //layerThree.setFocusableInTouchMode(true);
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        slider = ((SeekBar) findViewById(R.id.seekBar));
        sliderb = ((SeekBar) findViewById(R.id.seekBar4));
        sliderc = ((SeekBar) findViewById(R.id.seekBar5));
        slider.setProgress(Common.Alpha);
        sliderb.setProgress(Common.Height);
        sliderc.setProgress(Common.Area);
        if (Common.FilterYN) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        }
        if (Common.Gradient > (-1)) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                TextPercent.setText(paramAnonymousInt + "%");
                Common.Alpha = 200 - paramAnonymousInt * 2;
                if (Common.Layer == 1) {
                    rService.setAlpha(Common.Alpha);
                }
                if (Common.Layer == 2) {
                    rService.setAlpha(Common.Alpha2);
                }
                if (Common.Layer == 3) {
                    rService.setAlpha(Common.Alpha3);
                }
                
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                TextPercent.setText((paramAnonymousSeekBar.getProgress()) + "%");
                if (Common.Layer == 1) {
                    Common.Alpha = 200 - (paramAnonymousSeekBar.getProgress()) * 2;
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Alpha", (paramAnonymousSeekBar.getProgress()));
                    editor.apply();
                    rService.setAlpha(Common.Alpha);
                }
                if (Common.Layer == 2) {
                    Common.Alpha2 = 200 - (paramAnonymousSeekBar.getProgress()) * 2;
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Alpha", (paramAnonymousSeekBar.getProgress()));
                    editor.apply();
                    rService.setAlpha(Common.Alpha2);
                }
                if (Common.Layer == 3) {
                    Common.Alpha3 = 200 - (paramAnonymousSeekBar.getProgress()) * 2;
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Alpha", (paramAnonymousSeekBar.getProgress()));
                    editor.apply();
                    rService.setAlpha(Common.Alpha3);
                }
            }
        });
        sliderb.setMax(100);
        sliderb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                if (Common.Layer == 1) {
                    Common.Height = paramAnonymousInt;
                }
                if (Common.Layer == 2) {
                    Common.Height2 = paramAnonymousInt;
                }
                if (Common.Layer == 3) {
                    Common.Height3 = paramAnonymousInt;
                }
                rService.setRotation();
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                if (Common.Layer == 1) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Height", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Height = paramAnonymousSeekBar.getProgress();
                    if (Common.Height < 1) {
                        Common.Height = 1;
                    }
                }
                if (Common.Layer == 2) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Height", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Height2 = paramAnonymousSeekBar.getProgress();
                    if (Common.Height2 < 1) {
                        Common.Height2 = 1;
                    }
                    Log.d("2", String.valueOf(Common.Height2));
                }
                if (Common.Layer == 3) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Height", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Height3 = paramAnonymousSeekBar.getProgress();
                    if (Common.Height3 < 1) {
                        Common.Height3 = 1;
                    }
                }
                rService.setRotation();
            }
        });
        sliderc.setMax(150);
        sliderc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                if (Common.Layer == 1) {
                    Common.Area = paramAnonymousInt;
                }
                if (Common.Layer == 2) {
                    Common.Area2 = paramAnonymousInt;
                }
                if (Common.Layer == 3) {
                    Common.Area3 = paramAnonymousInt;
                }
                rService.setRotation();
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                if (Common.Layer == 1) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Area", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Area = paramAnonymousSeekBar.getProgress();
                }
                if (Common.Layer == 2) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Area", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Area2 = paramAnonymousSeekBar.getProgress();
                }
                if (Common.Layer == 3) {
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Area", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Area3 = paramAnonymousSeekBar.getProgress();
                }
                rService.setRotation();

            }
        });
        TextPercent.setText(slider.getProgress() + "%");
    }

    public void StartToggle(View view) {
        Log.d("1", "2");
        if (ToggleButton1.isChecked()) {
            Common.Receiver = true;
            if (!Common.FilterYN || !Common.FilterYN2 || !Common.FilterYN3) {
                startService(new Intent(this, FilterService.class));
                rService.Notification();
            }
            ToggleButton2.setEnabled(false);
            if (Common.Layer == 1) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN", true);
                editor.apply();
                Common.FilterYN = true;
                this.rService.addView(1);
            }
            if (Common.Layer == 2) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN2", true);
                editor.apply();
                Common.FilterYN2 = true;
                this.rService.addView(2);

            }
            if (Common.Layer == 3) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN3", true);
                editor.apply();
                Common.FilterYN3 = true;
                this.rService.addView(3);
            }
        } else {
            ToggleButton2.setEnabled(true);
            if (Common.Layer == 1) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN", false);
                editor.apply();
                Common.FilterYN = false;
            }
            if (Common.Layer == 2) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN2", false);
                editor.apply();
                Common.FilterYN2 = false;
            }
            if (Common.Layer == 3) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FilterYN3", false);
                editor.apply();
                Common.FilterYN3 = false;
            }
            this.rService.removeView();
            if (!Common.FilterYN | !Common.FilterYN2 | !Common.FilterYN3) {
                Common.Receiver = false;
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
                this.rService.endNotification();
                Common.Notif = false;
                stopService(new Intent(this, FilterService.class));
            }
            rService.setRotation();
        }
    }

    public void GradientToggle(View view) {
        if (ToggleButton2.isChecked()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] Lists = {"Top only", "All", "Bottom only", "Left to Right", "Right to Left"};
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Where")
                            .setItems(Lists, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Common.Gradient = i;
                                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putInt("Gradient", i);
                                    editor.apply();
                                }
                            })
                            .show();
                }
            });
        } else {
            Common.Gradient = (-1);
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Gradient", (-1));
            editor.apply();
        }
    }

    public void ColorPicker(View view) {
        Intent intent = new Intent(this, Color.class);
        this.startActivity(intent);
    }

    public void LayerOne(View view) {
        Common.Layer = 1;
        Log.d("1", String.valueOf(Common.Height));
        slider.setProgress(Common.Alpha);
        sliderb.setProgress(Common.Height);
        sliderc.setProgress(Common.Area);
        /*
        if (Common.FilterYN) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        } else {
            ToggleButton1.setChecked(false);
            ToggleButton2.setEnabled(true);
        }
        if (Common.Gradient > (-1)) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        */
    }

    public void LayerTwo(View view) {
        Common.Layer = 2;
        Log.d("2", String.valueOf(Common.Height2));
        slider.setProgress(Common.Alpha2);
        sliderb.setProgress(Common.Height2);
        sliderc.setProgress(Common.Area2);
        /*
        if (Common.FilterYN2) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        } else {
            ToggleButton1.setChecked(false);
            ToggleButton2.setEnabled(true);
        }
        if (Common.Gradient2 > (-1)) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        */
    }

    public void LayerThree(View view) {
        Common.Layer = 3;
        Log.d("3", String.valueOf(Common.Height3));
        slider.setProgress(Common.Alpha3);
        sliderb.setProgress(Common.Height3);
        sliderc.setProgress(Common.Area3);
        if (Common.FilterYN3) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        } else {
            ToggleButton1.setChecked(false);
            ToggleButton2.setEnabled(true);
        }
        if (Common.Gradient3 > (-1)) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (Common.Receiver) {
            unbindService(rConnection);
        }
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
        menu.findItem(R.id.action_settings_boot).setChecked(Common.Boot);
        menu.findItem(R.id.action_settings_hide).setChecked(Common.ToHide);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_root) {
            try {
                Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Command su not found")
                                .setMessage("This means Root was not detected.")
                                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                    }
                });
            }
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_boot) {
            if (item.isChecked()) {
                item.setChecked(false);
                Common.Boot = false;
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Boot", false);
                editor.apply();
                if (Common.Hide) {
                    Common.Hide = false;
                    editor.putBoolean("Hide", false);
                    editor.apply();
                    PackageManager packageManager = this.getPackageManager();
                    ComponentName componentName = new ComponentName(this,
                            Launcher.class);
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
            } else {
                item.setChecked(true);
                Common.Boot = true;
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Boot", true);
                editor.apply();
                if (Common.ToHide) {
                    Common.Hide = true;
                    editor.putBoolean("Hide", true);
                    editor.apply();
                    PackageManager packageManager = this.getPackageManager();
                    ComponentName componentName = new ComponentName(this,
                            Launcher.class);
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }
            }
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_hide) {
            if (item.isChecked()) {
                Common.ToHide = false;
                item.setChecked(false);
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ToHide", false);
                editor.apply();
                if (Common.Hide) {
                    Common.Hide = false;
                    editor.putBoolean("Hide", false);
                    editor.apply();
                    PackageManager packageManager = this.getPackageManager();
                    ComponentName componentName = new ComponentName(this,
                            Launcher.class);
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Warning")
                                .setMessage("The app will not hide if the filter is deactivated or the app is not set to start on boot.")
                                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                    }
                });
                Common.ToHide = true;
                item.setChecked(true);
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ToHide", true);
                editor.apply();
                if (Common.Boot) {
                    editor.putBoolean("Hide", true);
                    editor.apply();
                    Common.Hide = true;
                    PackageManager packageManager = this.getPackageManager();
                    ComponentName componentName = new ComponentName(this,
                            Launcher.class);
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }
                if (Common.Notif) {
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
            /*
            PackageManager packageManager = this.getPackageManager();
            ComponentName componentName = new ComponentName(this,
                    Launcher.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
                    */
        }
        return super.onOptionsItemSelected(item);
    }
}