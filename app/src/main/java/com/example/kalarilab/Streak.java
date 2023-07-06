package com.example.kalarilab;

import android.content.Context;

import java.util.Calendar;


public class Streak {
    private StreakSharedPreference streakSharedPreference;
    //This is gonna be tricky so help me god
    private ProgressTrackingSystem progressTrackingSystem;
    private Calendar c ;
    private Context context;
    private  int weekProgress  ;
    private int counter ;
    private static final int WEEK = 7;
    private int startDay ;
    private int currentDay ;
    private int lastTimeTheCounterUpdated;




    public Streak(ProgressTrackingSystem progressTrackingSystem, Context context) {
        this.progressTrackingSystem = progressTrackingSystem;
        this.context = context;
        initHooks();

    }

    public void initHooks(){
        streakSharedPreference = new StreakSharedPreference(context);
        c = Calendar.getInstance();
         counter = streakSharedPreference.getCounter();
         startDay = streakSharedPreference.getFirstDay();
         currentDay = c.get(Calendar.DAY_OF_YEAR);
         lastTimeTheCounterUpdated  = streakSharedPreference.getLastTimeTheCounterUpdated();
    }

    public void addDailyLearningReward(){
        //Called every time the user finish 60% of a lesson
        //But only adds point once a day
        checkIfANewLearningDayPassed();
    }

    public void setWeekProgress (){
        int daysPassedSinceFirstDay = currentDay - startDay;
        if (daysPassedSinceFirstDay <= 7 ){
            weekProgress =  daysPassedSinceFirstDay;
        }else {
            weekProgress = 1;
            streakSharedPreference.setFirstDay(currentDay);
            streakSharedPreference.setCounter(0);
            progressTrackingSystem.setWeeklyPointsZero();


        }

    }

    private void addNewPoints() {
        if (counter < 5){
            progressTrackingSystem.addPointsAfterFinishingLessons();
        }else if(counter == 5){
            progressTrackingSystem.addPointsAfterFinishingLessons();
            progressTrackingSystem.addPointsAfterFiveDaysStreakInAWeek();
        }

    }

    private void addDayToCounter(){

            streakSharedPreference.setCounter(counter + 1);
            streakSharedPreference.setLastTimeTheCounterUpdated(currentDay);
        }


    private void checkIfANewLearningDayPassed(){
        //a day of exercise is added when a user finish 60% of  a lesson inside a day
        //The next line to make sure that this method is only called once a day
        if (currentDay - lastTimeTheCounterUpdated >= 1){
            addDayToCounter();
            addNewPoints();
        }
    }

    //A method in the cloud is called at the end of every week to give points for rest days
    //only when 5 days of exercise where done
    private int countRestDays(){
        if (counter >= 5) {
            return weekProgress - counter;
        }
        return 0;
    }

    public int getWeekProgress(){
        return weekProgress;
    }

}
