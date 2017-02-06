package com.vedant.hereami;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends Activity implements View.OnClickListener {
    private EditText mUserEmail;
    private EditText mUserPassWord;
    private Button mLoginToMChat;
    private Button mRegisterUser;
    private FirebaseAuth firebaseAuth;


    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Main.class));
        }

        //initializing views
        mUserEmail = (EditText) findViewById(R.id.userEmailChat);
        mUserPassWord = (EditText) findViewById(R.id.passWordChat);
        mLoginToMChat = (Button) findViewById(R.id.btn_LogInChat);
        mRegisterUser = (Button) findViewById(R.id.registerUser);


        progressDialog = new ProgressDialog(this);
        mLoginToMChat.setOnClickListener(login.this);
        mRegisterUser.setOnClickListener(login.this);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginToMChat) {
            userLogin();
        }

        if (view == mRegisterUser) {
            finish();
            startActivity(new Intent(this, Register.class));
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
                            finish();
                            startActivity(new Intent(getApplicationContext(), Main.class));
                        }
                    }
                });

    }


}
