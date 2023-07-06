package com.example.kalarilab;

public class LevelReached {
    private boolean isImported = false;
    private int levelReached;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }
}
