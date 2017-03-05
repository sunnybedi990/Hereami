package com.vedant.hereami;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class userphoto extends Activity {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private FirebaseUser user;
    private String usermail;
    private CircleImageView propic;
    private static final String TAG = userphoto.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userphoto);
        firebaseAuth = FirebaseAuth.getInstance();
        //  textViewUserEmail = (TextView) findViewById(R.id.textView2);

        propic = (CircleImageView) findViewById(R.id.userPhoto);
        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        } else {
            //getting current user
            user = firebaseAuth.getCurrentUser();


            usermail = user.getEmail();
            if (user.getPhotoUrl() != null) {
                propic.setImageURI(user.getPhotoUrl());
            } else {
                Log.e(TAG, " no photo");
            }
        }

    }
}
