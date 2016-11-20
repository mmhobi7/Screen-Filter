package com.aaahh.yellow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

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
        OColor = Common.Color;
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setShowOldCenterColor();
        picker.setColor(Common.Color);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged() {
                Common.Color = picker.getColor();
                FilterService.setRotation(mContext);
            }
        });
    }

    public void setCancel(View view) {
        Common.Color = OColor;
        FilterService.setRotation(this);
        this.finish();
    }

    public void setOkay(View view) {
        FilterService.setRotation(this);
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Color", Common.Color);
        editor.apply();
        this.finish();
    }
}