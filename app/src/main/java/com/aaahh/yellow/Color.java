package com.aaahh.yellow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import static com.aaahh.yellow.FilterService.vw;
import static com.aaahh.yellow.MainActivity.currentlayer;

/**
 * Created by Aaahh on 9/20/14. Using Holo Color Picker
 */
public class Color extends Activity {
    private int OColor;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        final Context mContext = this;
        setContentView(R.layout.color);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        getWindow().setFlags(4, 4);
        OColor = Common.Color.get(currentlayer);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setShowOldCenterColor();
        picker.setColor(Common.Color.get(currentlayer));
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged() {
                Common.Color.set(currentlayer, picker.getColor());
                if (vw != null) {
                    FilterService.setRotation(mContext, vw.toArray(new View[vw.size()]));
                }
            }
        });
    }

    public void setCancel(View view) {
        Common.Color.set(currentlayer, OColor);
        if (vw != null) {
            FilterService.setRotation(this, vw.toArray(new View[vw.size()]));
        }
        this.finish();
    }

    public void setOkay(View view) {
        if (vw != null) {
            FilterService.setRotation(this, vw.toArray(new View[vw.size()]));
        }
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Color", Common.Color.get(currentlayer));
        editor.apply();
        this.finish();
    }
}