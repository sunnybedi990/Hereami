package com.vedant.hereami.chatfolder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vedant.hereami.R;
import com.vedant.hereami.login.login;

import java.io.File;

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
    private StorageReference islandRef;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewcurrentuserprofile);
        setTitle("Profile");
        dialog = ProgressDialog.show(viewcurrentuserprofile.this, "",
                "Loading. Please wait...", true);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //  textViewUserEmail = (TextView) findViewById(R.id.textView2);

        //    imageView = (ImageView) findViewById(R.id.imgView);
        //    imageView.setImageResource(R.drawable.image);
        propic = findViewById(R.id.currentuserprofileimg);
        nametxt = findViewById(R.id.nametxtview);
        statustxt = findViewById(R.id.statustextview);
        mobilenotxt = findViewById(R.id.mobilenotextview);
        emailtxt = findViewById(R.id.emailtextview);



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
            storageReference = storage.getReferenceFromUrl(ReferenceUrl.FIREBASE_STORAGE_URL);
            mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
            myConnectionsStatusRef2 = mFireChatUsersRef.child(currentuser).child(ReferenceUrl.image);
            mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");

            mobilenotxt.setText(username);
            emailtxt.setText(usermail);

            previewStoredFirebaseImage();
            downloadFile();

            //     Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);


        }
    }



    private void previewStoredFirebaseImage() {


        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                //    Log.e("pic1", "comming");
                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {
                    //      Log.e("pic1", "cominggg");
                    //    Log.e("pic1", "okkkkkkk");

//                    Log.e("pic1", "ok");
                    connectionstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.image).getValue().toString();
                    namestatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.name).getValue().toString();
                    statusstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.status).getValue().toString();
                    nametxt.setText(namestatus);
                    statustxt.setText(statusstatus);
                    File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/HereamI/" + currentuser + "1.jpg");
                    if (imgFile.exists()) {

                        myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        propic.setImageBitmap(myBitmap);
                    } else {
                        propic.setImageResource(R.drawable.headshot_7);
                    }
                    //                  Log.e("pic1", connectionstatus);
                    //    byte[] imageAsBytes = Base64.decode(connectionstatus.getBytes(), Base64.DEFAULT);
                    //                Log.e("pic90", String.valueOf(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
                    //   propic.setImageBitmap(
                    //         BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    dialog.dismiss();
                    //              System.out.println("Downloaded image with length: " + imageAsBytes.length);
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

    private void downloadFile() {

        islandRef = storageReference.child("propic/" + currentuser + ".jpg");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "/HereamI/");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, currentuser + "1.jpg");
        Log.e("firebase12 ", ";local tem file created  created " + localFile.toString());
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ", ";local tem file created  created " + localFile.toString());

                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ", ";local tem file not created  created " + exception.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            //   LocationFound();
            Intent intent = new Intent(viewcurrentuserprofile.this, status.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}


