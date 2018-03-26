package com.vedant.hereami.chatfolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vedant.hereami.R;
import com.vedant.hereami.login.login;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class userphoto extends Activity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private FirebaseUser user;
    private String usermail;
    private CircleImageView propic;
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
    private Uri selectedImage;
    private StorageReference storageReference;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userphoto);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://iamhere-29f2b.appspot.com");    //change the url according to your firebase app
        //  textViewUserEmail = (TextView) findViewById(R.id.textView2);
        mRegisterButton = findViewById(R.id.button5);
        mRegisterButton.setOnClickListener(userphoto.this);
        //    imageView = (ImageView) findViewById(R.id.imgView);
        //    imageView.setImageResource(R.drawable.image);
        propic = findViewById(R.id.userPhoto);

        //   propic.setImageResource(R.drawable.headshot_7);
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
            currentuser = usermail.replace(".", "dot") + user.getDisplayName();
            //     previewStoredFirebaseImage();

            Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com");
            mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
            myConnectionsStatusRef2 = mFireChatUsersRef.child(usermail.replace(".", "dot") + user.getDisplayName()).child(ReferenceUrl.image);
            mFirebaseMessagesChatconnectioncheck = fb_parent.child("/users");
            previewStoredFirebaseImage();
            //     Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);

            propic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                //       Log.e("pic2", String.valueOf(filePathColumn));
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                //       Log.e("pic1", String.valueOf(picturePath));

                propic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void userpropic() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        // we finally have our base64 string version of the image, save it.
        myConnectionsStatusRef2.setValue(base64Image);
        //    System.out.println("Stored image with length: " + bytes.length);
        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show();
    }

    @Override

    public void onClick(View v) {
        if (v == mRegisterButton) {
            if (picturePath != null)
            userpropic();
            uploadFile();
        }
    }

    private void previewStoredFirebaseImage() {


        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                //           Log.e("pic1", "comming");
                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {
                    //               Log.e("pic1", "cominggg");
                    //               Log.e("pic1", "okkkkkkk");

                    //               Log.e("pic1", "ok");
                    connectionstatus = dataSnapshot1.child(currentuser).child(ReferenceUrl.image).getValue().toString();

                    //               Log.e("pic1", connectionstatus);
                    byte[] imageAsBytes = Base64.decode(connectionstatus.getBytes(), Base64.DEFAULT);
                    //               Log.e("pic90", String.valueOf(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)));
                    propic.setImageBitmap(
                            BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    //               System.out.println("Downloaded image with length: " + imageAsBytes.length);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });
    }

    private void uploadFile() {
        //if there is a file to upload
        if (selectedImage != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            StorageReference riversRef = storageReference.child("propic/" + currentuser + ".jpg");
            riversRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
}


