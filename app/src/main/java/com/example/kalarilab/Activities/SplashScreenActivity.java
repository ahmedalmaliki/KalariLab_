package com.example.kalarilab.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.kalarilab.Activities.BaseActivity;
import com.example.kalarilab.Activities.LogInActivity;
import com.example.kalarilab.R;
//import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends BaseActivity {
    //the activity runs the splash screen in the beginning.
    private Timer timer; // This timer controls the time the splash screen stays up.
    private  Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initMobileAds();

        setContentView(R.layout.activity_splash_screen);
        if (!isTaskRoot()) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        initHooks();

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

    private void initHooks() {
        context = this;
        timer = new Timer();
        timer.schedule(new RemindTask(), 3000); // The timer is scheduled for three seconds.
    }
    class RemindTask extends TimerTask{

        @Override
        public void run() {
            this.moveToLogInActivity();
        }
        private void moveToLogInActivity() {
            //After the timer ends the UI moves to the Log in  activity.
            Intent intent = new Intent(context, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }


}