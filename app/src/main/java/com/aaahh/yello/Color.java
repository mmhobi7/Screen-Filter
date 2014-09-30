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
    public static ColorPicker picker;

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
        // Common.BgColor = Common.Color;
        FilterService.mThis.setColor();
        DatabaseActivity db = new DatabaseActivity(this);
        db.open();
        db.updateTitle(2,
                "5",
                "BgColor",
                String.valueOf(Common.Color));
        db.close();
        this.finish();
    }
}