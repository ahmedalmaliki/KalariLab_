package com.example.kalarilab.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {AdminEntry.class, AuthEntry.class}, version = 1)
public  abstract class AppDataBase extends RoomDatabase {
    public  abstract AdminDao AdminDao();
    public abstract AuthDao AuthDao();
    private static AppDataBase instance;

    public static synchronized AppDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, "AppDataBase").fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;

    }
    private static Callback roomCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataAsyncTask(instance).execute();

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDataAsyncTask(instance).execute();

        }
    };
    private static class PopulateDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private AdminDao mAdminDao;
        private AuthDao mAuthDao;

        public PopulateDataAsyncTask(AppDataBase db) {
            this.mAdminDao = db.AdminDao();
            this.mAuthDao = db.AuthDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
