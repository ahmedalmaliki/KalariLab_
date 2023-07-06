package com.example.kalarilab.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.ViewModels.AuthViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClothesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClothesFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton c1Btn, c2Btn, c3Btn, c4Btn;
    private String gender;
    private SessionManagement sessionManagement;
    private List<ImageButton> imageButtons;
    private ImageView base;
    private String pickedClothes;
    private AuthViewModel authViewModel;
    private AuthModel authModel1 = new AuthModel();
    private final static String TAG = "ClothesFragmentDebug";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClothesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clothingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClothesFragment newInstance(String param1, String param2) {
        ClothesFragment fragment = new ClothesFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clothes, container, false);
        initHooks(view);
        observeData();
        bindings();
        setGender();
        return view;
    }


    private void observeData() {
        authViewModel.getmAuthModel().observe(getActivity(), new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
            }
        });
    }

    private void bindings() {
        c1Btn.setOnClickListener(this);
        c2Btn.setOnClickListener(this);
        c3Btn.setOnClickListener(this);
        c4Btn.setOnClickListener(this);
    }

    private void initHooks(View view) {
        sessionManagement = new SessionManagement(getActivity());
        c1Btn = (ImageButton) view.findViewById(R.id.c1);
        c2Btn = (ImageButton) view.findViewById(R.id.c2);
        c3Btn = (ImageButton) view.findViewById(R.id.c3);
        c4Btn = (ImageButton) view.findViewById(R.id.c4);
        base =  getActivity().findViewById(R.id.base);
        imageButtons = Arrays.asList(c1Btn, c2Btn, c3Btn, c4Btn);
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(getActivity());
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }
    }

    private void setGender() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            gender = bundle.getString("gender", authModel1.getGender());
            if(!gender.equals("F")){
                c1Btn.setImageResource(R.drawable.mc1);
                c2Btn.setImageResource(R.drawable.mc2);
                c3Btn.setImageResource(R.drawable.mc3);
                c4Btn.setImageResource(R.drawable.mc4);
            }
            else {
                c1Btn.setImageResource(R.drawable.fc1);
                c2Btn.setImageResource(R.drawable.fc2);
                c3Btn.setImageResource(R.drawable.fc3);
                c4Btn.setImageResource(R.drawable.fc4);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.c1:
                setBorders(c1Btn);
                sessionManagement.save_clothes("c1");
                pickedClothes = "c1";
                pickSkinTone();
                Log.d(TAG, "c1");
                break;
            case R.id.c2:
                setBorders(c2Btn);
                sessionManagement.save_clothes("c2");
                pickedClothes = "c2";
                pickSkinTone();
                Log.d(TAG, "c2");

                break;
            case R.id.c3:
                setBorders(c3Btn);
                sessionManagement.save_clothes("c3");
                pickedClothes = "c3";
                pickSkinTone();
                Log.d(TAG, "c3");
                break;
            case R.id.c4:
                setBorders(c4Btn);
                sessionManagement.save_clothes("c4");
                pickedClothes = "c4";
                pickSkinTone();
                Log.d(TAG, "c4");
                break;

        }

    }

    private void pickSkinTone() {
        switch (sessionManagement.return_skin_tone()){
            case "b":
                if(!gender.equals("F")){
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sbmc1);
                        sessionManagement.save_skin_tone_drawable("sbmc1");
                                
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.sbmc2);
                        sessionManagement.save_skin_tone_drawable("sbmc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sbmc3);
                        sessionManagement.save_skin_tone_drawable("sbmc3");
                    }
                    else if(Objects.equals(pickedClothes, "c4")){
                        base.setImageResource(R.drawable.sbmc4);
                        sessionManagement.save_skin_tone_drawable("sbmc4");
                    }

                }else{
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sbfc1);
                        sessionManagement.save_skin_tone_drawable("sbfc1");

                    }
                    else if (Objects.equals(pickedClothes, "c2")) {
                        base.setImageResource(R.drawable.sbfc2);
                        sessionManagement.save_skin_tone_drawable("sbfc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sbfc3);
                        sessionManagement.save_skin_tone_drawable("sbfc3");
                    }
                    else if(Objects.equals(pickedClothes, "c4")){
                        base.setImageResource(R.drawable.sbfc4);
                        sessionManagement.save_skin_tone_drawable("sbfc4");
                    }
                }
            break;
            case "w":
                if(!gender.equals("F")){
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.swmc1);
                        sessionManagement.save_skin_tone_drawable("swmc1");
                    }
                    else if(Objects.equals(pickedClothes, "c2")) {
                        base.setImageResource(R.drawable.swmc2);
                        sessionManagement.save_skin_tone_drawable("swmc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.swmc3);
                        sessionManagement.save_skin_tone_drawable("swmc3");
                    }else {
                        base.setImageResource(R.drawable.swmc4);
                        sessionManagement.save_skin_tone_drawable("swmc4");
                    }
                }else{
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.swfc1);
                        sessionManagement.save_skin_tone_drawable("swfc1");
                    }
                    else if(Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.swfc2);
                        sessionManagement.save_skin_tone_drawable("swfc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.swfc3);
                        sessionManagement.save_skin_tone_drawable("swfc3");
                    }else{
                        base.setImageResource(R.drawable.swfc4);
                        sessionManagement.save_skin_tone_drawable("swfc4");
                    }

                }
            break;
            case "db":
                if(!gender.equals("F")){
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sdbmc1);
                        sessionManagement.save_skin_tone_drawable("sdbmc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.sdbmc2);
                        sessionManagement.save_skin_tone_drawable("sdbmc2");

                    }
                    else if (Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sdbmc3);
                        sessionManagement.save_skin_tone_drawable("sdbmc3");
                    }else {
                        base.setImageResource(R.drawable.sdbmc4);
                        sessionManagement.save_skin_tone_drawable("sdbmc4");
                    }
                }else {
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sdbfc1);
                        sessionManagement.save_skin_tone_drawable("sdbfc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.sdbfc2);
                        sessionManagement.save_skin_tone_drawable("sdbfc2");

                    }
                    else if (Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sdbfc3);
                        sessionManagement.save_skin_tone_drawable("sdbfc3");
                    }else {
                        base.setImageResource(R.drawable.sdbfc4);
                        sessionManagement.save_skin_tone_drawable("sdbfc4");
                    }
                }
                break;
            case "br":
                if(!gender.equals("F")){
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sbrmc1);
                        sessionManagement.save_skin_tone_drawable("sbrmc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.sbrmc2);
                        sessionManagement.save_skin_tone_drawable("sbrmc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sbrmc3);
                        sessionManagement.save_skin_tone_drawable("sbrmc3");
                    }
                    else {
                        base.setImageResource(R.drawable.sbrmc4);
                        sessionManagement.save_skin_tone_drawable("sbrmc4");
                    }
                }else{
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.sbrfc1);
                        sessionManagement.save_skin_tone_drawable("sbrfc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.sbrfc2);
                        sessionManagement.save_skin_tone_drawable("sbrfc2");

                    }else if (Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.sbrfc3);
                        sessionManagement.save_skin_tone_drawable("sbrfc3");
                    }else {
                        base.setImageResource(R.drawable.sbrfc4);
                        sessionManagement.save_skin_tone_drawable("sbrfc4");
                    }
                }
                break;
            case "l":
                if(!gender.equals("F")){
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.slmc1);
                        sessionManagement.save_skin_tone_drawable("slmc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.slmc2);
                        sessionManagement.save_skin_tone_drawable("slmc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.slmc3);
                        sessionManagement.save_skin_tone_drawable("slmc3");
                    }else {
                        base.setImageResource(R.drawable.slmc4);
                        sessionManagement.save_skin_tone_drawable("slmc4");
                    }
                }else {
                    if(Objects.equals(pickedClothes, "c1")){
                        base.setImageResource(R.drawable.slfc1);
                        sessionManagement.save_skin_tone_drawable("slfc1");
                    }
                    else if (Objects.equals(pickedClothes, "c2")){
                        base.setImageResource(R.drawable.slfc2);
                        sessionManagement.save_skin_tone_drawable("slfc2");

                    }
                    else if(Objects.equals(pickedClothes, "c3")){
                        base.setImageResource(R.drawable.slfc3);
                        sessionManagement.save_skin_tone_drawable("slfc3");
                    }
                    else {
                        base.setImageResource(R.drawable.slfc4);
                        sessionManagement.save_skin_tone_drawable("slfc4");
                    }
                }
                break;
        }
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
}