package com.example.kalarilab.Models;

import java.util.List;

public class AdminsModel {
    private List<String> emails;

    public AdminsModel(List<String> emails) {
        this.emails = emails;
    }


    public List<String> getEmails() {
        return emails;
    }
}
