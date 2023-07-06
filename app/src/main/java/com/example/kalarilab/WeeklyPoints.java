package com.example.kalarilab;

public class WeeklyPoints {
    private boolean isImported = false;
    private int weeklyPoints;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public int getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }
}
