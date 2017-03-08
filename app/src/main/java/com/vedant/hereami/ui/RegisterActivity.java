package com.vedant.hereami.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vs.android.com.vs.FireChatHelper.ChatHelper;
import vs.android.com.vs.FireChatHelper.ReferenceUrl;
import vs.android.com.vs.R;

public class RegisterActivity extends Activity {

    private static final String TAG=RegisterActivity.class.getSimpleName();
    private EditText mUserFirstNameRegister;
    private EditText mUserEmailRegister;
    private EditText mUserPassWordRegister;
    private EditText mMobilenoRegister;
    private Button mRegisterButton;
    private Button mCancelRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide action bar
        this.getActionBar().hide();//Handel this carefully

        // Initialize
        mUserFirstNameRegister=(EditText)findViewById(R.id.userFirstNameRegister);
        mUserEmailRegister=(EditText)findViewById(R.id.userEmailRegister);
        mUserPassWordRegister=(EditText)findViewById(R.id.passWordRegister);
        mMobilenoRegister=(EditText)findViewById(R.id.usermobilenoRegister);
        mRegisterButton=(Button)findViewById(R.id.registerButton);
        mCancelRegister=(Button)findViewById(R.id.cancelRegisterButton);

        // Register user click listener
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* validate input text */

                // Get name, email and password
                String userFirstName=mUserFirstNameRegister.getText().toString();
                String userEmail=mUserEmailRegister.getText().toString();
                String userPassword=mUserPassWordRegister.getText().toString();
                String userMobileno=mMobilenoRegister.getText().toString();

                // Omit space
                userFirstName=userFirstName.trim();
                userEmail=userEmail.trim();
                userPassword=userPassword.trim();
                userMobileno=userMobileno.trim();
                if(userFirstName.isEmpty()||userEmail.isEmpty()||userPassword.isEmpty()||userMobileno.isEmpty()){
                    // Show message when field is empty
                    showErrorMessageToUser(getString(R.string.register_error_message));

                }else{

                    /* Create new user and allow user to log in if successfully created*/

                    // note from Firebase: Creating an account will not log that new account in
                    // so you have to log user in automatically when account is successfully created

                    final Firebase registerMChatUser = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL);  // Get app main firebase url
                    final String finalUserEmail = userEmail;
                    final String finalUserPassword = userPassword;
                    final String finalUserFirstName = userFirstName;
                    final String finalUserMobileno = userMobileno;

                    // Create new user
                    registerMChatUser.createUser(userEmail, userPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {

                            /* User registered successfully, so Log in automatically */

                            // Show a toast message for successfully registration
                            Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();

                            // Note from firebase: Creating an account will not log that new account in.
                            // Successfully created user account, and log the user in automatically

                            registerMChatUser.authWithPassword(finalUserEmail, finalUserPassword, new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {

                                    // Store user data necessary for the chat app

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(ReferenceUrl.KEY_PROVIDER, authData.getProvider()); // The authentication method used
                                    map.put(ReferenceUrl.KEY_FIRST_NAME, finalUserFirstName);   // User first name
                                    map.put(ReferenceUrl.KEY_USER_EMAIL, (String) authData.getProviderData().get(ReferenceUrl.KEY_EMAIL)); // User email address
                                    map.put(ReferenceUrl.CHILD_CONNECTION, ReferenceUrl.KEY_ONLINE);  // User status
                                    map.put(ReferenceUrl.KEY_AVATAR_ID, ChatHelper.generateRandomAvatarForUser()); // User avatar id
                                    map.put(ReferenceUrl.KEY_MOBILE_NO,finalUserMobileno); // user mobile no

                                   // Time user date is stored in database
                                    long createTime = new Date().getTime();
                                    map.put(ReferenceUrl.KEY_TIMESTAMP, String.valueOf(createTime)); // Timestamp is string type

                                    // Store user data in the path https://<YOUR-FIREBASE-APP>.firebaseio.com/users/<uid>,
                                    // where users/ is any arbitrary path to store user data, and <uid> represents the
                                    // unique id obtained from the authentication data
                                    registerMChatUser.child(ReferenceUrl.CHILD_USERS).child(authData.getUid()).setValue(map);


                                    // After storing, go to main activity
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    // There is an error, and close the screen
                                    Toast.makeText(RegisterActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            // There is an error in creating a user
                            //Log.e(TAG, "error creating user");
                            showErrorMessageToUser(firebaseError.getMessage());
                        }
                    });


                }


            }
        });

        // Cancel registration, and go to LogIn screen
        mCancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showErrorMessageToUser(String errorMessage){
        //Create an AlertDialog to show error message
        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(errorMessage)
                .setTitle(getString(R.string.login_error_title))
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
