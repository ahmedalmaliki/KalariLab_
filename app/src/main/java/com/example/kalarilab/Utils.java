package com.example.kalarilab;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class Utils {

    public static void LockGlobalScreen(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
        }
    }

    public static void UnlockGlobalScreen(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            );
        }
    }
}

