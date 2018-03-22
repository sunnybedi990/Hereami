package com.vedant.hereami.chatfolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vedant.hereami.R;
import com.vedant.hereami.login.login;

public class status extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private FirebaseUser user;
    private String usermail;
    private ImageView propic;
    private static final String TAG = userphoto.class.getSimpleName();
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private String picturePath;
    private Button mRegisterButton;
    private Firebase myConnectionsStatusRef2;
    private Firebase mFireChatUsersRef;
    private Firebase mFirebaseMessagesChatconnectioncheck;
    private String connectionstatus, connectionstatus1;
    private String currentuser;
    private TextView nametxt;
    private EditText statustxt;

    private String username;
    private Button sendbutton;
    private Button cancellbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        firebaseAuth = FirebaseAuth.getInstance();
        statustxt = (EditText) findViewById(R.id.status_edittext);
        sendbutton = (Button) findViewById(R.id.btn_status_send);
        cancellbutton = (Button) findViewById(R.id.btn_status_cancel);

        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        } else {
            //getting current user
            user = firebaseAuth.getCurrentUser();
            usermail = user.getEmail();
            username = user.getDisplayName();
            currentuser = usermail.replace(".", "dot") + username;
            Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
            mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
            myConnectionsStatusRef2 = mFireChatUsersRef.child(currentuser).child(ReferenceUrl.status);
            mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");


            changestatus();

        }

    }

    private void changestatus() {

        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {


            private String statusstatus;

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {
                //    Log.e("pic1", "comming");
                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {


                    statusstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.status).getValue().toString();

                    statustxt.setText(statusstatus);
                    sendbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myConnectionsStatusRef2.setValue(statustxt.getText().toString());
                            finish();
                        }
                    });
                    cancellbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();


                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });
    }
}
