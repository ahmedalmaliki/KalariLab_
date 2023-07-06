package com.example.kalarilab.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {
    protected App mMyApp;
    final static String TAG = "BaseActivityDebug";
    private int currentActivityOrientation   = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    private int parentActivityOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (App)this.getApplicationContext();
        Log.d(TAG,  getClass().getSimpleName() );
        this.cacheOrientations();

    }
    private void cacheOrientations()
    {
        if (this.currentActivityOrientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        {
            final Intent parentIntent = this.getParentActivityIntent();

            if (parentIntent != null)
            {
                final ComponentName parentComponentName = parentIntent.getComponent();

                if (parentComponentName != null)
                {
                    this.currentActivityOrientation = this.getConfiguredOrientation(this.getComponentName());
                    this.parentActivityOrientation = this.getConfiguredOrientation(parentComponentName);
                }
            }
        }
    }
    private int getConfiguredOrientation(@NonNull final ComponentName source)
    {
        try
        {
            final PackageManager packageManager = this.getPackageManager();
            final ActivityInfo   activityInfo   = packageManager.getActivityInfo(source, 0);
            return activityInfo.screenOrientation;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }
    @CallSuper
    @Override
    protected void onPause()
    {
        if (this.parentActivityOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        {
            this.setRequestedOrientation(this.parentActivityOrientation);
        }

        super.onPause();
    }
    @CallSuper
    @Override
    protected void onResume()
    {
        super.onResume();

        if (this.currentActivityOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        {
            this.setRequestedOrientation(this.currentActivityOrientation);
        }
    }
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }


}
