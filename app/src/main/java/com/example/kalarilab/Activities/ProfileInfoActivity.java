package com.example.kalarilab.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.kalarilab.KalariLabUtils;
import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Models.AuthModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class ProfileInfoActivity extends BaseActivity implements View.OnClickListener {

    private Button continueBtn;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Button birtDateBtn;
    private Spinner genderSpinner;
    private ArrayAdapter arrayAdapter;
    private DatePickerDialog datePicker;
    private String birthdate = "";
    private KalariLabUtils kalariLabUtils;
    private SessionManagement sessionManagement;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private  AuthModel authModel = new AuthModel();
    private static final String TAG = "ProfileInfoActivityDebug";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        initHooks();
        bindings();







    }




    private void initHooks() {
        sessionManagement = new SessionManagement(this);
        continueBtn = findViewById(R.id.continueBtn);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.topAppBar);
        birtDateBtn = findViewById(R.id.birthDateButton);
        genderSpinner = findViewById(R.id.genderSpinner);
        kalariLabUtils = new KalariLabUtils();
        arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders, R.layout.spinner_item);
        initDatePicker();

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthdate = makeDateToString(year, month + 1, dayOfMonth);
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
        Log.d(TAG, String.valueOf(month));
        return year+"-"+month+"-"+dayOfMonth;
    }


    private void bindings() {
        continueBtn.setOnClickListener(this);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.back:
                        moveToRegisterActivity();
                        break;
                }

                return false;
            }
        });



    }

    private void moveToRegisterActivity() {
        Intent intent = new Intent(ProfileInfoActivity.this, Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueBtn:

                Register();

                break;
        }
    }



    private void Register() {
        authModel = (AuthModel) getIntent().getSerializableExtra("authModelObject");
        authModel.setGender( kalariLabUtils.getGenderFromInt(genderSpinner.getSelectedItemPosition()));
        authModel.setBirthdate( birthdate);
        authModel.setBio("Tell us about your self!");

        sendProfileToFireBase(authModel);
    }

    private void sendProfileToFireBase(AuthModel authModel) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(authModel.getEmail(), authModel.getPassword()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                    getCurrentUser().getUid()).setValue(authModel).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        createSession();
                                        sendVerificationEmail();

                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    moveToRegisterActivity();

                                }
                            });



                        }
                        else {
                            setAlertDialog( task.getException().getMessage(), "emailAlreadyInUse", "Authentication Error!" );
                        }

                    }
                });
    }

    private void IndexUidByEmail() {

        FirebaseDatabase.getInstance().getReference("Emails").child(getEmailIndexingId(authModel.getEmail())).setValue(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("authD", e.getMessage());

            }
        });


    }

    private String getEmailIndexingId(String email) {
        //This method returns the handle of the email to use it for indexing.
        StringBuilder index = new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            if(email.charAt(i) != '@') index.append(email.charAt(i));
            else {
                break;
            }
        }
        Log.d("authD", index.toString());
        return index.toString();
    }

    private void createSession() {
        sessionManagement.saveSession(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    private void setAlertDialog( String message, String useCase, String title) {
        if (!this.isFinishing()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (useCase){
                                case "emailAlreadyInUse":
                                    moveToRegisterActivity();
                                    break;
                                case "activationEmailSuccessfullySent":
                                   moveToAvatarActivity();
                                   Log.d(TAG, "l");
                                   break;
                            }
                        }
                    } )

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void moveToAvatarActivity() {
        String value="registrationWithEmail";
        Intent i = new Intent(this, AvatarSelectionActivity.class);
        i.putExtra("key",value);
        startActivity(i);
    }

    private void sendVerificationEmail() {

        FirebaseUser user_verify = FirebaseAuth.getInstance().getCurrentUser();
        user_verify.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                setAlertDialog("A verification message has been sent to your e-mail! Please click on the activation link.", "activationEmailSuccessfullySent", "Activate your account!" );
                IndexUidByEmail();

            }
        });

    }

    private void moveToLogInActivity() {
        Log.d("authD", "s4");
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }



    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    public void openDatePicker(View view) {
        datePicker.show();

    }
    @Override
    protected void onStop() {
        super.onStop();

    }

}