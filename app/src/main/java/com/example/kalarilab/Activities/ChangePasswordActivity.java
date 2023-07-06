package com.example.kalarilab.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.R;
import com.example.kalarilab.ViewModels.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;
import com.r0adkll.slidr.Slidr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity  {
    private EditText currentPasswordEntry, newPasswordEntry;
    private Button resetPassword;
    private androidx.appcompat.widget.Toolbar toolbar ;
    private AuthViewModel authViewModel;
    private AuthModel authModel1 = new AuthModel();
    private final static String TAG = "ChangePasswordActivityDebug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Slidr.attach(this);
        initHooks();
        observeData();
        bindings();
    }

    private void observeData() {
        authViewModel.getmAuthModel().observe(this, new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                authModel1 = authModel;
            }
        });
    }

    private void bindings() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               initiateReset();
                clearEntryFeilds();
            }
        });

    }

    private void clearEntryFeilds() {
        currentPasswordEntry.setText("");
        newPasswordEntry.setText("");
    }

    private void pushNewPassword() throws NoSuchAlgorithmException {

    }

    private void initiateReset() {
        if(authModel1 != null) {
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(authModel1.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if (signInMethods.contains(currentPasswordEntry.getText().toString())) {
                                    // Password is correct
                                    try {
                                        pushNewPassword();
                                    } catch (NoSuchAlgorithmException e) {
                                        Toast.makeText(ChangePasswordActivity.this, "Unable to reset password!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Password is incorrect
                                    Toast.makeText(ChangePasswordActivity.this, "Entered password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Error occurred
                                Exception error = task.getException();
                                setAlertDialog("Conncetion error!", "error");

                            }
                        }
                    });


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






    private void initHooks() {
        authViewModel = new AuthViewModel();
        authViewModel.setActivity(this);
        try {
            authViewModel.init();

        }catch (Exception e){
            android.util.Log.d(TAG, e.getMessage());
        }
        currentPasswordEntry = findViewById(R.id.editTextCurrentPassword);
        newPasswordEntry = findViewById(R.id.editTextNewPassword);
        resetPassword = findViewById(R.id.resetPasswordButton);
        toolbar = findViewById(R.id.topAppBar);

    }



    private void showAlertDialog( String message) {
        if (!this.isFinishing()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Connection Error!")
                    .setMessage(message)

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(password.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for(byte b : mdArray) {
            int v = b & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }



        return sb.toString();

    }
}