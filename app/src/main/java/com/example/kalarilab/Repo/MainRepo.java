package com.example.kalarilab.Repo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.kalarilab.Post;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.db.AdminDao;
import com.example.kalarilab.db.AdminEntry;
import com.example.kalarilab.db.AppDataBase;
import com.example.kalarilab.db.AuthDao;
import com.example.kalarilab.db.AuthEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainRepo {
    private static MainRepo instance;
    private final static String TAG = "RepoDebug";
    private RepoCallBack repoCallBack;
    private AdminDao mAdminDao;
    private LiveData<List<AdminEntry>> adminsList;
    private AuthDao mAuthDao;
    private LiveData<AuthEntry> authData ;
    private   AppDataBase db;
    private Context context;




    public  static MainRepo getInstance(Context context){

        if (instance == null) {
            instance = new MainRepo();
            instance.context = context;


        }

        return instance;
    }
    private void passAdminsList() throws JSONException {
        repoCallBack.getAdminsList(adminsList); //after that it passes history data from db to the viewmodel through the callback interface

    }

    public void setRepoCallback(RepoCallBack repoCallBack) {
        this.repoCallBack = repoCallBack;
    }

    public void getPostsDataFromAPI() {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                 List<String> usersIDs = new ArrayList<>();
                 List<String>  uris = new ArrayList<>();
                 List<String> fullNames = new ArrayList<>();
                 List<String> challenges = new ArrayList<>();
                List<String> levels = new ArrayList<>();
                List<String> tokens = new ArrayList<>();
                FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            for(DataSnapshot ds: snapshot.getChildren()) {
                                Post post = ds.getValue(Post.class);
                                assert post != null;
                                challenges.add(post.getChallenge());
                                levels.add(post.getLevel());
                                fullNames.add(post.getFullName());
                                usersIDs.add(post.getUserId());
                                uris.add(post.getUri());
                                tokens.add(post.getToken());





                            }
                            repoCallBack.getPanelLiveData(fullNames, challenges, levels,uris, usersIDs, tokens);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }
    public void getAdminsListFromRepo() throws JSONException {
        try {
            getAdminsListFromDB();
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }


        if (isConnectedToInternet(context)) getAdminsListFromServer();
        else setAlertDialog((Activity) context, "Please check your internet connection!");
    }
    public void getAuthDataFromRepo() throws JSONException{
        try {
            getAuthDataFromDB();

        }catch (Exception e)
       {
        }
        if(isConnectedToInternet(context)) gatAuthDataFromServer();
        else setAlertDialog((Activity) context, "Please check your internet connection!");


    }

    private void getAuthDataFromDB() {
        initDBForAuthData();
        passAuthData();
    }

   public void passAuthData()
   {


       repoCallBack.getAuthData(authData);
    }

    private void gatAuthDataFromServer() {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        initDBForAuthData();
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        AuthEntry latestAuthEntry = new AuthEntry(authModel.getFullName(), authModel.getEmail(),
                                authModel.getPassword(), authModel.getBio(), authModel.getGender(),
                                authModel.getBirthdate(), authModel.getLevelReached(), authModel.getLessonReached(),
                                authModel.getPoints(), authModel.getWeeklyPoints(), authModel.getAwardedPoints(),
                                authModel.getAwardedPostures(), authModel.getSkinTone(),authModel.getHair() );
                        updateAuthData(latestAuthEntry);

                        insertAuthData(latestAuthEntry);




                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }




    private void getAdminsListFromDB() throws JSONException {
        initDBForAdminPanel();
        passAdminsList();

    }
    private void initDBForAdminPanel() {
        db = AppDataBase.getInstance(context);
        mAdminDao = db.AdminDao();
        adminsList = mAdminDao.getAdminsList();
    }
    private void initDBForAuthData() {
        db = AppDataBase.getInstance(context);
        mAuthDao = db.AuthDao();
        authData = mAuthDao.getAuthData();


    }
    public void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                       repoCallBack.getToken(token);
                    }
                });
    }


    private void getAdminsListFromServer() {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseDatabase.getInstance().getReference("Admins").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        try {
                            initDBForAdminPanel();
                            for(DataSnapshot ds: snapshot.getChildren()) {
                               populateAdminsDao( ds.getValue().toString());

                            }

                            passAdminsList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }
    private void populateAdminsDao(String email) throws JSONException {

            insertAdminList(new AdminEntry(email) );




    }
    public void insertAuthData(AuthEntry authEntry){
        new InsertAuthDataAsyncTask(mAuthDao, this).execute(authEntry);
    }
    public void insertAdminList(AdminEntry adminEntry){
        new InsertAdminListAsyncTask(mAdminDao).execute(adminEntry);
    }
    public void  deleteAdminsList (){
        new DeleteAdminsListAsyncTask(mAdminDao).execute();
    }
    private void deleteAuthData() {
        new DeleteAuthDataAsyncTask(mAuthDao).execute();
    }
    private void updateAuthData(AuthEntry authEntry){
        new UpdateAuthDataAsyncTask(mAuthDao, this).execute(authEntry);
    }
    public static boolean isConnectedToInternet(Context context)
    {
        // Check intenet connectivity
        boolean connected = false;
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            connected = (   conMgr.getActiveNetworkInfo() != null &&
                    conMgr.getActiveNetworkInfo().isAvailable() &&
                    conMgr.getActiveNetworkInfo().isConnected()   );
        }catch (Exception e)
        {
            return false;
        }

        return connected;

    }


    public interface RepoCallBack {


        void getPanelLiveData(List<String> fullNames, List<String> challenges, List<String> levels, List<String> uris, List<String> usersIds, List<String> tokens) throws JSONException;

        void getAdminsList(LiveData<List<AdminEntry>> adminsList);
        void getAuthData(LiveData<AuthEntry> authEntryLiveData);
        void getToken(String token);

    }
    private void setAlertDialog(Activity context, String message) {
        if (!context.isFinishing() ) {
            new AlertDialog.Builder(context)
                    .setTitle("Connection Error.")
                    .setMessage(message)

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }}

    private static class InsertAdminListAsyncTask extends AsyncTask<AdminEntry, Void, Void> {
        private AdminDao adminDao;


        public InsertAdminListAsyncTask(AdminDao adminDao) {
            this.adminDao = adminDao;

        }

        @Override
        protected Void doInBackground(AdminEntry... adminEntries) {
            Log.d(TAG, adminEntries[0].getEmail());
            adminDao.insert(adminEntries[0]);
            return null;
        }


    }
    private static class DeleteAdminsListAsyncTask extends AsyncTask<Void, Void, Void> {
        private  AdminDao adminDao;

        public DeleteAdminsListAsyncTask(AdminDao adminDao) {
            this.adminDao = adminDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            adminDao.deleteAdminsList();
            return null;
        }
    }

    private static class DeleteAuthDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private  AuthDao authDao;

        public DeleteAuthDataAsyncTask(AuthDao authDao) {
            this.authDao = authDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            authDao.deleteAuthData();
            return null;
        }
    }
    private static class InsertAuthDataAsyncTask extends AsyncTask<AuthEntry, Void, Void> {
        private AuthDao authDao;
        private MainRepo mainRepo;


        public InsertAuthDataAsyncTask(AuthDao authDao, MainRepo mainRepo) {
            this.authDao = authDao;
            this.mainRepo = mainRepo;

        }

        @Override
        protected Void doInBackground(AuthEntry... authEntry) {
            
           authDao.insert(authEntry[0]);



            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            mainRepo.passAuthData();

        }
    }
    private static class UpdateAuthDataAsyncTask extends AsyncTask<AuthEntry, Void, Void> {
        private  AuthDao authDao;
        private  MainRepo mainRepo;

        public UpdateAuthDataAsyncTask(AuthDao authDao, MainRepo mainRepo) {
            this.authDao = authDao;
            this.mainRepo = mainRepo;
        }

        @Override
        protected Void doInBackground(AuthEntry... authEntries) {
            authDao.update(authEntries[0]);


            return null;
        }
    }
}
