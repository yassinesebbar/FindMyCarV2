package com.example.findmycarv2;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withBackgroundColor(Color.parseColor("#000000"))
                .withTargetActivity(MapsActivity.class)
                .withSplashTimeOut(6000)
                .withLogo(R.mipmap.appfront);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}