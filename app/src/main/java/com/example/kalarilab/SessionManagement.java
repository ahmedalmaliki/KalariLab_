package com.example.kalarilab;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

public class SessionManagement implements Serializable {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String FRESH_INSTALL = "fresh_install" ;
    String REFRESH_TOKEN = "refresh_token" ;
    String USER_ID = "user_id";
    String COSTUMER_ID = "costumer_id";

    String HAIR = "m_hair";
    String F_HAIR = "f_hair";
    String SKIN_TONE_DRAWABLE = "skin_tone_drawable";
    String SKIN_TONE = "skin_tone";
    String SECRET_KEY = "secret_key";
    String PUBLISHED_KEY = "published_key";
    String LESSON_REACHED = "lesson_reached";
    String LEVEL_REACHED = "level_reached";

    String CURR_LEVEL = "curr_level";
    String CURR_CHALLENGE = "curr_challenge";
    String NUM_OF_AWARDED_POSTURES = "num_of_awarded_postures";
    String LATEST_AWARDED_POSTURE_URI = "latest_awarded_posture_uri";
    String  CLOTHES = "clothes";
    String ChallengesFragmentAlreadyVisited = "challengesFragmentAlreadyVisited";
    String HAIR_MARGIN_TOP = "hairMarginTop";
    String TOKEN = "token";
    String PREV_GENDER = "prev_gender";
    public SessionManagement(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void saveSession(String id) {
        editor.putString(SESSION_KEY, id).commit();
    }

    public String returnSession() {
        return sharedPreferences.getString(SESSION_KEY, "");
    }

    public void removeSession() {
        editor.putString(SESSION_KEY, "").commit();
    }



    public void onBoardingSeen(){
        editor.putBoolean(FRESH_INSTALL, false).commit();
    }

    public boolean getFRESH_INSTALLStatus(){
        return sharedPreferences.getBoolean(FRESH_INSTALL, true);
    }
    public void setFRESH_INSTALLStatus(boolean status){
        editor.putBoolean(FRESH_INSTALL, status);
    }
    public String returnRefreshToken(){
        return sharedPreferences.getString(REFRESH_TOKEN, "");
    }
    public void saveRefreshToken(String refresh_token){
        editor.putString(REFRESH_TOKEN, refresh_token).commit();
    }

    public void saveUserId(String user_id){
        editor.putString(USER_ID, user_id).commit();
    }
    public void saveCostumerId(String costumer_id){
        editor.putString(COSTUMER_ID, costumer_id).commit();
    }
    public String returnUserId(){
        return sharedPreferences.getString(USER_ID, "");

    }

    //Profile Info storage



    public void save_hair(String hair){
        editor.putString(HAIR, hair).commit();
    }
    public String return_hair(){
        return sharedPreferences.getString(HAIR, "mh1");

    }
    public void save_token (String token){
        editor.putString(TOKEN, token);
    }
    public String return_token (){
        return sharedPreferences.getString(TOKEN, "");
    }
    public void save_hair_marginTop(int margin){
        editor.putInt(HAIR_MARGIN_TOP,margin).commit();
    }
    public int return_hair_marginTop(){
        return sharedPreferences.getInt(HAIR_MARGIN_TOP, 0);

    }
    public void save_prev_gender (String prev_gender){
        editor.putString(PREV_GENDER, prev_gender);
    }
    public String return_prev_gender (){
        return sharedPreferences.getString(PREV_GENDER, "M");
    }

    public void save_skin_tone_drawable(String skin_tone){
        editor.putString(SKIN_TONE_DRAWABLE, skin_tone).commit();
    }
    public String return_skin_tone_drawable(){
        return sharedPreferences.getString(SKIN_TONE_DRAWABLE, "sbmc1");

    }
    public void save_skin_tone(String tone){
        editor.putString(SKIN_TONE, tone);
    } // to br referenced by the clothing fragment
    public String return_skin_tone(){
        return sharedPreferences.getString(SKIN_TONE, "b");
    }


    public void save_clothes(String clothes){
        editor.putString(CLOTHES, clothes).commit();
    }
    public String return_clothes(){
        return sharedPreferences.getString(CLOTHES, "c1");

    }

    public String returnSecret_Key(){
        return sharedPreferences.getString(SECRET_KEY, "sk_test_y73W3LsYaVsR2alkHjINf4bm003bbnh44C");

    }
    public void savePublished_Key(String published_key){
        editor.putString(PUBLISHED_KEY, published_key).commit();
    }

    public String returnPublished_Key(){
        return sharedPreferences.getString(PUBLISHED_KEY, "pk_test_zV1aA4qKsQN7OlzF1YjyNWOf00KMFl1nbt");

    }
    public void saveLesson_Reached(int lesson_reached){
        editor.putInt(LESSON_REACHED, lesson_reached).commit();
    }

    public int returnLesson_Reached(){
        return sharedPreferences.getInt(LESSON_REACHED, 0);

    }
    public void saveLevel_Reached(int level_reached){
        editor.putInt(LEVEL_REACHED, level_reached).commit();
    }

    public int returnLevel_Reached(){
        return sharedPreferences.getInt(LEVEL_REACHED, 1);

    }

    public void saveCurrLevel(int curr_level){
        editor.putInt(CURR_LEVEL, curr_level).commit();
    }

    public int returnCurrLevel(){
        return sharedPreferences.getInt(CURR_LEVEL, 0);

    }
    public void saveCurrChallenge(int curr_challenge){
        editor.putInt(CURR_CHALLENGE, curr_challenge).commit();
    }

    public int returnCurrChallenge(){
        return sharedPreferences.getInt(CURR_CHALLENGE, 0);

    }
    public void saveNumOfAwardedPostures(int numOfAwardedPostures){
        editor.putInt(NUM_OF_AWARDED_POSTURES, numOfAwardedPostures).commit();
    }
    public int returnNumOfAwardedPostures(){
        return sharedPreferences.getInt(NUM_OF_AWARDED_POSTURES, 0);
    }
    public void saveLatestAwardedPostureUri(String latestAwardedPostureUri){
        editor.putString(LATEST_AWARDED_POSTURE_URI, latestAwardedPostureUri).commit();
    }
    public String returnLatestAwardedPostureUri(){
        return sharedPreferences.getString(LATEST_AWARDED_POSTURE_URI, "");
    }

    public void setChallengesFragmentAlreadyVisited(){
        editor.putBoolean(ChallengesFragmentAlreadyVisited, true).commit();
    }
    public boolean returnChallengesFragmentAlreadyVisited(){
        return sharedPreferences.getBoolean(ChallengesFragmentAlreadyVisited, false);
    }

}
