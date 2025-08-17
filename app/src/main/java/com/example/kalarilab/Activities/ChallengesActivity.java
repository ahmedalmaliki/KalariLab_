package com.example.kalarilab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kalarilab.Fragments.Camera2VideoFragment;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.nikartm.support.ImageBadgeView;

public class ChallengesActivity extends AppCompatActivity {

    private static final String TAG = "CAMERA_TUTORIAL";
    private androidx.appcompat.widget.Toolbar mToolbar ;
    private SessionManagement sessionManagement;
    private StyledPlayerView styledPlayerView;
    private ExoPlayer exoPlayer;
    private     Uri currUri;
    private     MediaItem mediaItem;
    private ProgressBar progressBar;








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        mToolbar = findViewById(R.id.topAppBar);
        sessionManagement = new SessionManagement(this);
        styledPlayerView = findViewById(R.id.player);
        exoPlayer = new ExoPlayer.Builder(this).build();
        styledPlayerView.setPlayer(exoPlayer);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.camera_fragment, Camera2VideoFragment.newInstance())
                .commit();
        getUri();
        checkIfThereISFeedback();

    }

    private void checkIfThereISFeedback() {
        ImageBadgeView imageBadgeView = findViewById(R.id.feedback);

        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("FeedBack").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).child(String.valueOf(sessionManagement.returnCurrLevel())).child(String.valueOf(sessionManagement.returnCurrChallenge())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, sessionManagement.returnCurrLevel()+" "+sessionManagement.returnCurrChallenge());
                    if(snapshot.exists()){
                        imageBadgeView.setBadgeValue(1);
                        imageBadgeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ChallengesActivity.this , FeedbackActivity.class);
                                intent.putExtra("feedback", snapshot.getValue().toString());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }else                         imageBadgeView.setBadgeValue(0);


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

    @Override
    public void onBackPressed() {
        onStop();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    private void getUri(){
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, String.valueOf(sessionManagement.returnCurrLevel()) + String.valueOf(sessionManagement.returnCurrChallenge()));
                FirebaseDatabase.getInstance().getReference("Challenges").child(String.valueOf(sessionManagement.returnCurrLevel())).child(String.valueOf(sessionManagement.returnCurrChallenge())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        currUri = Uri.parse(snapshot.getValue().toString());
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
    private void runVid() {
        try {
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("uriDebug", "Error : " + e.toString());
        }
    }
}