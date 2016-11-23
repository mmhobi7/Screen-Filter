package com.aaahh.yellow;

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
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private MainActivity mThis;
    private ToggleButton ToggleButton2;
    private TextView TextPercent;
    private ToggleButton ToggleButton1;
    private Button layerButton;
    public static int currentlayer = 0;
    public static List<Integer> Layersx;
    private FilterService rService;
    private final ServiceConnection rConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FilterService.LocalBinder localLocalBinder = (FilterService.LocalBinder) iBinder;
            MainActivity.this.rService = localLocalBinder.getService();
            if ((Common.FilterYN) && (MainActivity.this.rService.vw.size() < 1)) {
                Log.d(TAG, "onServiceConnected: mom");
                MainActivity.this.startService(new Intent(MainActivity.this, FilterService.class));
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
        if (Common.BootNow) {
            Common.BootNow = false;
            moveTaskToBack(true);
        }
        setContentView(R.layout.activity_main);
        mThis = this;
        requestSystemAlertPermission(mThis);
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        int Alpha = settings.getInt("Alpha", 50);
        int AreaY = settings.getInt("AreaY", 75);
        int AreaX = settings.getInt("AreaX", 75);
        boolean Boot = settings.getBoolean("Boot", Common.Boot);
        int Color = settings.getInt("Color", -8257792);
        boolean FilterYN = settings.getBoolean("FilterYN", false);
        int Gradient = settings.getInt("Gradient", -1);
        int Height = settings.getInt("Height", 50);
        int Width = settings.getInt("Height", 50);
        boolean Hide = settings.getBoolean("Hide", Common.Hide);
        boolean ToHide = settings.getBoolean("ToHide", Common.ToHide);
        Layersx = new ArrayList<Integer>();
        Layersx.add(1);
        FilterService.vw = new ArrayList<>();
        Common.Alpha.add(currentlayer, 200 - (Alpha * 2));
        Common.AreaY.add(currentlayer, AreaY);
        Common.AreaX.add(currentlayer, AreaX);
        Common.Boot = Boot;
        Common.Color.add(currentlayer, Color);
        Common.FilterYN = FilterYN;
        Common.Gradient.add(currentlayer, Gradient);
        Common.Height.add(currentlayer, Height);
        Common.Width.add(currentlayer, Width);
        Common.Hide = Hide;
        Common.ToHide = ToHide;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        SeekBar sliderTransparency = ((SeekBar) findViewById(R.id.seekBar));
        SeekBar sliderHeight = ((SeekBar) findViewById(R.id.seekBar4));
        SeekBar sliderWidth = ((SeekBar) findViewById(R.id.seekBar5));
        SeekBar sliderAreaY = ((SeekBar) findViewById(R.id.seekBar7));
        SeekBar sliderAreaX = ((SeekBar) findViewById(R.id.seekBar6));
        layerButton = ((Button) findViewById(R.id.button));
        layerButton.setText(getString(R.string.Layer) + " " + getString(R.string.One));
        sliderTransparency.setProgress(Alpha);
        sliderHeight.setProgress(Height);
        sliderWidth.setProgress(Width);
        sliderAreaY.setProgress(AreaY);
        sliderAreaX.setProgress(AreaX);
        if (FilterYN) {
            ToggleButton1.setChecked(true);
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        }
        if (Common.Gradient.get(currentlayer) > (-1)) {
            ToggleButton2.setChecked(true);
        } else {
            ToggleButton2.setChecked(false);
        }
        sliderTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                String textpercenttext = (paramAnonymousInt + "%");
                TextPercent.setText(textpercenttext);
                Common.Alpha.set(currentlayer, 200 - paramAnonymousInt * 2);
                rService.setAlpha(Common.Alpha.get(currentlayer));
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                String textpercenttext = (paramAnonymousSeekBar.getProgress()) + "%";
                TextPercent.setText(textpercenttext);
                Common.Alpha.set(currentlayer, 200 - (paramAnonymousSeekBar.getProgress()) * 2);
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Alpha", (paramAnonymousSeekBar.getProgress()));
                editor.apply();
                rService.setAlpha(Common.Alpha.get(currentlayer));
            }
        });
        sliderHeight.setMax(100);
        sliderHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.Height.set(currentlayer, paramAnonymousInt);
                if (Common.Height.get(currentlayer) < 1) {
                    Common.Height.set(currentlayer, 1);
                }
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Height", paramAnonymousSeekBar.getProgress());
                editor.apply();
                Common.Height.set(currentlayer, paramAnonymousSeekBar.getProgress());
                if (Common.Height.get(currentlayer) < 1) {
                    Common.Height.set(currentlayer, 1);
                }
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }
        });
        sliderWidth.setMax(100);
        sliderWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.Width.set(currentlayer, paramAnonymousInt);
                if (Common.Width.get(currentlayer) < 1) {
                    Common.Width.set(currentlayer, 1);
                }
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Width", paramAnonymousSeekBar.getProgress());
                editor.apply();
                Common.Width.set(currentlayer, paramAnonymousSeekBar.getProgress());
                if (Common.Width.get(currentlayer) < 1) {
                    Common.Width.set(currentlayer, 1);
                }
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }
        });
        sliderAreaY.setMax(150);
        sliderAreaY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.AreaY.set(currentlayer, paramAnonymousInt);
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("AreaY", paramAnonymousSeekBar.getProgress());
                editor.apply();
                Common.AreaY.set(currentlayer, paramAnonymousSeekBar.getProgress());
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }

            }
        });
        sliderAreaX.setMax(150);
        sliderAreaX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                Common.AreaX.set(currentlayer, paramAnonymousInt);
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("AreaY", paramAnonymousSeekBar.getProgress());
                editor.apply();
                Common.AreaX.set(currentlayer, paramAnonymousSeekBar.getProgress());
                if (FilterService.vw != null) {
                    FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                }

            }
        });
        String textpercenttext = sliderTransparency.getProgress() + "%";
        TextPercent.setText(textpercenttext);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
                @Override
                public void onDisplayAdded(int i) {
                    if (FilterService.vw != null) {
                        FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                    }
                }

                @Override
                public void onDisplayRemoved(int i) {
                    if (FilterService.vw != null) {
                        FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                    }
                }

                @Override
                public void onDisplayChanged(int i) {
                    if (FilterService.vw != null) {
                        FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
                    }
                }
            };
            DisplayManager displayManager = (DisplayManager) mThis.getSystemService(Context.DISPLAY_SERVICE);
            displayManager.registerDisplayListener(mDisplayListener, null);
        }
    }

    public void StartToggle(View view) {
        if (ToggleButton1.isChecked()) {
            ToggleButton2.setEnabled(false);
            Common.Receiver = true;
            Common.toggle = false; // not really needed here
            rService.Notification();
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
            Common.toggle = false;
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FilterYN", false);
            editor.apply();
            this.rService.removeView();
            stopService(new Intent(this, FilterService.class));
        }
    }

    private static void requestSystemAlertPermission(Activity context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            final boolean result = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
            if (!result) {
                final String packageName = context.getPackageName();
                final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName));
                context.startActivityForResult(intent, 1);
            }
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
                                    Common.Gradient.set(currentlayer, i);
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
            Common.Gradient.set(currentlayer, -1);
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

    public void update () {
        FilterService.setRotation(mThis, FilterService.vw.toArray(new View[FilterService.vw.size()]));
    }

    public void LayerPicker(View view) {
        final AlertDialog.Builder abuild = new AlertDialog.Builder(mThis);
        abuild.setTitle(getString(R.string.Layers));
        final List<CharSequence> layers = new ArrayList<CharSequence>();
        for (int i = 0; i < Layersx.size(); i++) {
            layers.add(getString(R.string.Layer) + " " + (i + 1));
        }
        abuild.setItems(layers.toArray(new CharSequence[layers.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = getString(R.string.Layer) + " " + (which + 1);
                currentlayer = which;
                layerButton.setText(text);
                update();
            }
        });
        abuild.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // overide
            }
        });
        abuild.setNegativeButton("Cancel", null);
        final AlertDialog alert = abuild.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ("onClick:" + Layersx.size()));
                Layersx.add(Layersx.size());
                String text = getString(R.string.Layer) + " " + (Layersx.size());
                currentlayer = Layersx.size()-1;
                layerButton.setText(text);
                addlayer();
                mThis.rService.addView();
                alert.dismiss();
                LayerPicker(null);
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (Common.Receiver) {
            unbindService(rConnection);
        }
    }

    public void addlayer(){
        Common.Alpha.add(50);
        Common.Height.add(50);
        Common.Width.add(50);
        Common.AreaY.add(50);
        Common.AreaX.add(50);
        Common.Gradient.add(-1);
        Common.Color.add(-8257792);
        Common.FilterYN = true;
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
        /*if (id == R.id.action_settings_root) {
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
        }*/

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
        }
        return super.onOptionsItemSelected(item);
    }
}