package com.example.kalarilab.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kalarilab.AwardedPostures;
import com.example.kalarilab.PosturesAdapter;
import com.example.kalarilab.ProgressTrackingSystem;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PosturesActivity extends BaseActivity {

    private PosturesAdapter posturesAdapter;
    private ProgressTrackingSystem progressTrackingSystem;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private ViewPager2 viewPager;
    private List<String> postures = new ArrayList<>();
    private final static String TAG = "PosturesActivityDebug";
     private Context context ;
     private SessionManagement sessionManagement;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture);
        //Slidr.attach(this); //takes care of sliding to exit current page
        initHooks();
        getPostures();

        bindings();



    }

    private void getUnAwardedPostures() {
        Thread dataBaseThread = new Thread(new Runnable() {

            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Postures").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren()){
                                if(!postures.contains(ds.getKey()) && !Objects.requireNonNull(ds.getKey()).contains("Imature") ){
                                    if(!Objects.equals(ds.getKey(), "length")) {
                                        Log.d(TAG, ds.getKey());

                                        postures.add(ds.getKey() + "Silhouette");
                                    }
                               }
                            }
                            Log.d(TAG, postures.toString());
                            passPosturesToAdapter();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }

    }

    private void initHooks(){
        progressTrackingSystem = new ProgressTrackingSystem();
         //passing info to the adapter to organise the number and type of postures opened

        mToolbar = findViewById(R.id.topAppBar);
        sessionManagement = new SessionManagement(this);

    }

    private void getPostures() {

         AwardedPostures awardedPostures = new AwardedPostures();
        progressTrackingSystem.getAwardedPostures(awardedPostures);

        Thread dataBaseThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!awardedPostures.Imported()){
                    synchronized (awardedPostures){
                        try {

                            Log.d("Waiting_thread", "waiting");

                            awardedPostures.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                postures = new ArrayList<String>(Arrays.asList(awardedPostures.getAwardedPostures().split(",")));
                if(Objects.equals(postures.get(0), "")){ //when there is no awarded postures there will be only a comma in the list
                    postures.remove(0);
                }
                    getUnAwardedPostures();
                    replaceImmatureWithMature();

            }
        });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }


    }

    private void replaceImmatureWithMature() {

            for (Iterator<String> iterator = postures.iterator(); iterator.hasNext(); ) {
                String value = iterator.next();
                if (value.contains("Imature")) {
                    if(postures.contains(value.replace("Imature ", ""))){
                        iterator.remove();

                    }
                }
                Log.d(TAG+"it", iterator.toString());
                }
            }



    private void passPosturesToAdapter() {
        Log.d(TAG, postures.toString());
        PosturesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                posturesAdapter = new PosturesAdapter( getSupportFragmentManager(), PosturesActivity.this, postures, getLifecycle());
                posturesAdapter.setNUM_ITEMS(postures.size() );
                viewPager = (ViewPager2) findViewById(R.id.pager);
                viewPager.setAdapter(posturesAdapter);

            }
        });


    }

    private void bindings() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }








    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





}