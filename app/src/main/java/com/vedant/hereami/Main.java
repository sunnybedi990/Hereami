package com.vedant.hereami;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.chatfolder.chatmain;
import com.vedant.hereami.chatfolder.recentchat;

import java.util.Locale;

public class Main extends  RuntimePermissionsActivity {
    public Button b1, b2, b3, b4, b5, b6, b7;
    private static final int REQUEST_PERMISSIONS = 5;
    public SharedPreferences sharedpreferences;
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";
    String savedpass;
    private TextView textViewUserEmail;
    public String firstname;

    private FirebaseAuth firebaseAuth;
    public String name3;
    private String usermail;
    private Locale mCurrentLocale;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //      mCurrentLocale = locale;
        setContentView(R.layout.activity_main2);

        firebaseAuth = FirebaseAuth.getInstance();
        textViewUserEmail = (TextView) findViewById(R.id.textView2);

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
            name3 = user.getDisplayName();
            if (name3 == null) {
                //      Bundle bundle = getIntent().getExtras();
                //    firstname = bundle.getString("first_name");

        /*        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(firstname).build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //   Log.d(TAG, "User profile updated.");
                                    // finish();

                                }
                            }
                        });
                        */

                Intent intent4 = new Intent(this, phonenumber.class);
                startActivity(intent4);
                //   textViewUserEmail.setText("Welcome " + firstname);
            } else {
                textViewUserEmail.setText("Welcome " + usermail);

            }

            //initializing views


            //displaying logged in user name


            //adding listener to button


            sharedpreferences = getSharedPreferences(mypreference123,
                    Context.MODE_PRIVATE);
            //    if (sharedpreferences.contains(Pass)) {
            //      savedpass = (sharedpreferences.getString(Pass, ""));


            re();
            b1 = (Button) findViewById(R.id.btn);
            b2 = (Button) findViewById(R.id.btn_list);
            b3 = (Button) findViewById(R.id.button2);
            b4 = (Button) findViewById(R.id.btn_chat);
            b5 = (Button) findViewById(R.id.btn_one);
            b6 = (Button) findViewById(R.id.btn_propic);
            b7 = (Button) findViewById(R.id.btn_recent);


            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, MainActivity.class);
                    startActivity(intent1);
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, ListActivity.class);
                    startActivity(intent1);
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, Sendlocation.class);
                    startActivity(intent1);
                }
            });
            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, chat.class);
                    startActivity(intent1);
                }
            });
            //  } else {
            //      Intent b = new Intent(Main.this, name.class);
            //      startActivity(b);
            // }
            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, chatmain.class);
                    startActivity(intent1);
                }
            });
            b6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, userphoto.class);
                    startActivity(intent1);
                }
            });
            b7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, recentchat.class);
                    startActivity(intent1);
                }
            });
        }

        }


    public void onPermissionsGranted(final int requestCode) {
       // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }
    public void re(){
        Main.super.requestAppPermissions(new
                        String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LocationFound();
            return true;
        }
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void LocationFound() {
        Intent intent = new Intent(this, name.class);
        startActivity(intent);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}
