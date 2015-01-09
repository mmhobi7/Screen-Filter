package com.aaahh.yellow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

/**
 * Created by aaahh on 9/20/14. Using Holo Color Picker
 */
public class Color extends Activity {
    public static Color mThis;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.color);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        mThis = this;
        getWindow().setFlags(4, 4);
        Common.OColor = Common.Color;
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
//        picker.setOldCenterColor(picker.getColor());
        picker.setShowOldCenterColor(false);
        picker.setColor(Common.Color);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Common.Color = picker.getColor();
                FilterService.mThis.setColor();
            }
        });
    }

    public void setCancel(View view) {
        Common.Color = Common.OColor;
        FilterService.mThis.setColor();
        this.finish();
    }

    public void setOkay(View view) {
        FilterService.mThis.setColor();
        SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Color", Common.Color);
        editor.apply();
        this.finish();
    }
}