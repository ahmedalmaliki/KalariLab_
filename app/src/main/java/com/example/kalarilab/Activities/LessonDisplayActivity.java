package com.example.kalarilab.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kalarilab.ProgressTrackingSystem;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Vid;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LessonDisplayActivity extends AppCompatActivity {


    int currLesson;
    int currLevel;
    private androidx.appcompat.widget.Toolbar toolbar ;


    Uri currUri;
    StyledPlayerView styledPlayerView;
    MediaItem mediaItem;
    ExoPlayer exoPlayer;
    ProgressBar progressBar;
    Runnable runnable;
    Handler handler;
    ProgressTrackingSystem progressTrackingSystem;
    SessionManagement sessionManagement;
    Map<String, String> alreadyWatchedLessons;
    private long mPlaybackPosition;
    final static String TAG = "LessonDisplayDebug";

    private static final String PLAYBACK_POSITION_KEY = "playback_position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_display);
        Slidr.attach(this);

        initHooks();
        if (savedInstanceState != null) {
            // Get the playback position from the Bundle.
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
        }
        progressBar.setVisibility(View.VISIBLE);
        bindings();
        getUri();
    }

    private void runVid() {
        try {
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            
            exoPlayer.seekTo(mPlaybackPosition);
            exoPlayer.play();
            progressBar.setVisibility(View.GONE);
            checkIfLessonAlreadyWatched();
        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("uriDebug", "Error : " + e.toString());
        }
    }

    private void checkIfLessonAlreadyWatched() {


        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("WatchedLessons").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GenericTypeIndicator<Map<String, String>> to = new
                                GenericTypeIndicator<Map<String,String>>() {};
                        alreadyWatchedLessons = snapshot.getValue(to);
                        if ( alreadyWatchedLessons != null){

                            if (alreadyWatchedLessons.containsKey("level "+String.valueOf(currLevel))  ){
                            ArrayList<String> watchedLessons =  new ArrayList<>(Arrays.asList(alreadyWatchedLessons.get("level "+String.valueOf(currLevel)).split(",")));

                            if ( watchedLessons.contains("lesson " +String.valueOf( currLesson))){
                                //Do nothing

                        }
                            else{

                                checkProgress();
                            }}}

                        else {

                            checkProgress();
                        }


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

    private void addTheUpdatedMapToDB(Map<String , String> watchedLessons) {
        FirebaseDatabase.getInstance().getReference("WatchedLessons").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).setValue(watchedLessons);
    }

    private void getUri(){
        Log.d(TAG, String.valueOf(currLesson)+":"+String.valueOf(currLevel));
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Vids").child(String.valueOf(currLevel)).child(String.valueOf(currLesson)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Vid vid = snapshot.getValue(Vid.class);
                        assert vid != null;
                        currUri = Uri.parse(vid.getUri());
                        Log.d(TAG, currUri.toString());

                        mediaItem = MediaItem.fromUri(currUri);
                        runVid();


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.run();

        } catch (Exception e) {

        }

    }

    private void bindings() {
        Bundle bundle = getIntent().getExtras();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currLesson = bundle.getInt("lesson");
        currLevel  = bundle.getInt("level");
        toolbar.setTitle(new StringBuilder().append("Lesson ").append(currLesson).toString());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.back:
                        moveToMainActivity();
                        finish();

                        break;
                }

                return false;
            }
        });



    }
    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("frgToLoad", "LEVELS_FRAGMENT");
        startActivity(intent);
    }
    private void initHooks() {
        progressTrackingSystem = new ProgressTrackingSystem();
        toolbar = findViewById(R.id.topAppBar);
        styledPlayerView = findViewById(R.id.player);
        exoPlayer = new ExoPlayer.Builder(this).build();
        styledPlayerView.setPlayer(exoPlayer);
        progressBar = findViewById(R.id.pBar);
        sessionManagement = new SessionManagement(this);


    }

    private void checkProgress() {

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 1000);
                if ((exoPlayer.getCurrentPosition() * 100 )/ exoPlayer.getDuration() > 60){
                    progressTrackingSystem.addPointsAfterFinishingLessons();
                    uploadLevelAndLessonProgress();
                    addLessonToWatchedLessons();
                    try {
                        handler.removeCallbacksAndMessages(null);

                    }catch (Exception e){
                        
                    }

                }
            }
        };
        handler.postDelayed(runnable, 0);


    }

    private void addLessonToWatchedLessons() {
        //add the newly watched lesson to a string that separate all watched lessons with a commas

        if (alreadyWatchedLessons != null){
            alreadyWatchedLessons.put("level "+String.valueOf(currLevel), alreadyWatchedLessons.get("level "+String.valueOf(currLevel)) + "lesson " +String.valueOf( currLesson)+"," ) ;
            addTheUpdatedMapToDB(alreadyWatchedLessons);
        }else{
            alreadyWatchedLessons = new HashMap<>();
            alreadyWatchedLessons.put("level "+String.valueOf(currLevel), "lesson " +String.valueOf( currLesson)+"," ) ;
            addTheUpdatedMapToDB(alreadyWatchedLessons);

        }
    }

    private void uploadLevelAndLessonProgress() {
        if(NoHigherProgressHasBeenAchievedBefore()){

            progressTrackingSystem.uploadLessonReached(currLesson);
            progressTrackingSystem.uploadLevelReached(currLevel);
            sessionManagement.saveLevel_Reached(currLevel);
            sessionManagement.saveLesson_Reached(currLesson);

        }
        //Do nothing

    }

    private boolean NoHigherProgressHasBeenAchievedBefore() {
        if (currLevel >= sessionManagement.returnLevel_Reached() ){
            if(currLesson > sessionManagement.returnLesson_Reached()){
                return true;}
            else{
                return false;
                }

            }else return false;
        }



    @Override
    protected void onStop() {
        mPlaybackPosition = exoPlayer.getCurrentPosition();

        // Stop playback.
        exoPlayer.setPlayWhenReady(false);
        try {
            handler.removeCallbacksAndMessages(null);

        }catch (Exception e){

        }

        super.onStop();
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the playback position in the Bundle.
        outState.putLong(PLAYBACK_POSITION_KEY, mPlaybackPosition);
    }


}