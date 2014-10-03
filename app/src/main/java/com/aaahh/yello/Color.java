package com.aaahh.yello;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
                Log.d("gish", String.valueOf(Common.Color));
            }
        });
    }

    public void setCancel(View view) {
        Common.Color = Common.OColor;
        FilterService.mThis.setColor();
        this.finish();
    }

    public void setOkay(View view) {
        // Common.BgColor = Common.Color;
        FilterService.mThis.setColor();
        // "5",
        //   "BgColor",
        //     String.valueOf(Common.Color));
        this.finish();
    }
}