package com.example.kalarilab;

public class TotalPoints {
    private boolean isImported = false;
    private int totalPoints;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
