package com.example.kalarilab.Fragments;

import android.os.Bundle;
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
 * Use the {@link hairFragmentMale#newInstance} factory method to
 * create an instance of this fragment.
 */
public class hairFragmentMale extends Fragment  implements View.OnClickListener {
    SessionManagement sessionManagement;
    List<ImageButton> imageButtons;



    ImageButton mh1, mh2, mh3, mh4, mh5, mh6, mh7, mh8, mh9, mh10, mh11, mh12,
            mh13, mh14, mh15, mh16, mh17, mh18,mh19, mh20, mh21, mh22;
    ImageView hair;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public hairFragmentMale() {
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
    public static hairFragmentMale newInstance(String param1, String param2) {
        hairFragmentMale fragment = new hairFragmentMale();
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
        View view =  inflater.inflate(R.layout.fragment_hair_male, container, false);
        initHooks(view);
        bindings();

        return view;
    }



    private void bindings() {
        mh1.setOnClickListener(this);
        mh2.setOnClickListener(this);
        mh3.setOnClickListener(this);
        mh4.setOnClickListener(this);
        mh5.setOnClickListener(this);
        mh6.setOnClickListener(this);
        mh7.setOnClickListener(this);
        mh8.setOnClickListener(this);
        mh9.setOnClickListener(this);
        mh10.setOnClickListener(this);
        mh11.setOnClickListener(this);
        mh12.setOnClickListener(this);
        mh13.setOnClickListener(this);
        mh14.setOnClickListener(this);
        mh15.setOnClickListener(this);
        mh16.setOnClickListener(this);
        mh17.setOnClickListener(this);
        mh18.setOnClickListener(this);
        mh19.setOnClickListener(this);
        mh20.setOnClickListener(this);
        mh21.setOnClickListener(this);
        mh22.setOnClickListener(this);



    }
    private void initHooks(View view) {
       mh1 = (ImageButton) view.findViewById(R.id.mh1);
        mh2 = (ImageButton) view.findViewById(R.id.mh2);
        mh3 = (ImageButton) view.findViewById(R.id.mh3);
        mh4 = (ImageButton) view.findViewById(R.id.mh4);
        mh5 = (ImageButton) view.findViewById(R.id.mh5);
        mh6 =(ImageButton) view.findViewById(R.id.mh6);
        mh7 =(ImageButton) view.findViewById(R.id.mh7);
        mh8 =(ImageButton) view.findViewById(R.id.mh8);
        mh9 =(ImageButton) view.findViewById(R.id.mh9);
        mh10 =(ImageButton) view.findViewById(R.id.mh10);
        mh11 =(ImageButton) view.findViewById(R.id.mh11);
        mh12 =(ImageButton) view.findViewById(R.id.mh12);
        mh13 =(ImageButton) view.findViewById(R.id.mh13);
        mh14 =(ImageButton) view.findViewById(R.id.mh14);
        mh15 =(ImageButton) view.findViewById(R.id.mh15);
        mh16 =(ImageButton) view.findViewById(R.id.mh16);
        mh17 =(ImageButton) view.findViewById(R.id.mh17);
        mh18 =(ImageButton) view.findViewById(R.id.mh18);
        mh19 =(ImageButton) view.findViewById(R.id.mh19);
        mh20 =(ImageButton) view.findViewById(R.id.mh20);
        mh21 =(ImageButton) view.findViewById(R.id.mh21);
        mh22 =(ImageButton) view.findViewById(R.id.mh22);

        sessionManagement = new SessionManagement(getActivity());

        hair =  getActivity().findViewById(R.id.hair);

        imageButtons = Arrays.asList(mh1, mh2, mh3, mh4, mh5, mh6, mh7, mh8,
                mh9, mh10, mh11, mh12, mh13, mh14,
                mh15, mh16, mh17, mh18, mh19, mh20, mh21, mh22);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mh1:
                pickmh1();
                setCurrentHair("mh1");
                changeMarginTop();
                break;
            case R.id.mh2:
                pickmh2();
                setCurrentHair("mh2");
                changeMarginTop();
                break;
            case R.id.mh3:
                pickmh3();
                setCurrentHair("mh3");
                changeMarginTop();
                break;
            case R.id.mh4:
                pickmh4();
                setCurrentHair("mh4");
                changeMarginTop();
                break;
            case R.id.mh5:
                pickmh5();
                setCurrentHair("mh5");
                changeMarginTop();
                break;
            case R.id.mh6:
                pickmh6();
                setCurrentHair("mh6");
                changeMarginTop();
                break;
            case R.id.mh7:
                pickmh7();
                setCurrentHair("mh7");
                changeMarginTop();
                break;
            case R.id.mh8:
                pickmh8();
                setCurrentHair("mh8");
                changeMarginTop();
                break;
            case R.id.mh9:
                pickmh9();
                setCurrentHair("mh9");
                changeMarginTop();
                break;
            case R.id.mh10:
                pickmh10();
                setCurrentHair("mh10");
                changeMarginTop();
                break;
            case R.id.mh11:
                pickmh11();
                setCurrentHair("mh11");
                changeMarginTop();
                break;
            case R.id.mh12:
                pickmh12();
                setCurrentHair("mh12");
                changeMarginTop();
                break;
            case R.id.mh13:
                pickmh13();
                setCurrentHair("mh13");
                changeMarginTop();
                break;
            case R.id.mh14:
                pickmh14();
                setCurrentHair("mh14");
                changeMarginTop();
                break;
            case R.id.mh15:
                pickmh15();
                setCurrentHair("mh15");
                changeMarginTop();
                break;
            case R.id.mh16:
                pickmh16();
                setCurrentHair("mh16");
                changeMarginTop();
                break;
            case R.id.mh17:
                pickmh17();
                setCurrentHair("mh17");
                changeMarginTop();
                break;
            case R.id.mh18:
                pickmh18();
                setCurrentHair("mh18");
                changeMarginTop();
                break;
            case R.id.mh19:
                pickmh19();
                setCurrentHair("mh19");
                changeMarginTop();
                break;
            case R.id.mh20:
                pickmh20();
                setCurrentHair("mh20");
                changeMarginTop();
                break;
            case R.id.mh21:
                pickmh21();
                setCurrentHair("mh21");
                changeMarginTop();
                break;
            case R.id.mh22:
                pickmh22();
                setCurrentHair("mh22");
                changeMarginTop();
                break;




        }

    }


    private void pickmh22() {
        hair.setImageResource(R.drawable.mh22);
        setBorders(mh22);
    }

    private void pickmh21() {
        hair.setImageResource(R.drawable.mh21);
        setBorders(mh21);
    }

    private void pickmh20() {
        hair.setImageResource(R.drawable.mh20);
        setBorders(mh20);
    }

    private void pickmh19() {
        hair.setImageResource(R.drawable.mh19);
        setBorders(mh19);
    }

    private void pickmh18() {
        hair.setImageResource(R.drawable.mh18);
        setBorders(mh18);
    }

    private void pickmh17() {
        hair.setImageResource(R.drawable.mh17);
        setBorders(mh17);
    }

    private void pickmh16() {
        hair.setImageResource(R.drawable.mh16);
        setBorders(mh16);
    }

    private void pickmh15() {
        hair.setImageResource(R.drawable.mh15);
        setBorders(mh15);
    }

    private void pickmh14() {
        hair.setImageResource(R.drawable.mh14);
        setBorders(mh14);
    }

    private void pickmh13() {
        hair.setImageResource(R.drawable.mh13);
        setBorders(mh13);
    }

    private void pickmh12() {
        hair.setImageResource(R.drawable.mh12);
        setBorders(mh12);
    }

    private void pickmh11() {
        hair.setImageResource(R.drawable.mh11);
        setBorders(mh11);
    }

    private void pickmh10() {
        hair.setImageResource(R.drawable.mh10);
        setBorders(mh10);
    }

    private void pickmh9() {
        hair.setImageResource(R.drawable.mh9);
        setBorders(mh9);
    }

    private void pickmh8() {
        hair.setImageResource(R.drawable.mh8);
        setBorders(mh8);
    }

    private void pickmh7() {
        hair.setImageResource(R.drawable.mh7);
        setBorders(mh7);
    }

    private void pickmh6() {
        hair.setImageResource(R.drawable.mh6);
        setBorders(mh6);

    }



    private void pickmh5() {
        setBorders(mh5);
        hair.setImageResource(R.drawable.mh5);
    }



    private void pickmh4() {
        setBorders(mh4);
        hair.setImageResource(R.drawable.mh4);
    }



    private void pickmh3() {

        setBorders(mh3);
        hair.setImageResource(R.drawable.mh3);
    }



    private void pickmh2() {
        setBorders(mh2);
        hair.setImageResource(R.drawable.mh2);
    }



    private void pickmh1() {
        setBorders(mh1);
        hair.setImageResource(R.drawable.mh1);

    }



    public void setCurrentHair(String currentHair) {
        sessionManagement.save_hair(currentHair);

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
        params.topMargin = 0; // 5 pixels
        hair.requestLayout();
        sessionManagement.save_hair_marginTop(0);
    }
}