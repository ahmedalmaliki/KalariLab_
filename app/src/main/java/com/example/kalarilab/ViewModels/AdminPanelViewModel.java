package com.example.kalarilab.ViewModels;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.kalarilab.Models.AdminsModel;
import com.example.kalarilab.Models.AdminPanelModel;
import com.example.kalarilab.Repo.MainRepo;
import com.example.kalarilab.db.AdminEntry;
import com.example.kalarilab.db.AuthEntry;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelViewModel extends ViewModel implements MainRepo.RepoCallBack {

    private MutableLiveData<AdminPanelModel> mPanelModel;
    private MutableLiveData<AdminsModel> mAdminsModel;
    private Activity activity;
    private static final String TAG = "AdminPanelViewDebug";

    public void init() throws JSONException {
        mPanelModel = new MutableLiveData<>();
        mAdminsModel = new MutableLiveData<>();
        MainRepo mainRepo = MainRepo.getInstance(activity); //getting a singleton instance of the repository.
        mainRepo.setRepoCallback(this); //setting the repository callback interface
        mainRepo.getAdminsListFromRepo();
        mainRepo.getPostsDataFromAPI(); //if there is internet connection then make request to api, else: get data from DB


    }

    public  LiveData<AdminsModel> getAdminModel(){return mAdminsModel;}

    public LiveData<AdminPanelModel> getPanelModel() {
        return mPanelModel;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }




    @Override
    public void getPanelLiveData(List<String> fullNames, List<String> challenges, List<String> levels, List<String> uris, List<String> usersIds, List<String> tokens) throws JSONException {

        mPanelModel.setValue(new AdminPanelModel(fullNames, challenges, levels,uris, usersIds, tokens));

    }

    @Override
    public void getAdminsList(LiveData<List<AdminEntry>> adminsList) {
            adminsList.observe((LifecycleOwner) activity, new Observer<List<AdminEntry>>() {
                @Override
                public void onChanged(List<AdminEntry> adminEntries) {
                    mAdminsModel.setValue(new AdminsModel(getAdminsEmails(adminEntries)));
                }
            });
    }

    @Override
    public void getAuthData(LiveData<AuthEntry> authEntryLiveData) {

    }

    @Override
    public void getToken(String token) {

    }

    private List<String> getAdminsEmails(List<AdminEntry> adminEntries) {
        ArrayList<String> emails = new ArrayList<>();
        for (int i = 0; i < adminEntries.size() ; i++ ){
            emails.add(String.valueOf(adminEntries.get(i).getEmail()));
        }
        return emails;
    }


}
