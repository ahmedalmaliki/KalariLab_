package com.example.kalarilab.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kalarilab.KalariLabUtils;
import com.example.kalarilab.Models.AdminsModel;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.ViewModels.AdminPanelViewModel;
import com.example.kalarilab.ViewModels.AuthViewModel;
import com.example.kalarilab.db.AppDataBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.r0adkll.slidr.Slidr;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Objects;

public class Settings extends BaseActivity implements View.OnClickListener {
    private EditText nameEntry, bioEntry;
    private Spinner gendersSpinner;
    private ArrayAdapter arrayAdapter ;
    private KalariLabUtils kalariLabUtils;
    private SessionManagement sessionManagement;
    private Button birtDateBtn,  passwordBtn, signOutBtn, panelBtn;
    private AdminPanelViewModel adminPanelViewModel;
    private DatePickerDialog datePicker;
    private String birthdate = "";
    private final static String TAG = "settingsDebug";
    private SwipeRefreshLayout swipeRefreshLayout;
    private AuthViewModel authViewModel;
    private AuthModel authModel1;
    private String bioPlaceHolder = "Tell us about your self!";
    private AppDataBase db = AppDataBase.getInstance(this);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Slidr.attach(this);
        initHooks();
        observeDate();
        bindings();




    }



    private void bindings() {

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gendersSpinner.setAdapter(arrayAdapter);
        passwordBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        panelBtn.setOnClickListener(this);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            //swiping the top of the page to refresh the page
            @Override
            public void onRefresh() {
                refreshCurrActivity();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        bioEntry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    if(bioEntry.getText().toString().trim().equals("") || bioEntry.getText().toString().equals(bioPlaceHolder)) {
                        bioEntry.setText("");
                    }
                }else {
                    if(bioEntry.getText().toString().trim().equals("")) {
                        bioEntry.setText(bioPlaceHolder);
                    }

                }
            }
        });

    }



    private void initHooks() {
        adminPanelViewModel = new AdminPanelViewModel();
        adminPanelViewModel.setActivity(this); //passing the activity context to the main view model
        try {
            adminPanelViewModel.init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(this);
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }
        nameEntry = findViewById(R.id.editTextName);
        bioEntry = findViewById(R.id.editTextBio);

        gendersSpinner = findViewById(R.id.genderSpinner);
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders, R.layout.spinner_item);

        sessionManagement = new SessionManagement(this);
        kalariLabUtils = new KalariLabUtils();
        birtDateBtn = findViewById(R.id.birthDateButton);

        initDatePicker();
        passwordBtn = findViewById(R.id.resetPasswordButton);
        signOutBtn = findViewById(R.id.signOutButton);
        panelBtn = findViewById(R.id.panel);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

    }

    private void observeDate() {

        authViewModel.getmAuthModel().observe(this, new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
                setText();
                birthdate = authModel1.getBirthdate();
                gendersSpinner.setSelection(kalariLabUtils.getIndexOfGender(authModel1.getGender()));
                checkIfUserISAdmin();

            }
        });

    }

    private void checkIfUserISAdmin() {

        adminPanelViewModel.getAdminModel().observe(this, new Observer<AdminsModel>() {
            @Override
            public void onChanged(AdminsModel adminsModel) {

                for(String email: adminsModel.getEmails()) {

                    if (Objects.equals(authModel1.getEmail(), email)){
                        userIsAdmin();
                    }

                }
            }
        });
    }

    private void userIsAdmin() {
        panelBtn.setVisibility(View.VISIBLE);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        sessionManagement.removeSession();
        startActivity(new Intent(Settings.this, LogInActivity.class));
        sendBroadcastToPreventAccessToAllActivities();
        clearSharedPreferenceAvatarInfo();
        clearDB();
        finish();




    }

    private void clearDB() {
        Thread dbThread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.clearAllTables();

            }
        });
dbThread.start();
    }

    private void clearSharedPreferenceAvatarInfo() {
      sessionManagement.save_hair("");
      sessionManagement.save_skin_tone_drawable("");
      sessionManagement.saveLatestAwardedPostureUri("");
    }

    private void sendBroadcastToPreventAccessToAllActivities() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);
    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthdate = makeDateToString(year, month, dayOfMonth);

                birtDateBtn.setText(birthdate);




            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }
    private String makeDateToString(int year, int month, int dayOfMonth) {
        return year+"-"+month+"-"+dayOfMonth;
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openDatePicker(View view) {
        datePicker.show();

    }
    private void setText(){
        nameEntry.setText(authModel1.getFullName());
        if(!authModel1.getBio().equals(""))      {
            bioEntry.setText(authModel1.getBio());
        }
        else {
            bioEntry.setText(bioPlaceHolder);

        }

        birtDateBtn.setText(authModel1.getBirthdate());

    }
    private boolean infoHasChangedOrEmpty() {
        if (authModel1 != null) {
            if (authModel1.getFullName().equals("") || authModel1.getBirthdate().equals("")
                    || authModel1.getBio().equals("")) {
                return true;
            } else if (nameEdited() || bioEdited() || birthDateEdited() || genderEdited()) {

                return true;
            } else {
                return false;
            }
        } else {
            setAlertDialog("Trouble fetching personal information please try again.", "Missing personal data!");
            return false;
        }

    }


    private void setAlertDialog( String message,String title) {
        if (!this.isFinishing()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    private boolean genderEdited() {
        return gendersSpinner.getSelectedItemPosition() != kalariLabUtils.getIndexOfGender(authModel1.getGender());
    }

    private boolean birthDateEdited() {
        if (birthdate.isEmpty() ){
            return false;
        }
        else  return !birthdate.equals(authModel1.getBirthdate() );
    }

    private boolean bioEdited() {

        return !bioEntry.getText().toString()
                .equals(authModel1.getBio());
    }

    private boolean nameEdited() {
        return !nameEntry.getText().toString()
                .equals(authModel1.getFullName());
    }
    private void sendInputInfo() {
        Thread pushInfo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("fullName").setValue(nameEntry.getText().toString());
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("gender").setValue(kalariLabUtils.getGenderFromInt(gendersSpinner.getSelectedItemPosition()));
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).child("birthdate").setValue(birthdate);
                    if (!bioEntry.getText().toString().equals(bioPlaceHolder)) {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).child("bio").setValue(bioEntry.getText().toString());
                    }else {
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).child("bio").setValue("");
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });
        pushInfo.run();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(infoHasChangedOrEmpty() ){
            sendInputInfo();

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetPasswordButton:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
            case  R.id.signOutButton:
                signOut();

                break;
            case R.id.panel:
                moveToPanelActivity();
                break;

        }
    }
    private void moveToPanelActivity(){
        //this method moves the UI to the boarding activity when the install is fresh.
        Intent intent = new Intent(this, AdminPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    private void refreshCurrActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
