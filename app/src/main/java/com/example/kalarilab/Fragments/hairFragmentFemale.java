package com.example.kalarilab.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link hairFragmentFemale#newInstance} factory method to
 * create an instance of this fragment.
 */
public class hairFragmentFemale extends Fragment implements View.OnClickListener {


    int currentHair;
    SessionManagement sessionManagement;
    List<ImageButton> imageButtons;
    private final static String TAG = "HairFragmentFemaleDebug";



    ImageButton fh1, fh2, fh3, fh4, fh5, fh6, fh7, fh8, fh9, fh10, fh11, fh12,
            fh13, fh14, fh15;
    ImageView hair;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public hairFragmentFemale() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment hairFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static hairFragmentFemale newInstance(String param1, String param2) {
        hairFragmentFemale fragment = new hairFragmentFemale();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_hair_female, container, false);
        initHooks(view);
        bindings();
        return view;
    }


    private void bindings() {
        fh1.setOnClickListener(this);
        fh2.setOnClickListener(this);
        fh3.setOnClickListener(this);
        fh4.setOnClickListener(this);
        fh5.setOnClickListener(this);
        fh6.setOnClickListener(this);
        fh7.setOnClickListener(this);
        fh8.setOnClickListener(this);
        fh9.setOnClickListener(this);
        fh10.setOnClickListener(this);
        fh11.setOnClickListener(this);
        fh12.setOnClickListener(this);
        fh13.setOnClickListener(this);
        fh14.setOnClickListener(this);
        fh15.setOnClickListener(this);


    }
    private void initHooks(View view) {

        fh1 = (ImageButton) view.findViewById(R.id.fh1);
        fh2 = (ImageButton) view.findViewById(R.id.fh2);
        fh3 = (ImageButton) view.findViewById(R.id.fh3);
        fh4 = (ImageButton) view.findViewById(R.id.fh4);
        fh5 = (ImageButton) view.findViewById(R.id.fh5);
        fh6 =(ImageButton) view.findViewById(R.id.fh6);
        fh7 =(ImageButton) view.findViewById(R.id.fh7);
        fh8 =(ImageButton) view.findViewById(R.id.fh8);
        fh9 =(ImageButton) view.findViewById(R.id.fh9);
        fh10 =(ImageButton) view.findViewById(R.id.fh10);
        fh11 =(ImageButton) view.findViewById(R.id.fh11);
        fh12 =(ImageButton) view.findViewById(R.id.fh12);
        fh13 =(ImageButton) view.findViewById(R.id.fh13);
        fh14 =(ImageButton) view.findViewById(R.id.fh14);
        fh15 =(ImageButton) view.findViewById(R.id.fh15);

        hair =  getActivity().findViewById(R.id.hair);
        sessionManagement = new SessionManagement(getActivity());
        imageButtons = Arrays.asList(fh1, fh2, fh3, fh4, fh5, fh6, fh7, fh8,
                fh9, fh10, fh11, fh12, fh13, fh14,
                fh15);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fh1:
                pickfh1();
                setCurrentHair("fh1");
                changeMarginTop();
                break;
            case R.id.fh2:
                pickfh2();
                setCurrentHair("fh2");
                changeMarginTop();
                break;
            case R.id.fh3:
                pickfh3();
                setCurrentHair("fh3");
                changeMarginTop();
                break;
            case R.id.fh4:
                pickfh4();
                setCurrentHair("fh4");
                changeMarginTop();
                break;
            case R.id.fh5:
                pickfh5();
                setCurrentHair("fh5");
                changeMarginTop();
                break;
            case R.id.fh6:
                pickfh6();
                setCurrentHair("fh6");
                changeMarginTop();
                break;
            case R.id.fh7:
                pickfh7();
                setCurrentHair("fh7");
                changeMarginTop();
                break;
            case R.id.fh8:
                pickfh8();
                setCurrentHair("fh8");
                changeMarginTop();
                break;
            case R.id.fh9:
                pickfh9();
                setCurrentHair("fh9");
                changeMarginTop();
                break;
            case R.id.fh10:
                pickfh10();
                setCurrentHair("fh10");
                changeMarginTop();
                break;
            case R.id.fh11:
                pickfh11();
                setCurrentHair("fh11");
                changeMarginTop();
                break;
            case R.id.fh12:
                pickfh12();
                setCurrentHair("fh12");
                changeMarginTop();
                break;
            case R.id.fh13:
                pickfh13();
                setCurrentHair("fh13");
                changeMarginTop();
                break;
            case R.id.fh14:
                pickfh14();
                setCurrentHair("fh14");
                changeMarginTop();
                break;
            case R.id.fh15:
                pickfh15();
                setCurrentHair("fh15");
                changeMarginTop();
                break;



        }

    }


    private void pickfh15() {
        hair.setImageResource(R.drawable.fh15);
        setBorders(fh15);
    }

    private void pickfh14() {
        hair.setImageResource(R.drawable.fh14);
        setBorders(fh14);
    }

    private void pickfh13() {
        hair.setImageResource(R.drawable.fh13);
        setBorders(fh13);
    }

    private void pickfh12() {
        hair.setImageResource(R.drawable.fh12);
        setBorders(fh12);
    }

    private void pickfh11() {
        hair.setImageResource(R.drawable.fh11);
        setBorders(fh11);
    }

    private void pickfh10() {
        hair.setImageResource(R.drawable.fh10);
        setBorders(fh10);
    }

    private void pickfh9() {
        hair.setImageResource(R.drawable.fh9);
        setBorders(fh9);
    }

    private void pickfh8() {
        hair.setImageResource(R.drawable.fh8);
        setBorders(fh8);
    }

    private void pickfh7() {
        hair.setImageResource(R.drawable.fh7);
        setBorders(fh7);
    }

    private void pickfh6() {
        hair.setImageResource(R.drawable.fh6);
        setBorders(fh6);

    }



    private void pickfh5() {
        setBorders(fh5);
        hair.setImageResource(R.drawable.fh5);
    }



    private void pickfh4() {
        setBorders(fh4);
        hair.setImageResource(R.drawable.fh4);
    }



    private void pickfh3() {

        setBorders(fh3);
        hair.setImageResource(R.drawable.fh3);
    }



    private void pickfh2() {
        setBorders(fh2);
        hair.setImageResource(R.drawable.fh2);
    }



    private void pickfh1() {
        Log.d(TAG, "l");
        setBorders(fh1);
        hair.setImageResource(R.drawable.fh1);

    }



    public void setCurrentHair(String currentHair) {
        sessionManagement.save_hair(currentHair);

    }

    public int getCurrentHair() {
        return currentHair;
    }


    @Override
    public void onStop() {
        super.onStop();
    }
    private void setBorders(ImageButton btn){
        for(ImageButton button : imageButtons){
            if (button == btn){
                button.setBackground(getResources().getDrawable(R.drawable.image_button_border_color));
            }else{
                button.setBackground(getResources().getDrawable(R.drawable.image_button_border));
            }
        }
    }
    private void changeMarginTop() {
        //Due to the inadequacy of our graphic designer I have to work around the problem.
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) hair.getLayoutParams();
        params.topMargin = 7; // 5 pixels
        hair.requestLayout();
        sessionManagement.save_hair_marginTop(7);

    }
}