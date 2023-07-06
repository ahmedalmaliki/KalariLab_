package com.example.kalarilab.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.example.kalarilab.AvatarInfo;
import com.example.kalarilab.Fragments.ClothesFragment;
import com.example.kalarilab.Fragments.hairFragmentFemale;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Fragments.eyesFragment;
import com.example.kalarilab.Fragments.hairFragmentMale;
import com.example.kalarilab.Fragments.skinToneFragment;
import com.example.kalarilab.ViewModels.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;

public class AvatarSelectionActivity extends BaseActivity implements View.OnClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageButton toMaleBtn, toFemaleBtn, hairMenuBtn, clothesMenuBtn, skinToneMenuBtn;
    private FrameLayout selectionMenuFrameLayout;
    public static String gender = "M" ;//setting initial gender as Male;
    public ImageView base, hair;
    public String currentFragment;
    private SessionManagement sessionManagement;
    private Button continueBtn;
    private AuthViewModel authViewModel;
    private AuthModel authModel1 ;
    private String prev_activity = "";
    private final static String TAG = "AvatarSelectionActivityDebug";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_selection);
        Slidr.attach(this);
        initHooks();
        observeData();
        defaultStart();
        checkThePreviousActivity();
        bindings();



    }

    private void observeData() {
        Log.d(TAG, "1" );

        authViewModel.getmAuthModel().observe(this, new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                if(authModel1 == null) {
                    authModel1 = authModel;

                    setUpStarterAvatar();
                }

            }
        });
    }

    private void checkThePreviousActivity() {

        Bundle bundle = getIntent().getExtras();
       if(bundle == null) {
           return;
       }

        else {
            continueBtn.setVisibility(View.VISIBLE);
            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Objects.equals(bundle.getString("key"), "registrationWithEmail")) {
                        moveToMainActivity();

                    }else {
                        moveToMainActivity();
                    }
                }
            });
        }
    }

    private void moveToSignInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    private void defaultStart() {

        setCurrentFragment("S");
        pickGenderBtn();
        pickSkinToneMenu();
    }



    private void setUpStarterAvatar() {
//
//        if (authModel1.getSkinTone() == 0 && authModel1.getHair() == 0){
//            switch (authModel1.getGender()){
//                case "O":
//
//                    base.setImageResource(R.drawable.sbmc1);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbmc1);
//                    hair.setImageResource(R.drawable.mh1);
//                    sessionManagement.save_hair(R.drawable.mh1);
//                    break;
//                case "M":
//
//                    base.setImageResource(R.drawable.sbmc1);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbmc1);
//                    hair.setImageResource(R.drawable.mh1);
//                    sessionManagement.save_hair(R.drawable.mh1);
//                    break;
//                case "F":
//                    base.setImageResource(R.drawable.sbf);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbf);
//                    hair.setImageResource(R.drawable.fh1);
//                    sessionManagement.save_hair(R.drawable.fh1);
//                    break;
//
//
//            }
//        }else if(authModel1.getSkinTone() == 0 ){
//            switch (authModel1.getGender()){
//                case "O":
//                    base.setImageResource(R.drawable.sbmc1);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbmc1);
//                    hair.setImageResource(sessionManagement.return_hair());
//                case "M":
//                    base.setImageResource(R.drawable.sbmc1);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbmc1);
//                    hair.setImageResource(sessionManagement.return_hair());
//                    break;
//                case "F":
//                    base.setImageResource(R.drawable.sbf);
//                    sessionManagement.save_skin_tone_drawable(R.drawable.sbf);
//                    hair.setImageResource(sessionManagement.return_hair());
//
//
//
//            }}
//        else if(authModel1.getHair() == 0){
//            switch (authModel1.getGender()){
//                case "O":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(R.drawable.mh1);
//                    sessionManagement.save_hair(R.drawable.mh1);
//
//                case "M":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(R.drawable.mh1);
//                    sessionManagement.save_hair(R.drawable.mh1);
//                    break;
//                case "F":
//                    base.setImageResource(sessionManagement.return_skin_tone_drawable());
//                    hair.setImageResource(R.drawable.fh1);
//                    sessionManagement.save_hair(R.drawable.fh1);
//
//
//            }
//        }
//        else {
        int skinToneId = getResources().getIdentifier(authModel1.getSkinTone(), "drawable", getPackageName());
        Drawable skinToneDrawable = getResources().getDrawable(skinToneId);
            base.setImageDrawable(skinToneDrawable);
        int hairId = getResources().getIdentifier(authModel1.getHair(), "drawable", getPackageName());
        Drawable hairDrawable = getResources().getDrawable(hairId);
            hair.setImageDrawable(hairDrawable);



    }



    private void setGender(String g) {
        gender = g;
    }
    private String getGender(){
        return gender;
    }

    private void bindings() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.back:
                        moveToMainActivity();
                        break;
                }

                return false;
            }
        });
        toMaleBtn.setOnClickListener(this);
        toFemaleBtn.setOnClickListener(this);
        hairMenuBtn.setOnClickListener(this);
        clothesMenuBtn.setOnClickListener(this);
        skinToneMenuBtn.setOnClickListener(this);
    }

    private void initHooks() {
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(this);
        try {
            authViewModel.init();

        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        toolbar = findViewById(R.id.topAppBar);
        toMaleBtn = findViewById(R.id.toMale);
        toFemaleBtn = findViewById(R.id.toFemale);
        hairMenuBtn = findViewById(R.id.hairType);
        skinToneMenuBtn = findViewById(R.id.skinTone);
        selectionMenuFrameLayout = findViewById(R.id.selectionMenu);
        base = findViewById(R.id.base);
        hair = findViewById(R.id.hair);
        sessionManagement = new SessionManagement(this);
        continueBtn = findViewById(R.id.continueBtn);
        clothesMenuBtn = findViewById(R.id.clothes);

    }
    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("frgToLoad", "PROFILE_FRAGMENT");
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toMale:
                pickToMale();
                break;
            case R.id.toFemale:
                pickToFemale();
                break;
            case R.id.hairType:
              pickHairMenu();
                break;

            case R.id.skinTone:
                pickSkinToneMenu();
                break;
            case R.id.clothes:
                pickClothesMenu();
                break;

        }

    }

    private void pickClothesMenu() {
        clothesMenuBtn.setImageResource(R.drawable.clothes_colored);
        skinToneMenuBtn.setImageResource(R.drawable.circle_small);
        hairMenuBtn.setImageResource(R.drawable.hair);
        setCurrentFragment("C");

        runFragment("C");
    }

    private void pickToMale() {
        toFemaleBtn.setImageResource(R.drawable.female_grey);
        toMaleBtn.setImageResource(R.drawable.male_color);
        changeBaseAvatar('m');
        setGender("M");
        rerunCurrentFragment();

    }



    private void pickGenderBtn() {
        toFemaleBtn.setImageResource(R.drawable.female_grey);
        toMaleBtn.setImageResource(R.drawable.male_color);
        setGender("M");


        rerunCurrentFragment();
    }

    private void rerunCurrentFragment() {
        Log.d("what is the actual fuck", getCurrentFragment());
       runFragment(getCurrentFragment());
    }

    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }
    private void pickToFemale() {
        toMaleBtn.setImageResource(R.drawable.male_grey);
        toFemaleBtn.setImageResource(R.drawable.female_color);
        changeBaseAvatar('f');
        setGender("F");
        rerunCurrentFragment();
    }

    private void changeBaseAvatar(char g) {
        if(g == 'f'){
            sessionManagement.save_skin_tone_drawable("sbfc1");
            base.setImageResource(R.drawable.sbfc1);
        }else {
            sessionManagement.save_skin_tone_drawable("sbmc1");
            base.setImageResource(R.drawable.sbmc1);
        }

    }


    private void pickHairMenu() {
        clothesMenuBtn.setImageResource(R.drawable.clothes);
        skinToneMenuBtn.setImageResource(R.drawable.circle_small);
        hairMenuBtn.setImageResource(R.drawable.hair_color);
        setCurrentFragment("H");
        runFragment("H");

    }



    private void pickSkinToneMenu() {
        clothesMenuBtn.setImageResource(R.drawable.clothes);
        skinToneMenuBtn.setImageResource(R.drawable.circle_small_color);
        hairMenuBtn.setImageResource(R.drawable.hair);
        setCurrentFragment("S");
        runFragment("S");

    }

    private void runFragment(String fragmentIntended) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("gender", getGender());

        switch (fragmentIntended){
            case "H":

                if (gender.equals("M")) {
                    hairFragmentMale hairFragmentMen = new hairFragmentMale();
                    hairFragmentMen.setArguments(bundle);
                    transaction.replace(R.id.selectionMenu, hairFragmentMen);
                }else {
                    hairFragmentFemale hairFragmentFemale = new hairFragmentFemale();
                    hairFragmentFemale.setArguments(bundle);
                    transaction.replace(R.id.selectionMenu, hairFragmentFemale);
               }
                break;
            case "E":
                eyesFragment eyesFragment = new eyesFragment();
                eyesFragment.setArguments(bundle);
                transaction.replace(R.id.selectionMenu, eyesFragment);
                break;
            case "S":
                skinToneFragment skinToneFragment = new skinToneFragment();
                skinToneFragment.setArguments(bundle);
                transaction.replace(R.id.selectionMenu,skinToneFragment);
                break;
            case "C":
                ClothesFragment clothesFragment = new ClothesFragment();
                clothesFragment.setArguments(bundle);
                transaction.replace(R.id.selectionMenu,clothesFragment);
                Log.d("what is the actual fuck", ";");
                break;

        }
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uploadChosenAvatar();
        if(Objects.equals(prev_activity, "")){
            finish();
        }else {
       moveToMainActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        uploadChosenAvatar();
    }

    private void uploadChosenAvatar() {


        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("skinTone").setValue(sessionManagement.return_skin_tone_drawable());

        if (!Objects.equals(sessionManagement.return_hair(), "")) {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).child("hair").setValue(sessionManagement.return_hair());

        }else {
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).child("hair").setValue("mh1");

        }
    }


}