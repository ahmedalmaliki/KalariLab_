package com.example.kalarilab.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalarilab.Activities.ChallengesActivity;
import com.example.kalarilab.Activities.LessonDisplayActivity;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.TiledImageAdapter;
import com.example.kalarilab.ViewModels.AuthViewModel;
import com.example.kalarilab.ZeroPaddingItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LevelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LevelsFragment extends Fragment {
    SessionManagement sessionManagement;
    View view;
    RelativeLayout relativeLayout;
    ImageView lock, avatar;
    Map<String, int[]> lessonsAreas = new HashMap();
    public static double Y;
    public static double X;
    MotionEvent motionEvent;
    int clickedLesson;
    int level;
    int challenge;
    private AuthViewModel authViewModel;
    private AuthModel authModel1;
    RecyclerView map_recyclerView;
    TiledImageAdapter tiledImageAdapter;
    final static String TAG = "MAPDEBUG";
    Map<Integer, Integer> lockedKalaries = new HashMap();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LevelsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment classesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LevelsFragment newInstance(String param1, String param2) {
        LevelsFragment fragment = new LevelsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void moveToLessonsDisplayActivity() {
        Intent intent = new Intent(getActivity(), LessonDisplayActivity.class);
        intent.putExtra("lesson", clickedLesson);
        intent.putExtra("level", level);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    private void initHooks(View view) {
        map_recyclerView = view.findViewById(R.id.map_recyclerView);
        tiledImageAdapter = new TiledImageAdapter(getTiles(), getActivity());
        map_recyclerView.addItemDecoration(new ZeroPaddingItemDecoration());
        map_recyclerView.setAdapter(tiledImageAdapter);
        map_recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        relativeLayout = view.findViewById(R.id.levels_layout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Y = displayMetrics.heightPixels;
        X = displayMetrics.widthPixels;
        sessionManagement = new SessionManagement(getActivity());
        avatar = view.findViewById(R.id.avatar);
       // checkIfLocked();
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(getActivity());
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }

    }

    private void checkIfLocked() {
        ImageView lockView1 = new ImageView(getActivity());
//
//// Set the lock PNG image resource
        lockView1.setImageResource(R.drawable.lock);

//// Set the position of the ImageView using layout parameters
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(900, 650, 0,0);
        lockView1.setLayoutParams(layoutParams);
        relativeLayout.addView(lockView1);
// Add the ImageView to your map layout
    }

    private List<Drawable> getTiles() {
        List<Drawable> tiles = new ArrayList<>();
        tiles.add(getResources().getDrawable(R.drawable.map_01_01));
        tiles.add(getResources().getDrawable(R.drawable.map_01_02));
        tiles.add(getResources().getDrawable(R.drawable.map_01_03));
        tiles.add(getResources().getDrawable(R.drawable.map_01_04));
        tiles.add(getResources().getDrawable(R.drawable.map_01_05));
        tiles.add(getResources().getDrawable(R.drawable.map_01_06));
        tiles.add(getResources().getDrawable(R.drawable.map_01_07));
        tiles.add(getResources().getDrawable(R.drawable.map_01_08));
        tiles.add(getResources().getDrawable(R.drawable.map_01_09));
        tiles.add(getResources().getDrawable(R.drawable.map_01_10));
        tiles.add(getResources().getDrawable(R.drawable.map_01_11));
        tiles.add(getResources().getDrawable(R.drawable.map_01_12));
        tiles.add(getResources().getDrawable(R.drawable.map_01_13));
        tiles.add(getResources().getDrawable(R.drawable.map_01_14));
        tiles.add(getResources().getDrawable(R.drawable.map_01_15));
        tiles.add(getResources().getDrawable(R.drawable.map_01_16));
        tiles.add(getResources().getDrawable(R.drawable.map_01_17));
        tiles.add(getResources().getDrawable(R.drawable.map_01_18));
        tiles.add(getResources().getDrawable(R.drawable.map_01_19));
        tiles.add(getResources().getDrawable(R.drawable.map_01_20));
        return tiles;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_levels, container, false);
        initHooks(view);
        observeData();

        bindings();

        return view;

    }




    private void observeData() {
        authViewModel.getmAuthModel().observe(getActivity(), new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
                Log.d(TAG, String.valueOf(authModel1.getPoints()));
                setUpAvatar();

            }
        });
    }
    private  double[] getLessonOneXCoordinateRange(){
        double[] XCoordinateRange = {0.68, 0.74};
        return XCoordinateRange;
    }
    private  double[] getLessonOneYCoordinateRange(){
        double[] YCoordinateRange = {0.08, 0.12};
        return YCoordinateRange;
    }
    private  double[] getLessonTwoXCoordinateRange(){
        double[] XCoordinateRange = {0.16, 0.215};
        return XCoordinateRange;
    }
    private  double[] getLessonTwoYCoordinateRange(){
        double[] YCoordinateRange = {0.31, 0.34};
        return YCoordinateRange;
    }
    private  double[] getLessonThreeXCoordinateRange(){
        double[] XCoordinateRange = {0.29, 0.318};
        return XCoordinateRange;
    }
    private  double[] getLessonThreeYCoordinateRange(){
        double[] YCoordinateRange = {0.29, 0.33};
        return YCoordinateRange;
    }
    private  double[] getLessonFourXCoordinateRange(){
        double[] XCoordinateRange = {0.4, 0.43};
        return XCoordinateRange;
    }
    private  double[] getLessonFourYCoordinateRange(){
        double[] YCoordinateRange = {0.27, 0.28};
        return YCoordinateRange;
    }
    private  double[] getLessonFiveXCoordinateRange(){
        double[] XCoordinateRange = {0.41, 0.425};
        return XCoordinateRange;
    }
    private  double[] getLessonFiveYCoordinateRange(){
        double[] YCoordinateRange = {0.34, 0.365};
        return YCoordinateRange;
    }
    private  double[] getLessonSixXCoordinateRange(){
        double[] XCoordinateRange = {0.25, 0.275};
        return XCoordinateRange;
    }
    private  double[] getLessonSixYCoordinateRange(){
        double[] YCoordinateRange = {0.37,0.39};
        return YCoordinateRange;
    }
    private  double[] getLessonSevenXCoordinateRange(){
        double[] XCoordinateRange = {0.36,0.395};
        return XCoordinateRange;
    }
    private  double[] getLessonSevenYCoordinateRange(){
        double[] YCoordinateRange = {0.425,0.44 };
        return YCoordinateRange;
    }
    private  double[] getLessonEightXCoordinateRange(){
        double[] XCoordinateRange = {0.48, 0.52};
        return XCoordinateRange;
    }
    private  double[] getLessonEightYCoordinateRange(){
        double[] YCoordinateRange = {0.41, 0.435};
        return YCoordinateRange;
    }
    private  double[] getLessonNineXCoordinateRange(){
        double[] XCoordinateRange = {0.56, 0.575};
        return XCoordinateRange;
    }
    private  double[] getLessonNineYCoordinateRange(){
        double[] YCoordinateRange = {0.35, 0.365};
        return YCoordinateRange;
    }
    private  double[] getLessonTenXCoordinateRange(){
        double[] XCoordinateRange = {0.7, 0.72};
        return XCoordinateRange;
    }
    private  double[] getLessonTenYCoordinateRange(){
        double[] YCoordinateRange = {0.41,0.43};
        return YCoordinateRange;
    }
    private  double[] getLessonElevenXCoordinateRange(){
        double[] XCoordinateRange = {0.582, 0.61};
        return XCoordinateRange;
    }
    private  double[] getLessonElevenYCoordinateRange(){
        double[] YCoordinateRange = {0.45, 0.465};
        return YCoordinateRange;
    }
    private  double[] getLessonTwelveXCoordinateRange(){
        double[] XCoordinateRange = {0.50, 0.537};
        return XCoordinateRange;
    }
    private  double[] getLessonTwelveYCoordinateRange(){
        double[] YCoordinateRange = {0.51, 0.535};
        return YCoordinateRange;
    }
    private  double[] getLessonThirteenXCoordinateRange(){
        double[] XCoordinateRange = {0.385,0.41};
        return XCoordinateRange;
    }
    private  double[] getLessonThirteenYCoordinateRange(){
        double[] YCoordinateRange = {0.51, 0.53};
        return YCoordinateRange;
    }
    private  double[] getLessonFourteenXCoordinateRange(){
        double[] XCoordinateRange = {0.49, 0.53};
        return XCoordinateRange;
    }
    private  double[] getLessonFourteenYCoordinateRange(){
        double[] YCoordinateRange = {0.595, 0.62};
        return YCoordinateRange;
    }
    private  double[] getLessonFifteenXCoordinateRange(){
        double[] XCoordinateRange = {0.68, 0.705};
        return XCoordinateRange;
    }
    private  double[] getLessonFifteenYCoordinateRange(){
        double[] YCoordinateRange = {0.525, 0.54};
        return YCoordinateRange;
    }
    private  double[] getLessonSixteenXCoordinateRange(){
        double[] XCoordinateRange = {0.75, 0.791};
        return XCoordinateRange;
    }
    private  double[] getLessonSixteenYCoordinateRange(){
        double[] YCoordinateRange = {0.48, 0.49};
        return YCoordinateRange;
    }
    private  double[] getChallengeOneXCoordinateRange(){
        double[] XCoordinateRange = {0.63, 0.745};
        return XCoordinateRange;
    }
    private  double[] getChallengeOneYCoordinateRange(){
        double[] XCoordinateRange = {0.295, 0.395};
        return XCoordinateRange;
    }
    private  double[] getChallengeTwoXCoordinateRange(){
        double[] XCoordinateRange = {0.445, 0.465};
        return XCoordinateRange;
    }
    private  double[] getChallengeTwoYCoordinateRange(){
        double[] XCoordinateRange = {0.31, 0.33};
        return XCoordinateRange;
    }
    private  double[] getChallengeThreeXCoordinateRange(){
        double[] XCoordinateRange = {0.295, 0.32};
        return XCoordinateRange;
    }
    private  double[] getChallengeThreeYCoordinateRange(){
        double[] XCoordinateRange = {0.39, 0.405};
        return XCoordinateRange;
    }
    private  double[] getChallengeFourXCoordinateRange(){
        double[] XCoordinateRange = {0.51, 0.545};
        return XCoordinateRange;
    }
    private  double[] getChallengeFourYCoordinateRange(){
        double[] XCoordinateRange = {0.365, 0.385};
        return XCoordinateRange;
    }   private  double[] getChallengeFiveXCoordinateRange(){
        double[] XCoordinateRange = {0.635, 0.65};
        return XCoordinateRange;
    }
    private  double[] getChallengeFiveYCoordinateRange(){
        double[] XCoordinateRange = {0.415, 0.44};
        return XCoordinateRange;
    }   private  double[] getChallengeSixXCoordinateRange(){
        double[] XCoordinateRange = {0.45, 0.471};
        return XCoordinateRange;
    }
    private  double[] getChallengeSixYCoordinateRange(){
        double[] XCoordinateRange = {0.5, 0.52};
        return XCoordinateRange;
    }
    private  double[] getChallengeSevenXCoordinateRange(){
        double[] XCoordinateRange = {0.58, 0.6};
        return XCoordinateRange;
    }
    private  double[] getChallengeSevenYCoordinateRange(){
        double[] XCoordinateRange = {0.54, 0.55};
        return XCoordinateRange;
    }
    private  double[] getChallengeEightXCoordinateRange(){
        double[] XCoordinateRange = {0.865, 0.89};
        return XCoordinateRange;
    }
    private  double[] getChallengeEightYCoordinateRange(){
        double[] XCoordinateRange = {0.44, 0.465};
        return XCoordinateRange;
    }


    private void bindings() {

        map_recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                motionEvent = event;
                Log.d(TAG, String.valueOf(motionEvent.getX()/X)+ ":"+ motionEvent.getY()/Y);
                if (lessonBtnClicked()){
                    moveToLessonsDisplayActivity();
                }else if(challengeClicked()){
                    Log.d(TAG, "challengeClicked");
                    sessionManagement.saveCurrLevel(level);
                    sessionManagement.saveCurrChallenge(challenge);
                    moveToChallengesActivity();
                }
                return false;
            }
        });


    }

    private void moveToChallengesActivity() {
        Intent intent = new Intent(getActivity(), ChallengesActivity.class);
        intent.putExtra("challenge", challenge);
        intent.putExtra("level", level);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }

    private boolean challengeClicked() {
        return c1CoordinatesAreRight() || c2CoordinatesAreRight() || c3CoordinatesAreRight()|| c4CoordinatesAreRight()|| c5CoordinatesAreRight()||
                c6CoordinatesAreRight() || c7CoordinatesAreRight() || c8CoordinatesAreRight();
    }

    private boolean c8CoordinatesAreRight() {
        challenge = 8;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeEightXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeEightXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeEightYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeEightYCoordinateRange()[1]);

    }

    private boolean c7CoordinatesAreRight() {
        challenge = 7;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeSevenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeSevenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeSevenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeSevenYCoordinateRange()[1]);

    }

    private boolean c6CoordinatesAreRight() {
        challenge = 6;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeSixXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeSixXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeSixYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeSixYCoordinateRange()[1]);

    }

    private boolean c5CoordinatesAreRight() {
        challenge = 5;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeFiveXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeFiveXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeFiveYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeFiveYCoordinateRange()[1]);

    }

    private boolean c4CoordinatesAreRight() {
        challenge = 4;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeFourXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeFourXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeFourYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeFourYCoordinateRange()[1]);

    }

    private boolean c3CoordinatesAreRight() {
        challenge = 3;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeThreeXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeThreeXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeThreeYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeThreeYCoordinateRange()[1]);

    }

    private boolean c2CoordinatesAreRight() {
        challenge = 2;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeTwoXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeTwoXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeTwoYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeTwoYCoordinateRange()[1]);

    }

    private boolean c1CoordinatesAreRight() {
        challenge = 1;
        level = 1;
        return (standardizedEventXCoordinates() >= getChallengeOneXCoordinateRange()[0] && standardizedEventXCoordinates() <= getChallengeOneXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getChallengeOneYCoordinateRange()[0] && standardizedEventYCoordinates() <= getChallengeOneYCoordinateRange()[1]);

    }


    private boolean lessonBtnClicked() {
        return l1CoordinatesAreRight() || l2CoordinatesAreRight() || l3CoordinatesAreRight() || l4CoordinatesAreRight() || l5CoordinatesAreRight() || l6CoordinatesAreRight() || l7CoordinatesAreRight()
                || l8CoordinatesAreRight() || l9CoordinatesAreRight() || l10CoordinatesAreRight() || l11CoordinatesAreRight() || l12CoordinatesAreRight() || l13CoordinatesAreRight() || l4CoordinatesAreRight() || l15CoordinatesAreRight() ;
    }

    private boolean l1CoordinatesAreRight() {
        clickedLesson = 1;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonOneXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonOneXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonOneYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonOneYCoordinateRange()[1]);
    }
    private boolean l2CoordinatesAreRight() {
        clickedLesson = 2;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonTwoXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonTwoXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonTwoYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonTwoYCoordinateRange()[1]);
    }
    private boolean l3CoordinatesAreRight() {
        clickedLesson = 3;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonThreeXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonThreeXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonThreeYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonThreeYCoordinateRange()[1]);
    }  private boolean l4CoordinatesAreRight() {
        clickedLesson = 4;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonFourXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonFourXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonFourYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonFourYCoordinateRange()[1]);
    }
    private boolean l5CoordinatesAreRight() {
        clickedLesson = 5;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonFiveXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonFiveXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonFiveYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonFiveYCoordinateRange()[1]);
    }
    private boolean l6CoordinatesAreRight() {
        clickedLesson = 6;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonSixXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonSixXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonSixYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonSixYCoordinateRange()[1]);
    }
    private boolean l7CoordinatesAreRight() {
        clickedLesson = 7;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonSevenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonSevenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonSevenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonSevenYCoordinateRange()[1]);
    }
    private boolean l8CoordinatesAreRight() {
        clickedLesson = 8;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonEightXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonEightXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonEightYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonEightYCoordinateRange()[1]);
    }
    private boolean l9CoordinatesAreRight() {
        clickedLesson = 9;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonNineXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonNineXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonNineYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonNineYCoordinateRange()[1]);
    }
    private boolean l10CoordinatesAreRight() {
        clickedLesson = 10;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonTenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonTenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonTenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonTenYCoordinateRange()[1]);
    }
    private boolean l11CoordinatesAreRight() {
        clickedLesson = 11;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonElevenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonElevenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonElevenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonElevenYCoordinateRange()[1]);
    }
    private boolean l12CoordinatesAreRight() {
        clickedLesson = 12;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonTwelveXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonTwelveXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonTwelveYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonTwelveYCoordinateRange()[1]);
    }
    private boolean l13CoordinatesAreRight() {
        clickedLesson = 13;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonThirteenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonThirteenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonThirteenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonThirteenYCoordinateRange()[1]);
    }
    private boolean l14CoordinatesAreRight() {
        clickedLesson = 14;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonFourteenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonFourteenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonFourteenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonFourteenYCoordinateRange()[1]);
    }
    private boolean l15CoordinatesAreRight() {
        clickedLesson = 15;
        level = 1;
        return (standardizedEventXCoordinates() >= getLessonFifteenXCoordinateRange()[0] && standardizedEventXCoordinates() <= getLessonFifteenXCoordinateRange()[1]) && (standardizedEventYCoordinates() >= getLessonFifteenYCoordinateRange()[0] && standardizedEventYCoordinates() <= getLessonFifteenYCoordinateRange()[1]);
    }



    private double standardizedEventXCoordinates() {
        return  motionEvent.getX()  / X;

    }
    private double standardizedEventYCoordinates() {
        return  motionEvent.getY() / Y;

    }
    public void setUpAvatar(){
        int skinToneId = getResources().getIdentifier(authModel1.getSkinTone(), "drawable", getContext().getPackageName());
        Drawable skinToneDrawable = getResources().getDrawable(skinToneId);
        int hairId = getResources().getIdentifier(authModel1.getHair(), "drawable", getContext().getPackageName());
        Drawable hairDrawable = getResources().getDrawable(hairId);
        // Get the Bitmaps from the ImageViews
        hairDrawable.setBounds(0, 0, 200, 200);
        skinToneDrawable.setBounds(0, 0, 200, 200);

// Create the combined Bitmap and Canvas
        int width = 200;
        int height = 200;
        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);
        hairDrawable.draw(canvas);
        skinToneDrawable.draw(canvas);
        Bitmap resultBitmap = Bitmap.createBitmap(combinedBitmap, 50, 50, 200, 200, null, true);

        avatar.setImageBitmap(resultBitmap);
// Use the combined Bitmap as needed

    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}