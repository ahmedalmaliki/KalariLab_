package com.example.kalarilab.Activities;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.kalarilab.Fragments.FragmentAdapter;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends BaseActivity {


    public static  ViewPager viewPager;


    private BottomNavigationView bottomNavigationView;
    private ColorStateList navigationViewColorStateList;
    private FragmentAdapter fragmentAdapter;
    private SessionManagement sessionManagement ;
    private final static String TAG = "MainActivityDebug";

    // FOR NAVIGATION VIEW ITEM ICON COLOR
    int[][] states = new int[][]{
            new int[]{-android.R.attr.state_checked},  // unchecked
            new int[]{android.R.attr.state_checked},   // checked
            new int[]{}                                // default
    };
    // Fill in color corresponding to state defined in state
    int[] colors = new int[]{
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#ce262f"),
            Color.parseColor("#aaa8a8"),
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHooks();//Initiates variables and objects
        bindings();// preform necessary bindings
        receiveIntentFragment();//recieves the fragment intended and runs it
        onNewToken();

    }

    private void onNewToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, token);
                        if(!Objects.equals(token, sessionManagement.return_token())){
                            sessionManagement.save_token(token);
                            sendTokenToDatabase(token);
                        }
                    }
                });
    }

    private void sendTokenToDatabase(String token) {
        try{
            FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).setValue(token);
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }

    }


    private void initHooks() {



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        navigationViewColorStateList = new ColorStateList(states, colors);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        sessionManagement  = new SessionManagement(this);
    }
    private void bindings() {
        bottomNavigationView.setItemIconTintList(navigationViewColorStateList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // A listener that preforms action on a certain change
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateNavigationBarState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(fragmentAdapter);
        bottomNavigationView.setOnItemSelectedListener(item -> { // sets the fragments to their corresponding item
            switch (item.getItemId()){
                case R.id.home_page:

                    viewPager.setCurrentItem(0);

                    break;
                case R.id.profile_page:
                    viewPager.setCurrentItem(2);

                    break;


                case R.id.levels_page:

                    viewPager.setCurrentItem(1);

            }

            return true;
        });


    }






    private void receiveIntentFragment(){
       // receives info about which fragment to run
//
//        String intentFragment = sessionManagement.returnLatestFragment();
//            switch (intentFragment) {
//                case "PROFILE_FRAGMENT":
//                    viewPager.setCurrentItem(3);
//                    break;
//                case "LEVELS_FRAGMENT":
//                    viewPager.setCurrentItem(1);
//                    break;
//                default:
//                    viewPager.setCurrentItem(0);
//                    break;
//
//        }
    }





    public void updateNavigationBarState(int position){
        //updates which icon is currently selected on the bottom bar
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(position);
        item.setChecked(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfCompatibleWithGoogleServices();
    }

    private void checkIfCompatibleWithGoogleServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            // Google Play services is available on the device
        } else {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);
        }

    }
}