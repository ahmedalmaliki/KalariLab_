package com.example.kalarilab.ViewModels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.kalarilab.Models.AdminsModel;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.Repo.MainRepo;
import com.example.kalarilab.db.AdminEntry;
import com.example.kalarilab.db.AuthEntry;
import com.google.android.exoplayer2.util.Log;

import org.json.JSONException;

import java.util.List;

public class AuthViewModel extends ViewModel implements MainRepo.RepoCallBack{

    private MutableLiveData<AuthModel> mAuthModel;
    private Activity activity;
    private final static String TAG = "AuthViewModelDebug";


    public void init() throws JSONException {
        mAuthModel = new MutableLiveData<>();
        MainRepo mainRepo = MainRepo.getInstance(activity); //getting a singleton instance of the repository.
        mainRepo.setRepoCallback(this); //setting the repository callback interface
        mainRepo.getAuthDataFromRepo();
    }

    public MutableLiveData<AuthModel> getmAuthModel() {
        return mAuthModel;
    }
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }





    @Override
    public void getPanelLiveData(List<String> fullNames, List<String> challenges, List<String> levels, List<String> uris, List<String> usersIds, List<String> tokens) throws JSONException {

    }

    @Override
    public void getAdminsList(LiveData<List<AdminEntry>> adminsList) {

    }

    @Override
    public void getAuthData(LiveData<AuthEntry> authEntryLiveData) {
        authEntryLiveData.observe((LifecycleOwner) activity, new Observer<AuthEntry>() {
            @Override
            public void onChanged(AuthEntry authEntry) {
                try {
                    mAuthModel.setValue(new AuthModel(authEntry.getFullName(), authEntry.getEmail(),
                            authEntry.getPassword(), authEntry.getBio(), authEntry.getGender(), authEntry.getBirthdate(),
                            authEntry.getLevelReached(), authEntry.getLessonReached(), authEntry.getPoints(), authEntry.getWeeklyPoints(),
                            authEntry.getAwardedPoints(), authEntry.getAwardedPostures(), authEntry.getSkinTone(), authEntry.getHair()));
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                }

            }
        });
    }

    @Override
    public void getToken(String token) {

    }
}
