package com.vedant.hereami.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;
import com.vedant.hereami.ViewPager.TabWOIconActivity;
import com.vedant.hereami.miscellaneous.RuntimePermissionsActivity;

public class login extends RuntimePermissionsActivity implements View.OnClickListener {
    private EditText mUserEmail;
    private EditText mUserPassWord;
    private Button mLoginToMChat;
    private Button mRegisterUser;
    private FirebaseAuth firebaseAuth;
    private static final int REQUEST_PERMISSIONS = 5;
    private TextView forgetpass;

    //progress dialog
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private CoordinatorLayout coordinatorLayout;
    public String TAG;
    private String publickey;
    private String usermail;
    private Button mRegisteration, mcancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Firebase.setAndroidContext(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        coordinatorLayout = findViewById(R.id.coordinator_layout
        );
        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), TabWOIconActivity.class));
        }

        re();
        //initializing views
        mUserEmail = findViewById(R.id.userEmailChat);
        mUserPassWord = findViewById(R.id.passWordChat);
        mLoginToMChat = findViewById(R.id.btn_LogInChat);

        mRegisterUser = findViewById(R.id.registerUser);
        mRegisteration = findViewById(R.id.btn_login_register);
        mcancel = findViewById(R.id.btn_login_cancel);


        forgetpass = findViewById(R.id.textview_forgetpassword);


        progressDialog = new ProgressDialog(this);
        mLoginToMChat.setOnClickListener(login.this);
        mRegisterUser.setOnClickListener(login.this);
        forgetpass.setOnClickListener(login.this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginToMChat) {
            userLogin();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (view == mRegisterUser) {

            findViewById(R.id.btn_LogInChat).setVisibility(View.GONE);
            findViewById(R.id.registerUser).setVisibility(View.GONE);
            findViewById(R.id.btn_login_register).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_login_cancel).setVisibility(View.VISIBLE);
            //    findViewById(R.id.logout_items).setVisibility(View.GONE);
            //   finish();
            // startActivity(new Intent(this, Register.class));
        }

        if (view == forgetpass) {
            forgetpassword();
        }
        if (view == mRegisteration) {
            registerUser();
        }
        if (view == mcancel) {
            findViewById(R.id.btn_LogInChat).setVisibility(View.VISIBLE);
            findViewById(R.id.registerUser).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_login_register).setVisibility(View.GONE);
            findViewById(R.id.btn_login_cancel).setVisibility(View.GONE);

        }
    }

    private void userLogin() {
        String email = mUserEmail.getText().toString().trim();
        String password = mUserPassWord.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Loggin in Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //start the profile activity
                            // finish();
                            user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {

                                    Intent mainIntent = new Intent(getApplicationContext(), TabWOIconActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);


                                } else {
                                    user.sendEmailVerification();
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Please verify your account!", Snackbar.LENGTH_LONG);

                                    snackbar.show();

                                    //  Snackbar.make(getView().findViewById(R.id.coordinatorLayout), "Please verify your account!", Snackbar.LENGTH_LONG).show();
                                    firebaseAuth.signOut();

                                }
                                //  startActivity(new Intent(getApplicationContext(), Main.class));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }

    public void onPermissionsGranted(final int requestCode) {
        // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public void re() {
        login.super.requestAppPermissions(new
                        String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);

    }

    private void forgetpassword() {
        String email = mUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Reseting Please Wait...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email);
        progressDialog.dismiss();
    }


    private void registerUser() {
        String email = mUserEmail.getText().toString().trim();
        String password = mUserPassWord.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {

                            // Name, email address, and profile photo Url

                            //   finish();
                            //     Intent intent4 = new Intent(Register.this,Main.class).putExtra("first_name", Firstname);
                            //   startActivity(intent4);
                            //        Intent intent4 = new Intent(Register.this, phonenumber.class);
                            //      startActivity(intent4);
                            //     startActivity(new Intent(getApplicationContext(), Main.class));
                            firebaseAuth.signOut();
                            findViewById(R.id.btn_LogInChat).setVisibility(View.VISIBLE);
                            findViewById(R.id.registerUser).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_login_register).setVisibility(View.GONE);
                            findViewById(R.id.btn_login_cancel).setVisibility(View.GONE);


                        } else {
                            //display some message1 here
                            Toast.makeText(login.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });
    }

}