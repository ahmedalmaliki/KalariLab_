package com.example.kalarilab.Activities;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kalarilab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class App extends Application {
     final static String TAG = "BaseActivityDebug";
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,  getClass().getSimpleName() );

    }

//    private Activity mCurrentActivity = null;
//    public Activity getCurrentActivity(){
//        return mCurrentActivity;
//    }
//    public void setCurrentActivity(Activity mCurrentActivity){
//        this.mCurrentActivity = mCurrentActivity;
//    }


}
