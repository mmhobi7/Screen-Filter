package com.aaahh.yello;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
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
        picker.setColor(Common.BgColor);
        Common.OColor = Common.BgColor;
        Log.d("1", "2");
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
//        picker.setOldCenterColor(picker.getColor());
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Common.BgColor = picker.getColor();
                Common.Color = picker.getColor();
                Log.d("l", "p");
            }
        });
        picker.setShowOldCenterColor(false);
        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                Common.BgColor = picker.getColor();
                Common.Color = picker.getColor();
            }
        });
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {
                Common.BgColor = picker.getColor();
                Common.Color = picker.getColor();
            }
        });
    }

    public void setCancel(View view) {
        Common.BgColor = Common.OColor;
        this.finish();
    }

    public void setOkay(View view) {
        Common.BgColor = Common.Color;
        this.finish();
    }
}