package com.example.kalarilab.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kalarilab.Fragments.Camera2VideoFragment;
import com.example.kalarilab.R;
import com.r0adkll.slidr.Slidr;

public class FeedbackActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar mToolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        TextView feedback = findViewById(R.id.feedbackMsg);
        Intent intent = getIntent();
        feedback.setText(intent.getStringExtra("feedback"));
        mToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Slidr.attach(this);


    }


}