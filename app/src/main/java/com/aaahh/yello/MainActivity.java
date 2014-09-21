package com.aaahh.yello;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
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

import java.util.zip.Inflater;


public class MainActivity extends Activity {

    public static MainActivity mThis;
    public ToggleButton ToggleButton2;
    public TextView TextPercent;
    public ToggleButton ToggleButton1;
    public Button SelectButton;
    public SeekBar Slider;
    public SeekBar Sliderb;
    public SeekBar Sliderc;
    public ImageView ColorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseActivity db = new DatabaseActivity(this);
        db.open();
        Cursor c = db.getTitle(1);
        if (c.moveToFirst()) {
            Toast.makeText(this,
                    "id: " + c.getString(0) + "\n" +
                            "ISBN: " + c.getString(1) + "\n" +
                            "TITLE: " + c.getString(2) + "\n" +
                            "PUBLISHER:  " + c.getString(3),
                    Toast.LENGTH_LONG).show();
            Common.passedonce = "Y";
            // Sliderc.setProgress(q);
        } else {
            //Root...
            long id;
            id = db.insertTitle(
                    "2",
                    "FilterYN",
                    "N");
            id = db.insertTitle(
                    "3",
                    "GradientYN",
                    "N");
            id = db.insertTitle(
                    "4",
                    "GradientType",
                    "1");
            id = db.insertTitle(
                    "5",
                    "BgColor",
                    "#000000");
            id = db.insertTitle(
                    "6",
                    "a",
                    "50");
            id = db.insertTitle(
                    "7",
                    "Height",
                    "50");
            id = db.insertTitle(
                    "8",
                    "Area",
                    "50");
            id = db.insertTitle(
                    "9",
                    "Alpha",
                    "50");
            id = db.insertTitle(
                    "1",
                    "First",
                    "1");
            Sliderc.setProgress(50);
        }
        // db.close();
        if (Common.boot.contains("1")) {
            moveTaskToBack(true);
        }
        MainActivity mThis = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        Inflater inflater;
        TextPercent = ((TextView) findViewById(R.id.textViewPer));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton2));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton));
        SelectButton = ((Button) findViewById(R.id.button));
        Slider = ((SeekBar) findViewById(R.id.seekBar));
        Sliderb = ((SeekBar) findViewById(R.id.seekBar4));
        Sliderc = ((SeekBar) findViewById(R.id.seekBar5));
        ColorView = ((ImageView) findViewById(R.id.textureView));
        Cursor c2 = db.getTitle(2);
        Common.FilterYN = c2.getString(3);
        Cursor c3 = db.getTitle(3);
        Common.GradientYN = c3.getString(3);
        Cursor c4 = db.getTitle(4);
        Common.GradientType = c4.getString(3);
        Cursor c5 = db.getTitle(5);
        Common.BgColor = c5.getString(3);
        Cursor c6 = db.getTitle(6);
        //
        String a = (c6.getString(3));
        //
//        TextPercent.setText(a + "%");
        // Common.Alpha = (200- a *2);
        Cursor c8 = db.getTitle(8);
        int b = Integer.parseInt(c8.getString(3));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        float screenHeight = displaymetrics.heightPixels;
        Common.Height = (int) ((b / 100f) * screenHeight);
        Cursor c9 = db.getTitle(9);
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
                //rService.setAlpha(Common.Alpha);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int a = paramAnonymousSeekBar.getProgress();
                    TextPercent.setText(a + "%");
                    // SQLiteDatabase localSQLiteDatabase = mDBHelper.getWritableDatabase();
                    Common.Alpha = 200 - a * 2;
                    //  mDBHelper.putKeyData(localSQLiteDatabase, "Alpha", (Integer.toString(a)));
                    //  rService.setAlpha(Common.Alpha);
                } catch (IllegalStateException ignored) {
                }
            }
        });
        Sliderb.setMax(100);
        Log.d("r", String.valueOf(Common.passedonce));
        if (Common.passedonce.equals("Y")) {
            Sliderb.setProgress(b);
        } else {
            Sliderb.setProgress(50);
            //localSQLiteDatabase = mDBHelper.getWritableDatabase();
            // mDBHelper.putKeyData(localSQLiteDatabase, "passedonce", ("Y"));
        }
        Sliderb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Height = (int) ((paramAnonymousInt / 100f) * screenHeight);
                //  rService.setHeight(Common.Height);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int b = paramAnonymousSeekBar.getProgress();
                    //SQLiteDatabase localSQLiteDatabase = mDBHelper.getWritableDatabase();
                    //mDBHelper.putKeyData(localSQLiteDatabase, "Height", (Integer.toString(b)));
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                    float screenHeight = displaymetrics.heightPixels;
                    Common.Height = (int) ((b / 100f) * screenHeight);
                    //  rService.setHeight(Common.Height);
                } catch (IllegalStateException ignored) {
                }
            }
        });
        Sliderc.setMax(100);
        Sliderc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Area = (int) ((((paramAnonymousInt - 50) * 2) / 100f) * (screenHeight / 2) * -1);
                //rService.setArea(Common.Area);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                int c = paramAnonymousSeekBar.getProgress();
                //SQLiteDatabase localSQLiteDatabase = mDBHelper.getWritableDatabase();
                // mDBHelper.putKeyData(localSQLiteDatabase, "Area", (Integer.toString(c)));
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Area = (int) ((((c - 50) * 2) / 100f) * (screenHeight / 2) * -1);
                // rService.setArea(Common.Area);
            }
        });
        TextPercent.setText(Slider.getProgress() + "%");
    }

    public void StartToggle(View view) {
        if (ToggleButton1.isChecked()) {
            ToggleButton2.setEnabled(false);
        } else {
            ToggleButton2.setEnabled(true);
        }
    }

    public void GradientToggle(View view) {
        if (ToggleButton2.isChecked()) {
            Log.d("e", Common.GradientType);
            final DatabaseActivity db = new DatabaseActivity(this);
            db.open();
            if (ToggleButton2.isChecked()) {
                db.updateTitle(3,
                        "3",
                        "GradientYN",
                        "Y");
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
                                            db.updateTitle(3,
                                                    "4",
                                                    "GradientType",
                                                    "1");
                                        } else {
                                            if (i == 1) {
                                                Common.GradientType = String.valueOf(2);
                                                db.updateTitle(3,
                                                        "4",
                                                        "GradientType",
                                                        "2");
                                            } else {
                                                if (i == 2) {
                                                    Common.GradientType = String.valueOf(3);
                                                    db.updateTitle(3,
                                                            "4",
                                                            "GradientType",
                                                            "3");
                                                }
                                            }
                                        }
                                    }
                                })
                                .show();
                    }
                });
            } else {
                db.updateTitle(3,
                        "3",
                        "GradientYN",
                        "Y");
                db.close();
            }
        }
    }

    public void ColorPicker(View view) {
//Color
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
            //settings
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
