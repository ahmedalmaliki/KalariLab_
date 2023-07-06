package com.example.kalarilab.Fragments;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kalarilab.Activities.AvatarSelectionActivity;
import com.example.kalarilab.Activities.Settings;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.ViewModels.AuthViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment  {
    TextView fullName;
    TextView bio;
    SessionManagement sessionManagement ;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton settings, avatarEdit;
    ImageView base, hair;
    private AuthViewModel authViewModel;
    private AuthModel authModel1;
    private final static String TAG = "ProfileFragmentDebug";
    private View view;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        sessionManagement = new SessionManagement(getActivity());





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view =  inflater.inflate(R.layout.fragment_profile, container, false);
        initHooks(view);
        observeData();
        bindings();


        return view;
    }

    private void bindings() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSettingsActivity();
            }
        });
        avatarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAvatarActivity();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void moveToAvatarActivity() {
        Intent intent = new Intent(getActivity(), AvatarSelectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initHooks(View view) {
        fullName = (TextView) view.findViewById(R.id.name);
        bio = (TextView) view.findViewById(R.id.bio);
        settings = (ImageButton) view.findViewById(R.id.settings);
        swipeRefreshLayout  = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        avatarEdit = (ImageButton) view.findViewById(R.id.avatarEdit);
        base =  view.findViewById(R.id.base);
        hair = view.findViewById(R.id.hair);
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(getActivity());
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }

    }
    private void observeData() {
        authViewModel.getmAuthModel().observe(getActivity(), new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
                Log.d(TAG, String.valueOf(authModel1.getPoints()));
                showInfo();
                setUpAvatar();

            }
        });
    }
    private void refreshPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getParentFragmentManager().beginTransaction().detach(this).commitNow();
            getParentFragmentManager().beginTransaction().attach(this).commitNow();
        } else {
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    private void setUpAvatar() {
        changeMarginTop();
//        if (authModel1.getSkinTone() == 0 && authModel1.getHair() == 0){
//            switch (authModel1.getGender()){
//                case "O":
//                    base.setImageResource(R.drawable.sbmc1);
//                    hair.setImageResource(R.drawable.mh1);
//                case "M":
//                    base.setImageResource(R.drawable.sbmc1);
//                    hair.setImageResource(R.drawable.mh1);
//                    break;
//                case "F":
//                    base.setImageResource(R.drawable.sbf);
//                    hair.setImageResource(R.drawable.fh1);
//
//
//
//            }
//        }else if(authModel1.getSkinTone() == 0 ){
//            switch (authModel1.getGender()){
//                case "O":
//                    base.setImageResource(R.drawable.sbmc1);
//                    hair.setImageResource(sessionManagement.return_hair());
//                case "M":
//                    base.setImageResource(R.drawable.sbmc1);
//                    hair.setImageResource(sessionManagement.return_hair());
//                    break;
//                case "F":
//                    base.setImageResource(R.drawable.sbf);
//                    hair.setImageResource(sessionManagement.return_hair());
//
//
//
//            }}
//        else if(authModel1.getHair() == 0){
//            switch (authModel1.getGender()){
//                case "O":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(sessionManagement.return_hair());
//                case "M":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(sessionManagement.return_hair());
//                    break;
//                case "F":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(sessionManagement.return_hair());
//
//
//
//            }
//        }
//
//        else {
        int skinToneId = getResources().getIdentifier(authModel1.getSkinTone(), "drawable", getContext().getPackageName());
        Drawable skinToneDrawable = getResources().getDrawable(skinToneId);
        base.setImageDrawable(skinToneDrawable);
        int hairId = getResources().getIdentifier(authModel1.getHair(), "drawable", getContext().getPackageName());
        Drawable hairDrawable = getResources().getDrawable(hairId);
        hair.setImageDrawable(hairDrawable);



    }



    private void moveToSettingsActivity() {
        Intent intent = new Intent(getActivity(), Settings.class);
        startActivity(intent);

    }



    public void showInfo(){
        fullName.setText(getFullName());
        bio.setText(authModel1.getBio());



    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        observeData();

    }
    private String getFullName(){
        return authModel1.getFullName();
    }
    private void changeMarginTop() {
        //Due to the inadequacy of our graphic designer I have to work around the problem.
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) hair.getLayoutParams();
        params.topMargin = sessionManagement.return_hair_marginTop();
        hair.requestLayout();
    }
}