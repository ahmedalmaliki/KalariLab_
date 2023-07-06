package com.example.kalarilab;

public class AwardedPoints {
    private boolean isImported = false;
    private int awardedPoints;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public int getAwardedPoints() {
        return awardedPoints;
    }

    public void setAwardedPoints(int awardedPoints) {
        this.awardedPoints = awardedPoints;
    }
}
