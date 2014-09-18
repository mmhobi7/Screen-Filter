package com.aaahh.yello;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private TextView TextPercent;
    private TextView ToggleButton1;
    private ToggleButton ToggleButton2;
    private Button SelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        if (Common.boot) {
            moveTaskToBack(true);
        }
        MainActivity mThis = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        TextPercent = ((TextView) findViewById(R.id.textView));
        ToggleButton1 = ((ToggleButton) findViewById(R.id.toggleButton));
        ToggleButton2 = ((ToggleButton) findViewById(R.id.toggleButton2));
        SelectButton = ((Button) findViewById(R.id.button));
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
