package com.example.kalarilab.Activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Models.AuthModel;
import com.example.kalarilab.Utils;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.internal.Util;


public class Register extends BaseActivity implements View.OnClickListener {


    //// The API is retired for now. so the related functions and variables are commented out.
    // the current authentication method is carried

    private static final String TAG = "authDebug";
    private Button goToSignInBtn, registerBtn, signUpGmailBtn, TandSBtn;
    private EditText  emailEntry, passwordEntry, fullNameEntry;
    private ProgressBar progressBar;
    private TextInputLayout emailEntryParent, passwordEntryParent;
    public SessionManagement sessionManagement;
    private TextView warningTextEmail, warningTextPassword, warningTextUserName;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private FirebaseAuth mAuth;
    private FirebaseUser user ;
    private AlertDialog.Builder alertDialogBuilder;
    AuthModel authModel = new AuthModel();
    private Map<String, String> watchedLessons;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initHooks();
        bindings();
        initiateGoogleSignIn();
        initiateOneTapLogIn();


    }

    private void initiateOneTapLogIn() {
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false)
                .build();

    }

    private void initiateGoogleSignIn() {
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

    }

    private void bindings() {
        goToSignInBtn.setOnClickListener(this);
        signUpGmailBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        emailEntryParent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailEntryParent.setBoxStrokeColor(getResources().getColor(R.color.KalariLAbSecondary));
                }
                else {
                    emailEntryParent.setBoxStrokeColor(getResources().getColor(R.color.darkGrey));
                }
            }

        });
        emailEntry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    warningTextEmail.setText("");
                }
            }
        });
        passwordEntryParent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    emailEntryParent.setBoxStrokeColor(getResources().getColor(R.color.KalariLAbSecondary));
                else {
                    emailEntryParent.setBoxStrokeColor(getResources().getColor(R.color.darkGrey));
                }
            }

        });



        passwordEntry.setOnClickListener(this);
        emailEntry.setOnClickListener(this);
        TandSBtn.setOnClickListener(this);

    }

    private void initHooks() {
        mAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.register);
        goToSignInBtn = findViewById(R.id.goToSignIn);
        emailEntry = findViewById(R.id.editTextEmail);
        passwordEntry = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        emailEntryParent = findViewById(R.id.editTextEmailParent);
        passwordEntryParent = findViewById(R.id.editTextPasswordParent);
        signUpGmailBtn = findViewById(R.id.signInGmail);
        sessionManagement = new SessionManagement(Register.this);
        warningTextEmail = findViewById(R.id.warningTextEmail);
        warningTextPassword = findViewById(R.id.warningTextPassword);
        alertDialogBuilder = new AlertDialog.Builder(this);
        fullNameEntry = findViewById(R.id.editTextFullName);
        authModel = new AuthModel();
        watchedLessons = new HashMap<>();
        TandSBtn = findViewById(R.id.TandS);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goToSignIn:
                moveToSignInActivity();
                break;
            case R.id.register:
                try {
                    checkInfo(this.emailEntry.getText().toString(), this.passwordEntry.getText().toString(),
                            this.fullNameEntry.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.signInGmail:

                oneTapSignInGoogle();
                progressBar.setVisibility(View.VISIBLE);
                break;

            case R.id.editTextEmail:
                warningTextEmail.setText("");
                break;
            case R.id.editTextPassword:
                warningTextPassword.setText("");
                break;
            case R.id.editTextUserName:
                warningTextUserName.setText("");
                break;
            case R.id.TandS:
                moveToTandSActivity();
        }
    }

    private void moveToTandSActivity() {
        Intent intent = new Intent(this, TermsAndServices.class);
        startActivity(intent);
    }

    private void oneTapSignInGoogle() {
        Utils.LockGlobalScreen(this);
        progressBar.setVisibility(View.VISIBLE);
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            progressBar.setVisibility(View.GONE);


                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                            progressBar.setVisibility(View.GONE);
                            Utils.UnlockGlobalScreen(Register.this);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, Objects.requireNonNull(e.getMessage()));

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setAlertDialog("Too many attempts please try again later", "Too many attempts!");
                        progressBar.setVisibility(View.GONE);
                        Utils.UnlockGlobalScreen(Register.this);


                    }
                });

    }

    private void moveToSignInActivity() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }



    private void checkInfo(String email, String password, String fullName) throws NoSuchAlgorithmException {
        final String finalEmail = email.toLowerCase(Locale.ROOT).trim();
        final String finalPassword = password.toLowerCase(Locale.ROOT).trim();
        final String finalFullName = fullName.toLowerCase(Locale.ROOT).trim();

        if (finalEmail.isEmpty()) {
            warningTextEmail.setText(R.string.empty_email_field);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(finalEmail).matches()) {
            warningTextEmail.setText(R.string.invalid_email);

            return;
        }
        if (finalPassword.isEmpty()) {
            warningTextPassword.setText(R.string.empty_password);
            return;
        }
        if (finalPassword.length() < 6) {
            warningTextPassword.setText(R.string.short_password);
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        saveDataInAuthModelObject(finalEmail, finalPassword, finalFullName);
        moveToProfileInfoActivity();
        progressBar.setVisibility(View.GONE);


    }





    private void moveToProfileInfoActivity() {
        Intent intent = new Intent(Register.this, ProfileInfoActivity.class);
        intent.putExtra("authModelObject", authModel);

        this.startActivity(intent);
    }


    private void saveDataInAuthModelObject(String email, String password, String fullName) throws NoSuchAlgorithmException {
        authModel = new AuthModel();
        authModel.setFullName(fullName);
        authModel.setEmail(email);
        authModel.setPassword( password);
        authModel.setPoints(0);
        authModel.setLessonReached(0);
        authModel.setLevelReached(0);

    }

//    public static String hashPassword(String password) throws NoSuchAlgorithmException {
//
//        MessageDigest md = MessageDigest.getInstance("SHA-512");
//        md.reset();
//        md.update(password.getBytes());
//        byte[] mdArray = md.digest();
//        StringBuilder sb = new StringBuilder(mdArray.length * 2);
//        for(byte b : mdArray) {
//            int v = b & 0xff;
//            if(v < 16)
//                sb.append('0');
//            sb.append(Integer.toHexString(v));
//        }
//
//
//
//        return sb.toString();
//
//    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQ_ONE_TAP:
                try {

                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();

                    if (idToken !=  null) {
                        progressBar.setVisibility(View.VISIBLE);
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            progressBar.setVisibility(View.GONE);
                                            user = mAuth.getCurrentUser();
                                            authModel.setPassword(credential.getPassword());
                                            authModel.setEmail(user.getEmail());
                                            authModel.setFullName(user.getDisplayName());
                                            authModel.setLessonReached(0);
                                            authModel.setLevelReached(0);
                                            authModel.setPoints(0);
                                            authModel.setBio("Tell us about yourself!");
                                            authModel.setGender("M");
                                            storeUserInfoInDB();
                                           moveToAvatarActivity();

                                        } else {
                                                progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case CommonStatusCodes.CANCELED:
                            showOneTapUI = false;
                            break;
                        case CommonStatusCodes.NETWORK_ERROR:

                            break;

                    }
                    break;
                }
        }}
    private void moveToAvatarActivity() {
        String value="registrationWithGmail";
        Intent i = new Intent(this, AvatarSelectionActivity.class);
        i.putExtra("key",value);
        startActivity(i);
    }



    private void storeUserInfoInDB() {

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                    getCurrentUser().getUid()).setValue(authModel).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        indexUidByEmail();
                        createSession();

                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("authDebugD",  "0" + e.getMessage());

                }
            });



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



    private void createSession() {
        sessionManagement.saveSession(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    private void indexUidByEmail() {

        FirebaseDatabase.getInstance().getReference("Emails").child(getEmailIndexingId(user.getEmail())).setValue(FirebaseAuth.getInstance().
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
}
