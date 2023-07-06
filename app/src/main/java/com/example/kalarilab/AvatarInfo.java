package com.example.kalarilab;

public class AvatarInfo {
    private int skinTone;
    private  int hair;

    public AvatarInfo(int skinTone, int hair) {
        this.skinTone = skinTone;
        this.hair = hair;
    }

    public AvatarInfo() {
    }

    public int getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(int skinTone) {
        if (skinTone != 0) {
            this.skinTone = skinTone;
        }else {
            this.skinTone = R.drawable.sbmc1;
        }
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        if(hair != 0) {
            this.hair = hair;

        }else{
            this.hair = R.drawable.mh1;
        }
    }
}
