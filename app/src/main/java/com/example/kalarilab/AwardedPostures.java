package com.example.kalarilab;

import java.util.ArrayList;
import java.util.List;

public class AwardedPostures {
    private boolean isImported = false;
    private String awardedPostures ;

    public boolean Imported() {
        return isImported;
    }

    public void setImportedStatus(boolean imported) {
        isImported = imported;
    }

    public String getAwardedPostures() {
        return awardedPostures;
    }

    public void setAwardedPostures(String awardedPostures) {
        this.awardedPostures = awardedPostures;
    }}
