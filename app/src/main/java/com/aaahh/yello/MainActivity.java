package com.aaahh.yello;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    public ToggleButton ToggleButton2;
    private TextView TextPercent;
    private ToggleButton ToggleButton1;
    private Button SelectButton;
    private SeekBar Slider;
    private SeekBar Sliderb;
    private SeekBar Sliderc;
    private ImageView ColorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
                    "0");
            id = db.insertTitle(
                    "1",
                    "First",
                    "1");
        }
       // db.close();
        if (Common.boot.contains("1")) {
            moveTaskToBack(true);
        }
        MainActivity mThis = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textView));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton2));
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
        TextPercent.setText(a + "%");
        // Common.Alpha = (200- a *2);
        Cursor c8 = db.getTitle(8);
        int b = Integer.parseInt(c8.getString(3));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        float screenHeight = displaymetrics.heightPixels;
        Common.Height = (int) ((b / 100f) * screenHeight);
        Cursor c9 = db.getTitle(9);
        int q = Integer.parseInt(c9.getString(3));
        Common.Area = (int) ((((q - 50) * 2) / 100f) * (screenHeight / 2) * -1);
        this.ToggleButton2.setChecked(false);
        if (Common.FilterYN.equals("Y")) {
            this.ToggleButton1.setChecked(true);
            this.ToggleButton2.setEnabled(false);
            Common.Receiver = true;
        }
        if (Common.GradientYN.equals("Y")) {
            this.ToggleButton2.setChecked(true);
        } else {
            this.ToggleButton2.setChecked(false);
        }
        //int j = Common.converToDecimalFromHex(Common.BgColor);
        // ColorView.setBackgroundColor(j);
        ToggleButton2.setChecked(false);
        Slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                MainActivity.this.TextPercent.setText(paramAnonymousInt + "%");
                Common.Alpha = 200 - paramAnonymousInt * 2;
                //MainActivity.this.rService.setAlpha(Common.Alpha);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int a = paramAnonymousSeekBar.getProgress();
                    MainActivity.this.TextPercent.setText(a + "%");
                    // SQLiteDatabase localSQLiteDatabase = MainActivity.mDBHelper.getWritableDatabase();
                    Common.Alpha = 200 - a * 2;
                    //  MainActivity.mDBHelper.putKeyData(localSQLiteDatabase, "Alpha", (Integer.toString(a)));
                    //  MainActivity.this.rService.setAlpha(Common.Alpha);
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
            //localSQLiteDatabase = MainActivity.mDBHelper.getWritableDatabase();
            // MainActivity.mDBHelper.putKeyData(localSQLiteDatabase, "passedonce", ("Y"));
        }
        Sliderb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Height = (int) ((paramAnonymousInt / 100f) * screenHeight);
                //  MainActivity.this.rService.setHeight(Common.Height);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                try {
                    int b = paramAnonymousSeekBar.getProgress();
                    //SQLiteDatabase localSQLiteDatabase = MainActivity.mDBHelper.getWritableDatabase();
                    //MainActivity.mDBHelper.putKeyData(localSQLiteDatabase, "Height", (Integer.toString(b)));
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                    float screenHeight = displaymetrics.heightPixels;
                    Common.Height = (int) ((b / 100f) * screenHeight);
                    //  MainActivity.this.rService.setHeight(Common.Height);
                } catch (IllegalStateException ignored) {
                }
            }
        });
        Sliderc.setMax(100);
        if (Common.passedonce.equals("Y")) {
            Sliderc.setProgress(q);
        } else {
            Sliderc.setProgress(50);
            //  localSQLiteDatabase = MainActivity.mDBHelper.getWritableDatabase();
            // MainActivity.mDBHelper.putKeyData(localSQLiteDatabase, "passedonce", ("Y"));
        }
        Sliderc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Area = (int) ((((paramAnonymousInt - 50) * 2) / 100f) * (screenHeight / 2) * -1);
                //MainActivity.this.rService.setArea(Common.Area);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                int c = paramAnonymousSeekBar.getProgress();
                //SQLiteDatabase localSQLiteDatabase = MainActivity.mDBHelper.getWritableDatabase();
                // MainActivity.mDBHelper.putKeyData(localSQLiteDatabase, "Area", (Integer.toString(c)));
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
                float screenHeight = displaymetrics.heightPixels;
                Common.Area = (int) ((((c - 50) * 2) / 100f) * (screenHeight / 2) * -1);
                // MainActivity.this.rService.setArea(Common.Area);
            }
        });
        MainActivity.this.TextPercent.setText(Slider.getProgress() + "%");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void StartToggle(View view) {
        if (ToggleButton1.isActivated()) {

        } else {

        }
    }

    public void GradientToggle(View view) {
        if (ToggleButton2.isActivated()) {

        } else {

        }
    }

    public void ColorPicker(View view) {
        if (SelectButton.isActivated()) {

        } else {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
