package com.vedant.hereami.chatfolder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class viewcurrentuserprofile extends AppCompatActivity {
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
    private TextView statustxt;
    private TextView emailtxt;
    private TextView mobilenotxt;
    private String namestatus;
    private String statusstatus;
    private String username;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcurrentuserprofile);
        setTitle("Profile");
        dialog = ProgressDialog.show(viewcurrentuserprofile.this, "",
                "Loading. Please wait...", true);
        firebaseAuth = FirebaseAuth.getInstance();
        //  textViewUserEmail = (TextView) findViewById(R.id.textView2);

        //    imageView = (ImageView) findViewById(R.id.imgView);
        //    imageView.setImageResource(R.drawable.image);
        propic = (ImageView) findViewById(R.id.currentuserprofileimg);
        nametxt = (TextView) findViewById(R.id.nametxtview);
        statustxt = (TextView) findViewById(R.id.statustextview);
        mobilenotxt = (TextView) findViewById(R.id.mobilenotextview);
        emailtxt = (TextView) findViewById(R.id.emailtextview);

        statustxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(viewcurrentuserprofile.this, status.class);
                startActivity(a);
            }
        });

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
            myConnectionsStatusRef2 = mFireChatUsersRef.child(currentuser).child(ReferenceUrl.image);
            mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");

            mobilenotxt.setText(usermail);
            emailtxt.setText(username);

            previewStoredFirebaseImage();

            //     Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);


        }
    }


    private void previewStoredFirebaseImage() {


        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                Log.e("pic1", "comming");
                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {
                    Log.e("pic1", "cominggg");
                    Log.e("pic1", "okkkkkkk");

                    Log.e("pic1", "ok");
                    connectionstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.image).getValue().toString();
                    namestatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.name).getValue().toString();
                    statusstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.status).getValue().toString();
                    nametxt.setText(namestatus);
                    statustxt.setText(statusstatus);
                    Log.e("pic1", connectionstatus);
                    byte[] imageAsBytes = Base64.decode(connectionstatus.getBytes(), Base64.DEFAULT);
                    Log.e("pic90", String.valueOf(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
                    propic.setImageBitmap(
                            BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    dialog.dismiss();
                    System.out.println("Downloaded image with length: " + imageAsBytes.length);
                    propic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent s = new Intent(viewcurrentuserprofile.this, userphoto.class);
                            startActivity(s);
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


