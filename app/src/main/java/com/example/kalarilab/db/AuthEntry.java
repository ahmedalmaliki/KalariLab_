package com.example.kalarilab.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")


public class AuthEntry {
    @PrimaryKey(autoGenerate = true)
    private int index;
    private String fullName;
    private String email;
    private String password;
    private String bio;
    private String gender;
    private String birthdate;
    private int levelReached;
    private int lessonReached;
    private int points;
    private int weeklyPoints;
    private int awardedPoints;
    private String awardedPostures ;
    private  String skinTone;
    private String hair;
    public AuthEntry() {
    }


    public AuthEntry(String fullName, String email, String password, String bio, String gender, String birthdate, int levelReached, int lessonReached, int points, int weeklyPoints, int awardedPoints, String awardedPostures, String skinTone, String hair) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.gender = gender;
        this.birthdate = birthdate;
        this.levelReached = levelReached;
        this.lessonReached = lessonReached;
        this.points = points;
        this.weeklyPoints = weeklyPoints;
        this.awardedPoints = awardedPoints;
        this.awardedPostures = awardedPostures;
        this.skinTone = skinTone;
        this.hair = hair;
    }

    public  String getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(String skinTone) {
        this.skinTone = skinTone;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }

    public int getLessonReached() {
        return lessonReached;
    }

    public void setLessonReached(int lessonReached) {
        this.lessonReached = lessonReached;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }

    public int getAwardedPoints() {
        return awardedPoints;
    }

    public void setAwardedPoints(int awardedPoints) {
        this.awardedPoints = awardedPoints;
    }

    public String getAwardedPostures() {
        return awardedPostures;
    }

    public void setAwardedPostures(String awardedPostures) {
        this.awardedPostures = awardedPostures;
    }



    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
