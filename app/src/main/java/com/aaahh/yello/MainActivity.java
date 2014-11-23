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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
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
    FilterService rService;
    private final ServiceConnection rConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FilterService.LocalBinder localLocalBinder = (FilterService.LocalBinder) iBinder;
            MainActivity.this.rService = localLocalBinder.getService();
            if ((Common.FilterYN) && (FilterService.vw == null)) {
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
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        boolean Boot = settings.getBoolean("Boot", Common.Boot);
        if (Common.BootNow) {
            if (Boot) {
                Common.BootNow = false;
                moveTaskToBack(true);
            } else {
                finish();
            }
        }
        boolean Hide = settings.getBoolean("Hide", Common.Hide);
        boolean ToHide = settings.getBoolean("ToHide", Common.ToHide);
        int Area = settings.getInt("Area", 50);
        int Alpha = settings.getInt("Alpha", 50);
        int Height = settings.getInt("Height", 50);
        int Color = settings.getInt("Color", -8257792);
        int Gradient = settings.getInt("Gradient", 0);
        boolean FilterYN = settings.getBoolean("FilterYN", false);
        Common.Area = Area;
        Common.Alpha = 200 - Alpha * 2;
        Common.Height = Height;
        Common.Color = Color;
        Common.FilterYN = FilterYN;
        Common.Gradient = Gradient;
        Common.Boot = Boot;
        Common.Hide = Hide;
        Common.ToHide = ToHide;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        SelectButton = ((Button) findViewById(R.id.button));
        Slider = ((SeekBar) findViewById(R.id.seekBar));
        Sliderb = ((SeekBar) findViewById(R.id.seekBar4));
        Sliderc = ((SeekBar) findViewById(R.id.seekBar5));
        Slider.setProgress(Alpha);
        Sliderb.setProgress(Height);
        Sliderc.setProgress(Area);
        //boot.setChecked(Boot);
        if (FilterYN) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        }
        if (Common.Gradient > 0) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        Slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                TextPercent.setText(paramAnonymousInt + "%");
                Common.Alpha = 200 - paramAnonymousInt * 2;
                rService.setAlpha(Common.Alpha);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                    TextPercent.setText((paramAnonymousSeekBar.getProgress()) + "%");
                    Common.Alpha = 200 - (paramAnonymousSeekBar.getProgress()) * 2;
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Alpha", (paramAnonymousSeekBar.getProgress()));
                    editor.apply();
                    rService.setAlpha(Common.Alpha);
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
                    SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Height", paramAnonymousSeekBar.getProgress());
                    editor.apply();
                    Common.Height = paramAnonymousSeekBar.getProgress();
                    rService.setHeight(Common.Height);
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
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Area", paramAnonymousSeekBar.getProgress());
                editor.apply();
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
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FilterYN", true);
            editor.apply();
            startService(new Intent(this, FilterService.class));
            this.rService.addView();
        } else {
            ToggleButton2.setEnabled(true);
            Common.Receiver = false;
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
            this.rService.endNotification();
            Common.Notif = false;
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FilterYN", false);
            editor.apply();
            this.rService.removeView();
            stopService(new Intent(this, FilterService.class));
        }
    }

    public void GradientToggle(View view) {
        if (ToggleButton2.isChecked()) {
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
                                        Common.Gradient = 1;
                                        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putInt("Gradient", 1);
                                        editor.apply();
                                    } else {
                                        if (i == 1) {
                                            Common.Gradient = 2;
                                            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putInt("Gradient", 2);
                                            editor.apply();
                                        } else {
                                            if (i == 2) {
                                                Common.Gradient = 3;
                                                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                                                SharedPreferences.Editor editor = settings.edit();
                                                editor.putInt("Gradient", 3);
                                                editor.apply();
                                            }
                                        }
                                    }
                                }
                            })
                            .show();
                }
            });
        } else {
            Common.Gradient = 0;
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Gradient", 0);
            editor.apply();
        }
    }

    public void ColorPicker(View view) {
        Intent intent = new Intent(this, Color.class);
        this.startActivity(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        Common.Receiver = false;
        if (Common.Boot) {
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