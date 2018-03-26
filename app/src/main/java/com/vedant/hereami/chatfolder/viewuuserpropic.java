package com.vedant.hereami.chatfolder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedant.hereami.R;

import java.io.File;

public class viewuuserpropic extends AppCompatActivity {


    private File imgFile;
    private String filepath;
    private File directory;
    private byte[] filename;
    private String title;
    private String status;
    private TextView statusview;
    private String Imageuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuuserpropic);
        statusview = findViewById(R.id.statustextview);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        filename = bundle.getByteArray("image");
        title = bundle.getString("title");
        status = bundle.getString("status");
        Imageuri = bundle.getString("uri");
        //   Log.e("title", title);
        setTitle(title);
        statusview.setText(status);
        ImageView myImage = findViewById(R.id.userpropicimageview);
        File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/HereamI/" + title + "1.jpg");
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myImage.setImageBitmap(myBitmap);
        } else {
            myImage.setImageResource(R.drawable.headshot_7);
        }
        //  myImage.setImageBitmap(BitmapFactory.decodeFile(imgFile));
        //    myImage.setImageBitmap(BitmapFactory.decodeByteArray(filename, 0, filename.length));


    }

}


