package com.aaahh.yellow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Aaahh on 11/18/14.
 * This is a launcher so that I can hide my app.
 */
public class Launcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
