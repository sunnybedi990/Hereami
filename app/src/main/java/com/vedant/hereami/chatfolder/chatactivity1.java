package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;
import com.vedant.hereami.login;

import java.util.ArrayList;
import java.util.List;




/*
* CAUTION: This app is still far away from a production app
* Note: (1) Still fixing some code, and functionality and
*       I don't use FirebaseUI, but recommend you to use it.
*       (2) remember to add your own firabse url in ReferenceUrl.java
* */

public class chatactivity1 extends Activity {


    //https://github.com/sinch/android-messaging-tutorial
    //http://stackoverflow.com/questions/32151178/how-do-you-include-a-username-when-storing-email-and-password-using-firebase-ba

    /*
    * Question: how to query all users except the current user
    * http://stackoverflow.com/questions/25236576/firebase-displaying-other-users-username-except-yours-using-presence
    *
    * https://www.airpair.com/angularjs/posts/build-a-real-time-hybrid-app-with-ionic-firebase
    * */

    private static final String TAG = chatactivity1.class.getSimpleName();

    /* Reference to firebase */
    private Firebase mFirebaseChatRef;

    /* Reference to users in firebase */
    private Firebase mFireChatUsersRef;

    /* Updating connection status */
    Firebase myConnectionsStatusRef;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* recyclerView for mchat users */
    private RecyclerView mUsersFireChatRecyclerView;

    /* progress bar */
    private View mProgressBarForUsers;

    /* fire chat adapter */
    private UsersChatAdapter mUsersChatAdapter;

    /* current user uid */
    private String mCurrentUserUid;

    /* current user email */
    private String mCurrentUserEmail;

    /* Listen to users change in firebase-remember to detach it */
    private ChildEventListener mListenerUsers;

    /* Listen for user presence */
    private ValueEventListener mConnectedListener;

    /* List holding user key */
    private List<String> mUsersKeyList;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Firebase.setAndroidContext(this);


        // Initialize firebase
        name = user.getDisplayName();
        mCurrentUserEmail = user.getEmail();
        // Initialize keys list

        mUsersKeyList = new ArrayList<String>();
        queryFireChatUsers();
        // Listen for changes in the authentication state
        // Because probably token expire after 24hrs or
        // user log out
    }


    private void queryFireChatUsers() {

        //Show progress bar
        showProgressBarForUsers();

        mListenerUsers = mFireChatUsersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //Log.e(TAG, "inside onChildAdded");
                //Hide progress bar
                hideProgressBarForUsers();

                if (dataSnapshot.exists()) {
                    //Log.e(TAG, "A new user was inserted");

                    String userUid = dataSnapshot.getKey();

                    if (!userUid.equals(mCurrentUserUid)) {

                        //Get recipient user name
                        UsersChatModel user = dataSnapshot.getValue(UsersChatModel.class);

                        //Add recipient uid
                        user.setRecipientUid(userUid);

                        //Add current user (or sender) info
                        user.setCurrentUserEmail(mCurrentUserEmail); //email
                        user.setCurrentUserUid(mCurrentUserUid);//uid
                        mUsersKeyList.add(userUid);
                        mUsersChatAdapter.refill(user);


                    } else {
                        UsersChatModel currentUser = dataSnapshot.getValue(UsersChatModel.class);
                        String userName = currentUser.getFirstName(); //Get current user first name
                        String createdAt = currentUser.getCreatedAt(); //Get current user date creation
                        mUsersChatAdapter.setNameAndCreatedAt(userName, createdAt); //Add it the adapter
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if (!userUid.equals(mCurrentUserUid)) {
                        UsersChatModel user = dataSnapshot.getValue(UsersChatModel.class);

                        // Removed bug here
                        //Add recipient uid
                        user.setRecipientUid(userUid);

                        //Add current user (or sender) info
                        user.setCurrentUserEmail(mCurrentUserEmail); //email
                        user.setCurrentUserUid(mCurrentUserUid);//uid
                        int index = mUsersKeyList.indexOf(userUid);
                        Log.e(TAG, "change index " + index);
                        mUsersChatAdapter.changeUser(index, user);
                    }

                }


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        // // Store current user status as online
        myConnectionsStatusRef = mFireChatUsersRef.child(mCurrentUserUid).child(ReferenceUrl.CHILD_CONNECTION);

        // Indication of connection status
        mConnectedListener = mFirebaseChatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {

                    myConnectionsStatusRef.setValue(ReferenceUrl.KEY_ONLINE);

                    // When this device disconnects, remove it
                    myConnectionsStatusRef.onDisconnect().setValue(ReferenceUrl.KEY_OFFLINE);
                    Toast.makeText(chatactivity1.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(chatactivity1.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    private void navigateToLogin() {

        // Go to LogIn screen
        Intent intent = new Intent(this, login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //int size=mUsersKeyList.size();
        //Log.e(TAG, " size"+size);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //Log.e(TAG, "I am onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.e(TAG, "I am onPause");
    }

    @Override
    protected void onStop() {

        super.onStop();
        //Log.e(TAG, "I am onStop");
    }

    @Override
    protected void onNewIntent(Intent intent) {


        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        //Log.e(TAG, "I am onDestroy");

        // If changing configurations, stop tracking firebase session.
        mFirebaseChatRef.removeAuthStateListener(mAuthStateListener);

        mUsersKeyList.clear();

        // Stop all listeners
        // Make sure to check if they have been initialized
        if (mListenerUsers != null) {
            mFireChatUsersRef.removeEventListener(mListenerUsers);
        }
        if (mConnectedListener != null) {
            mFirebaseChatRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        }
    }


    private void logout() {

        if (this.mAuthData != null) {

            /* Logout of mChat */

            // Store current user status as offline
            myConnectionsStatusRef.setValue(ReferenceUrl.KEY_OFFLINE);

            // Finish token
            mFirebaseChatRef.unauth();

            /* Update authenticated user and show login screen */
            //   setAuthenticatedUser(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        } else if (item.getItemId() == R.id.action_user) {
            Intent i = new Intent(this, chatactivity.class);

            startActivity(i);
            return true;
        } else {

        }

        return super.onOptionsItemSelected(item);
    }


    /*Show and hide progress bar*/
    private void showProgressBarForUsers() {
        mProgressBarForUsers.setVisibility(View.VISIBLE);
    }


    private void hideProgressBarForUsers() {
        if (mProgressBarForUsers.getVisibility() == View.VISIBLE) {
            mProgressBarForUsers.setVisibility(View.GONE);
        }
    }

}
