package com.example.kalarilab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.SliderAdapter;


public class OnBoarding extends BaseActivity implements View.OnClickListener {
    private LinearLayout dotsLayout;
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private SessionManagement sessionManagement;
    private TextView[] dots;
    private   Button getStartedBtn, nextBtn;
    private ConstraintLayout mainLayout;
    private int mCurrentPage;
    private VideoView videoView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        initHooks();


    }

    private void onBoardingAlreadySeen() {
        sessionManagement.onBoardingSeen();;
    }

    @Override
    protected void onStart() {
        super.onStart();
     //   startFirstVideo();
    }

    private void initHooks() {

       viewPager = findViewById(R.id.viewPager);
       dotsLayout = findViewById(R.id.dotsLayout);
       sliderAdapter = new SliderAdapter(this);
        sessionManagement = new SessionManagement(this);
        sessionManagement.setFRESH_INSTALLStatus(false);
        mainLayout = findViewById(R.id.main);
        videoView = findViewById(R.id.videoView);
        viewPager.setAdapter(sliderAdapter);
        getStartedBtn = findViewById(R.id.getStartedBtn);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);
        getStartedBtn.setOnClickListener(this);
        addDotsIndicators(0);
        viewPager.addOnPageChangeListener(viewListener);
    }


    private void addDotsIndicators(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        getStartedBtn.setVisibility(View.GONE);
        nextBtn.setVisibility(View.VISIBLE);
        startFirstVideo();

        for(int i = 0 ; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.lightGrey));
            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.KalariLAbSecondary));
        }

    }

    private void startFirstVideo() {
        try {
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.train_at_home);
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicators(position);
            if (position == 2) {
                removeNextButton();
                addGetStartedButton();
            }
            mCurrentPage = position;
            setVideoPath(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setVideoPath(int position) {
        switch (position){
            case 0:
              //  videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.train_at_home);
                break;
            case 1:
              //  videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.transform_your_body);
                break;
            case 2:
              //  videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.build_absolute_focus);

        }
       // videoView.start();

    }

    private void removeNextButton() {
        nextBtn.setVisibility(View.GONE);
    }

    private void addGetStartedButton() {

        getStartedBtn.setVisibility(View.VISIBLE);

    }



    private void moveToSignInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.getStartedBtn:
                onBoardingAlreadySeen();
                moveToSignInActivity();
                break;
            case R.id.nextBtn:
                viewPager.setCurrentItem(mCurrentPage + 1);
                break;

        }
    }
}