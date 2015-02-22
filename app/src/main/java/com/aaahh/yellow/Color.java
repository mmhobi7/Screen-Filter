package com.aaahh.yellow;

import android.app.Activity;
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
        setContentView(R.layout.color);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        getWindow().setFlags(4, 4);
        if (Common.Layer == 1) {
            OColor = Common.Color;
        }
        if (Common.Layer == 2) {
            OColor = Common.Color2;
        }
        if (Common.Layer == 3) {
            OColor = Common.Color3;
        }
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setShowOldCenterColor();
        picker.setColor(OColor);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged() {
                if (Common.Layer == 1) {
                    Common.Color = picker.getColor();
                }
                if (Common.Layer == 2) {
                    Common.Color2 = picker.getColor();
                }
                if (Common.Layer == 3) {
                    Common.Color3 = picker.getColor();
                }
                FilterService.mThis.setRotation();
            }
        });
    }

    public void setCancel(View view) {
        if (Common.Layer == 1) {
            Common.Color = OColor;
        }
        if (Common.Layer == 2) {
            Common.Color2 = OColor;
        }
        if (Common.Layer == 3) {
            Common.Color3 = OColor;
        }
        FilterService.mThis.setRotation();
        this.finish();
    }

    public void setOkay(View view) {
        FilterService.mThis.setRotation();
        if (Common.Layer == 1) {
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Color", Common.Color);
            editor.apply();
        }
        if (Common.Layer == 2) {
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME2, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Color", Common.Color2);
            editor.apply();
        }
        if (Common.Layer == 3) {
            SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME3, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("Color", Common.Color3);
            editor.apply();
        }
        this.finish();
    }
}