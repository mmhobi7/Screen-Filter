package com.aaahh.yello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by aaahh on 11/18/14.
 */
public class Launcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
