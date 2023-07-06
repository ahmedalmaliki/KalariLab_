package com.example.kalarilab;

public class LessonReached {
    private boolean isImported = false;
    private int lessonReached;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public int getLessonReached() {
        return lessonReached;
    }

    public void setLessonReached(int lessonReached) {
        this.lessonReached = lessonReached;
    }
}
