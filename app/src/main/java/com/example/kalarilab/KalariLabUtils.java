package com.example.kalarilab;

import com.google.android.exoplayer2.util.Log;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class KalariLabUtils {
    private final static String TAG = "KalariLabUtilsDebug";
    public String ageCalculator(String dob){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("YYYY-MMM-DD");

        LocalDate dateOfBirth = LocalDate.parse(dob, df);
        LocalDate currentDate = LocalDate.now();
        if(dob != null && currentDate != null ){
            return String.valueOf(Period.between(dateOfBirth, currentDate).getYears());
        }else if(currentDate.equals(dob)) {
            return "Illegal";
        }
        else {
            return "0";
        }
    }

    public int getIndexOfGender(String returnUser_gender) {
        Log.d(TAG, returnUser_gender);
        switch (returnUser_gender){
            case "M":
                return 0;
            case "F":
                return 1;

            case "O":
                return 2;

            default:
                return 2;
        }
        }



    public String getGenderFromChar(String c) {
        try {


            switch (c) {
                case "M":
                    return "Male";
                case "F":
                    return "Female";

                case "O":
                    return "Other";

                default:
                    return "Other";
            }
        }catch (Exception e){
            return "Other";
        }
    }

    public String getGenderFromInt(int genderId) {
        switch (genderId){
            case 0:
                return "M";
            case 1:
                return "F";

            case 2:
                return "O";

            default:
                return "";
        }
    }

}
