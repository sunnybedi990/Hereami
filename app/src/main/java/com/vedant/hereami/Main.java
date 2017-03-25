package com.vedant.hereami;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vedant.hereami.chatfolder.Recentnotification;
import com.vedant.hereami.chatfolder.ReferenceUrl;
import com.vedant.hereami.chatfolder.recentchat;
import com.vedant.hereami.firebasepushnotification.ActivitySendPushNotification;
import com.vedant.hereami.firebasepushnotification.MainActivity5;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Main extends RuntimePermissionsActivity {
    public Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
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
    private ValueEventListener mConnectedListener;
    private Firebase mFirebaseChatRef;
    private Firebase myConnectionsStatusRef;
    private Firebase mFireChatUsersRef;
    private CoordinatorLayout coordinatorLayout1;
    private Firebase myConnectionsStatusRef1;
    private String tsTemp;
    private int hour;
    private int minutes;
    private String timest;
    private Calendar calendar;
    private String tsTemp1;
    private Calendar calendar1;
    private int hour1;
    private int minutes1;
    private Firebase myConnectionsStatusRef2;
    private String currentuser;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //      mCurrentLocale = locale;
        setContentView(R.layout.activity_main2);
        Firebase.setAndroidContext(this);
        coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayoutmain);
        firebaseAuth = FirebaseAuth.getInstance();

        textViewUserEmail = (TextView) findViewById(R.id.textView2);
        mFirebaseChatRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL);
        mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);


        // set up rules for Daylight Saving Time
        //      pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        //     pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        TimeZone pdt = TimeZone.getDefault();
        calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();

        hour1 = calendar1.get(Calendar.HOUR);
        minutes1 = calendar1.get(Calendar.MINUTE);
        final int ampm = calendar1.get(Calendar.AM_PM);
        tsTemp1 = String.format("%02d:%02d", hour1, minutes1);


        if (!isInternetOn()) {


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout1, "Check Your internet connection", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        }


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

            startService(new Intent(this, Recentnotification.class));
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
                System.out.println("Main.onCreate: " + FirebaseInstanceId.getInstance().getToken());
                connectionstatus();
                currentuser = usermail.replace(".", "dot") + user.getDisplayName();
                token = FirebaseInstanceId.getInstance().getToken();
                //     ProviderQueryResult s = firebaseAuth.fetchProvidersForEmail("sunnybedi990@gmail.com").getResult();
                //     System.out.println("getdata: " + s);

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

            b6 = (Button) findViewById(R.id.btn_propic);
            b7 = (Button) findViewById(R.id.btn_recent);
            b8 = (Button) findViewById(R.id.btn_regis);


            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, ListActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, Sendlocation.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });

            b6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, MainActivity5.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
            b7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, recentchat.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
            b8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this, ActivitySendPushNotification.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
        }

    }


    public void onPermissionsGranted(final int requestCode) {
        // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public void re() {
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
            //    tsTemp = String.format("%02d:%02d", hour, minutes);
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    public void connectionstatus() {

        myConnectionsStatusRef = mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child(ReferenceUrl.CHILD_CONNECTION);
        myConnectionsStatusRef1 = mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child(ReferenceUrl.timestamp);
        myConnectionsStatusRef2 = mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child(ReferenceUrl.devicetokenid);
        mConnectedListener = mFirebaseChatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();

                if (connected) {


                    Thread t = new Thread() {

                        @Override
                        public void run() {
                            try {
                                while (!isInterrupted()) {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TimeZone pdt = TimeZone.getDefault();
                                            calendar = new GregorianCalendar(pdt);
                                            Date trialTime = new Date();
                                            calendar.setTime(trialTime);
                                            Date now = new Date();

                                            hour = calendar.get(Calendar.HOUR);
                                            minutes = calendar.get(Calendar.MINUTE);
                                            final int ampm = calendar.get(Calendar.AM_PM);
                                            tsTemp = String.format("%02d:%02d", hour, minutes);

                                            myConnectionsStatusRef1.onDisconnect().setValue(tsTemp);
                                            //tsTemp.add(tsTemp1);
                                            Log.e("ts14", String.valueOf(tsTemp));
                                            // update TextView here!
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    };

                    t.start();

                    //   String s = tsTemp;

                    myConnectionsStatusRef2.setValue(token);
                    myConnectionsStatusRef1.setValue(tsTemp1);
                    myConnectionsStatusRef.setValue(ReferenceUrl.KEY_ONLINE);

                    // When this device disconnects, remove it
                    //    String s = tsTemp.toString();
                    //    Log.e("sssssss", s);

                    myConnectionsStatusRef.onDisconnect().setValue(ReferenceUrl.KEY_OFFLINE);
                    //    myConnectionsStatusRef1.onDisconnect().setValue(tsTemp);
                    Toast.makeText(Main.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Main.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


}

