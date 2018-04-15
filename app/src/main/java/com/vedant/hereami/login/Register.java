package com.vedant.hereami.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;
import com.vedant.hereami.chatfolder.ReferenceUrl;


public class Register extends Activity implements View.OnClickListener {
    private static final String TAG = Register.class.getSimpleName();
    private EditText mUserFirstNameRegister;
    private EditText mUserEmailRegister;
    private EditText mUserPassWordRegister;
    private EditText mMobilenoRegister;
    private Button mRegisterButton;
    private Button mCancelRegister;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    FirebaseUser user;
    private Firebase mFireChatUsersRef;
    private Firebase mFireChatUsersRef1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Firebase.setAndroidContext(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        mUserEmailRegister = findViewById(R.id.userEmailRegister);
        mUserPassWordRegister = findViewById(R.id.passWordRegister);

        mRegisterButton = findViewById(R.id.registerButton);
        mCancelRegister = findViewById(R.id.cancelRegisterButton);
        mRegisterButton.setOnClickListener(Register.this);
        mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child("userkey");
        mFireChatUsersRef1 = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
        progressDialog = new ProgressDialog(this);


    }

    private void registerUser() {
        String email = mUserEmailRegister.getText().toString().trim();
        String password = mUserPassWordRegister.getText().toString().trim();

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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {

                            // Name, email address, and profile photo Url

                            finish();
                            //     Intent intent4 = new Intent(Register.this,Main.class).putExtra("first_name", Firstname);
                            //   startActivity(intent4);
                            Intent intent4 = new Intent(Register.this, phonenumber.class);
                            startActivity(intent4);
                            //     startActivity(new Intent(getApplicationContext(), Main.class));
                        } else {
                            //display some message1 here
                            Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterButton) {
            registerUser();
        }
    }


}
