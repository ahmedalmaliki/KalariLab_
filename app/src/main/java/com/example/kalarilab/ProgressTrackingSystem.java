package com.example.kalarilab;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kalarilab.Models.AuthModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProgressTrackingSystem {


    private ProgressTrackingSystemCallBack progressTrackingSystemCallBack;
    TotalPoints totalPoints  =  new TotalPoints();
    WeeklyPoints weeklyPoints = new WeeklyPoints();
    AwardedPoints awardedPoints = new AwardedPoints();
    AwardedPostures awardedPostures = new AwardedPostures();
    SessionManagement sessionManagement ;
    public static final String TAG = "ProgressTrackingSystemDebug";
    public ProgressTrackingSystem() {
    }

    public ProgressTrackingSystem(ProgressTrackingSystemCallBack progressTrackingSystemCallBack, Context context) {
        this.progressTrackingSystemCallBack = progressTrackingSystemCallBack;
        this.sessionManagement = new SessionManagement(context);
    }

    public void getTotalPoints(String id) {
        //called from inside this class only
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);

                        totalPoints.setTotalPoints(authModel.getPoints());
                        totalPoints.setImportedStatus(true);
                        Log.d("pointsDebugs", "3 " + String.valueOf(totalPoints.Imported()));

                        synchronized (totalPoints) {
                                totalPoints.notifyAll();
                            }
                        Log.d("pointsDebugs", "4 " + String.valueOf(totalPoints.Imported()));


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

    public void getTotalPoints(TotalPoints totalPoints, String id) {
        //Parameter version
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);

                        totalPoints.setTotalPoints(authModel.getPoints());
                        totalPoints.setImportedStatus(true);

                        synchronized (totalPoints) {
                            totalPoints.notifyAll();
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

    private void addPointsToTotalPoints(int newPoints, String id){
        getTotalPoints(id);
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!totalPoints.Imported()){
                    synchronized (totalPoints){
                        try {
                            Log.d("Waiting_thread", "waiting");
                            totalPoints.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


                FirebaseDatabase.getInstance().getReference("Users").child(id).child("points").setValue(totalPoints.getTotalPoints() + newPoints);

                    }
                });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }

    }
    public void getWeeklyPoints(String id) {
        // called from inside this class only
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        weeklyPoints.setWeeklyPoints(authModel.getWeeklyPoints());
                        weeklyPoints.setImportedStatus(true);
                        synchronized (weeklyPoints) {
                            weeklyPoints.notifyAll();
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
    public void getWeeklyPoints(WeeklyPoints weeklyPoints, String id) {
        //Parameters version
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        weeklyPoints.setWeeklyPoints(authModel.getWeeklyPoints());
                        weeklyPoints.setImportedStatus(true);
                        synchronized (weeklyPoints) {
                            weeklyPoints.notifyAll();
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


    private void addPointsToWeeklyPoints(int newPoints, String id){
        getWeeklyPoints(id);
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!weeklyPoints.Imported()){
                    synchronized (weeklyPoints){
                        try {
                            Log.d("Waiting_thread", "waiting");
                            weeklyPoints.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                FirebaseDatabase.getInstance().getReference("Users").child(id).child("weeklyPoints").setValue(weeklyPoints.getWeeklyPoints() + newPoints);

            }
        });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }
    public void getAwardedPoints( String id) {
        //called from inside this class only
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        awardedPoints.setAwardedPoints(authModel.getAwardedPoints());
                        awardedPoints.setImportedStatus(true);
                        synchronized (awardedPoints) {
                            awardedPoints.notifyAll();
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

    private void addPointsToAwardedPoints(int newPoints, String id){
        //function used by admins only
        getAwardedPoints(id);
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!awardedPoints.Imported()){
                    synchronized (awardedPoints){
                        try {
                            Log.d("Waiting_thread", "waiting");
                            awardedPoints.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


                FirebaseDatabase.getInstance().getReference("Users").child(id).child("awardedPoints").setValue(awardedPoints.getAwardedPoints() + newPoints).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressTrackingSystemCallBack.awardedPointsSuccessfully();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressTrackingSystemCallBack.failedAwardingPoints();
                    }
                }) ;


            }
        });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }

    }

    public void addPointsAfterFinishingLessons(){
        addPointsToTotalPoints(10, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
        addPointsToWeeklyPoints(10, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
    }
    public void addPointsAfterFiveDaysStreakInAWeek(){
        addPointsToWeeklyPoints(20, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
        addPointsToTotalPoints(20, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
    }
    public void addPointsForEveryRestDay(){
        //rest days inside 7 days, only added if five days of these 7 days were used
        addPointsToWeeklyPoints(10, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
        addPointsToTotalPoints(10, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
    }
    public void addAwardedPoints(int awardedPoints, String id){
        addPointsToAwardedPoints(awardedPoints, id);
        addPointsToWeeklyPoints(awardedPoints, id);
        addPointsToTotalPoints(awardedPoints, id);
    }


    private void addHalfwayBonus(){
        addPointsToWeeklyPoints(100, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
        addPointsToTotalPoints(100, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
    }
    private void addEndOfLevelBonus(){
        addPointsToWeeklyPoints(100, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
        addPointsToTotalPoints(100, FirebaseAuth.getInstance().
                getCurrentUser().getUid());
    }


    public void getLevelReached(LevelReached levelReached) {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        levelReached.setLevelReached(authModel.getLevelReached());
                        levelReached.setImportedStatus(true);
                        synchronized (levelReached) {
                            levelReached.notifyAll();
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




    public void getLessonReached(LessonReached lessonReached) {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        lessonReached.setLessonReached(authModel.getLessonReached());
                        lessonReached.setImportedStatus(true);
                        synchronized (lessonReached) {
                            lessonReached.notifyAll();
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

    public void uploadLevelReached(int levelReached) {

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("levelReached").setValue(levelReached);

    }

    public void uploadLessonReached(int lessonReached) {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("lessonReached").setValue(lessonReached);

    }


    public void getAwardedPostures(AwardedPostures awardedPostures1) {
       getAwardedPostures(FirebaseAuth.getInstance().
               getCurrentUser().getUid(), awardedPostures1);

    }
    public void getAwardedPostures(String id, AwardedPostures awardedPostures1) {

        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);
                        if(authModel.getAwardedPostures() != null) {
                            awardedPostures1.setAwardedPostures(authModel.getAwardedPostures());
                        }else{

                            awardedPostures1.setAwardedPostures("");
                        }
                        awardedPostures1.setImportedStatus(true);

                        try {
                            progressTrackingSystemCallBack.updateHomeFragment();

                        }catch (Exception e){
                            Log.d(TAG, "l");
                        }
                        synchronized (awardedPostures1) {
                            awardedPostures1.notifyAll();
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

    public void sendPosture (String postureName, String id){

        Thread dataBaseThread = new Thread(new Runnable() {

            @Override
            public void run() {
                getAwardedPostures(id, awardedPostures);

                while (!awardedPostures.Imported()){
                    synchronized (awardedPostures){
                        try {

                            Log.d("Waiting_thread", "waiting");
                            awardedPostures.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (awardedPostures.getAwardedPostures() == ""){
                    FirebaseDatabase.getInstance().getReference("Users").child(id).child("awardedPostures").setValue(awardedPostures.getAwardedPostures()+postureName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressTrackingSystemCallBack.awardedPosturesSuccessfully();
                            addNewlyAwardedPosture(postureName, id);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressTrackingSystemCallBack.failedAwardingPostures();
                        }
                    });




                }else{
                FirebaseDatabase.getInstance().getReference("Users").child(id).child("awardedPostures").setValue(awardedPostures.getAwardedPostures()+","+postureName).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressTrackingSystemCallBack.awardedPosturesSuccessfully();
                        addNewlyAwardedPosture(postureName, id);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressTrackingSystemCallBack.failedAwardingPostures();
                    }
                });



            }}
        });

        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }
    private void addNewlyAwardedPosture(String postureName, String id) {
        Log.d(TAG, id);
        FirebaseDatabase.getInstance().getReference("NewlyAwardedPostures").child(id).setValue(postureName);
    }
    public void setWeeklyPointsZero(){
        //called at the beginning of every week
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("weeklyPoints").setValue(0);
    }

    public interface ProgressTrackingSystemCallBack {
        void awardedPointsSuccessfully() ;
        void awardedPosturesSuccessfully();
        void failedAwardingPoints();
        void failedAwardingPostures();
        void updateHomeFragment();
    }

}
