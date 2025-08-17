package com.example.kalarilab.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.kalarilab.Activities.BaseActivity;
import com.example.kalarilab.Activities.LogInActivity;
import com.example.kalarilab.R;
//import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends BaseActivity {
    //the activity runs the splash screen in the beginning.
    private Timer timer; // This timer controls the time the splash screen stays up.
    private Context context;
    private VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initMobileAds();

        setContentView(R.layout.activity_splash_screen);
        // Make it fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        videoView = findViewById(R.id.splashVideo);

        if (!isTaskRoot()) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }

        String path = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        videoView.setVideoURI(Uri.parse(path));

        videoView.setOnCompletionListener(mp -> {
            // After video finishes -> go to main
            startActivity(new Intent(SplashScreenActivity.this, LogInActivity.class));
            finish();
        });

        videoView.start();

    }

//    private void initMobileAds() {
//
//        Thread adsInitThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MobileAds.initialize(getApplicationContext());
//            }
//        });
//        adsInitThread.run();
//    }






}