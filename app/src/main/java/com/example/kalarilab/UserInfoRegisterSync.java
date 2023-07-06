package com.example.kalarilab;

public class UserInfoRegisterSync {
    //Any class that ends with 'Sync' it is used to facilitate actions between different threads -A.Al-Maliki
    private int userID;
    private boolean isChanged = false;
    public int getUserID(){return userID;}

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void changeID(){
        isChanged = true;
    }
    public boolean changed (){
        return isChanged;
    }

}
