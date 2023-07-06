package com.example.kalarilab.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kalarilab.R;
import com.example.kalarilab.SessionManagement;
import com.example.kalarilab.Models.AuthModel;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;


public class LogInActivity extends BaseActivity implements View.OnClickListener {
    //This activity takes care of logging in.
    //And it is the launching activity.
    private Button logInButt, goToSignUpButton;
    private Button signInGmail,  forgotPassword;
    private EditText userNameEntry, passwordEntry;
    private TextInputLayout userNameParent, passwordEntryParent;
    private FirebaseAuth mAuth;
    public SessionManagement sessionManagement;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private FirebaseUser user ;
    private ProgressBar progressBar;
    AuthModel authModel = new AuthModel();
    private SignInCredential credential;
    private String idToken;
    private final static String TAG = "LoginActivityDebug";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
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

        logInButt.setOnClickListener(this);
        goToSignUpButton.setOnClickListener(this);
        signInGmail.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        userNameParent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) userNameParent.setBoxStrokeColor(getResources().getColor(R.color.KalariLAbSecondary));
                else {
                    //Sets the color of the input box when in focus.
                    userNameParent.setBoxStrokeColor(getResources().getColor(R.color.darkGrey));
                }
            }

        });

        passwordEntryParent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) userNameParent.setBoxStrokeColor(getResources().getColor(R.color.KalariLAbSecondary));
                else {
                    //Sets the color of the input box when in focus.

                    userNameParent.setBoxStrokeColor(getResources().getColor(R.color.darkGrey));
                }
            }

        });

    }


    public void initHooks() {
        sessionManagement = new SessionManagement(LogInActivity.this);
        logInButt = findViewById(R.id.LogIn);
        goToSignUpButton = findViewById(R.id.goToSignUp);
        signInGmail = findViewById(R.id.signInGmail);
        userNameEntry = findViewById(R.id.editTextUserName);
        passwordEntry = findViewById(R.id.editTextPassword);
        userNameParent = findViewById(R.id.editTextUserNameParent);
        passwordEntryParent = findViewById(R.id.editTextPasswordParent);
        mAuth = FirebaseAuth.getInstance();
        sessionManagement = new SessionManagement(LogInActivity.this);
        progressBar = findViewById(R.id.progressBar);
        forgotPassword = findViewById(R.id.forgotPassword);
        checkFreshInstall();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LogIn:
                try {
                    checkEnteredInfo(this.userNameEntry.getText().toString(),
                            this.passwordEntry.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.goToSignUp:
                moveToSignUpActivity();
                break;
            case R.id.signInGmail:
                oneTapSignInGoogle();
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.forgotPassword:
                resetPassword();
                break;


        }


    }

    private void resetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your email.");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            sendResetEmail(input.getText().toString().toLowerCase(Locale.ROOT).trim());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void sendResetEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            setAlertDialog("Reset email has been sent successfully.", "Sent");
                        } else {
                            // Error occurred
                            setAlertDialog("Error sending password reset email. Check your e-mail is correct", "Error");

                            Log.e(TAG, "Error sending password reset email. Check your e-mail is correct", task.getException());
                        }
                    }
                });
    }

    private void oneTapSignInGoogle() {

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                        setAlertDialog( "Too many attempts please try again later, please try again later!", "Too many attempts!" );
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authModel = new AuthModel();


        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                     credential = oneTapClient.getSignInCredentialFromIntent(data);
                     idToken = credential.getGoogleIdToken();
                     progressBar.setVisibility(View.VISIBLE);
                     checkIfUserAlreadyRegisteredWithGmail(credential.getId());







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
        }



    }
    private void startGmailLogin(String idToken, SignInCredential credential){
        try {


            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                createSession(idToken);
                                progressBar.setVisibility(View.GONE);
                                user = mAuth.getCurrentUser();
                                authModel.setPassword(credential.getPassword());
                                authModel.setEmail(user.getEmail());
                                authModel.setFullName(user.getEmail());
                                authModel.setLessonReached(0);
                                authModel.setLevelReached(0);
                                authModel.setPoints(0);
                                authModel.setBio("");
                                moveToMainActivity();


                            } else {
                                progressBar.setVisibility(View.VISIBLE);

                            }
                        }
                    });
        }catch (Exception e){

        }
    }



    private void checkIfUserAlreadyRegisteredWithGmail(String email) {




            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("Emails").addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot snapshot) {
                     try {

                     if (snapshot.hasChild(getEmailIndexingId(email))) {

                         startGmailLogin(idToken, credential);

                     }
                     else {
                         progressBar.setVisibility(View.GONE);
                         setAlertDialog("This E-mail is not connected to any account!", "Authentication Error");

                     }
                     }
                     catch (DatabaseException e){



                     }
                 }


                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             }
            );


    }
    private void checkEnteredInfo(String finalUserName, String finalPassword) throws NoSuchAlgorithmException {
        //this method checks the input if it is valid.
        final String userName = finalUserName.trim().toLowerCase(Locale.ROOT);
        final String password = finalPassword.trim().toLowerCase(Locale.ROOT);


        if (userName.isEmpty()){
            this.userNameParent.setBoxStrokeColor(getResources().getColor(R.color.red));
            return;
        }

        if (password.isEmpty()){
            this.passwordEntryParent.setBoxStrokeColor(getResources().getColor(R.color.red));

            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        logIn(userName, password);
    }

    private void logIn(String email, String password) throws NoSuchAlgorithmException {
        progressBar.setVisibility(View.VISIBLE);
        sendInfoToFireBase(email, password);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }



    private void sendInfoToFireBase(String email, String password) throws NoSuchAlgorithmException {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LLoginD",password);
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified() == true) {

                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                sessionManagement.saveUserId(user.getUid());
                                createSession(sessionManagement.returnUserId());
                                moveToMainActivity();
                            }else {
                                setAlertDialog("Please verify your e-mail by clicking on the activation link sent to your email, you might find the e-mail in the spam folder!", "Activate your account!");
                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            setAlertDialog(task.getException().getMessage(), "ConnectionError!");

                        }
                    }
                });
    }


    private void moveToMainActivity() {
        //this method moves the UI to the main Activity.
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    private void createSession(String userId) {
        //this method creates the session after the log in
        getUserProfileInfoAfterLogin();
        getAvatarInfo();
        sessionManagement.saveSession(userId);

    }

    private void getAvatarInfo() {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Avatars").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);




                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }

    private void getUserProfileInfoAfterLogin() {
        Thread dataBaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AuthModel authModel = snapshot.getValue(AuthModel.class);




                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        try {
            dataBaseThread.start();

        } catch (Exception e) {

        }
    }


    private void moveToOnBoardingActivity(){
        //this method moves the UI to the boarding activity when the install is fresh.
        Intent intent = new Intent(this, OnBoarding.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    private void moveToSignUpActivity() {
        //tis method moves the UI to the sign up actvity
        Intent intent = new Intent(LogInActivity.this, Register.class);
        startActivity(intent);

    }







    @Override
    protected void onStart() {
        super.onStart();
        //On start it checks if the install is fresh on start
        checkFreshInstall();


    }




    private void checkSession(){
        //this method checks if there are a running session otherwise it asks thes user to sign in
        if(!Objects.equals(sessionManagement.returnSession(), "")) {

            startActivity(new Intent(LogInActivity.this, MainActivity.class));

        }else {

            // Machen sie nicht bitte
        }


    }





    public void checkFreshInstall(){
        //this method checks if the install is fresh, if it is it shows the boarding activity, id not it checks the session
        if (sessionManagement.getFRESH_INSTALLStatus()){
            //Show the onBoarding screen
            Log.d("autha", "0");
            moveToOnBoardingActivity();
        }else {

            checkSession();
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
    private String getEmailIndexingId(String email) {
        //This method returns the handle of the email to use it for indexing.
        StringBuilder index = new StringBuilder();
        for (int i = 0; i < email.length(); i++) {
            if(email.charAt(i) != '@') index.append(email.charAt(i));
            else {
                break;
            }
        }
        return index.toString();
    }


}