package com.vedant.hereami.chatfolder;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.vedant.hereami.R;

import java.io.File;

public class viewuuserpropic extends AppCompatActivity {


    private File imgFile;
    private String filepath;
    private File directory;
    private byte[] filename;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuuserpropic);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        filename = bundle.getByteArray("image");
        title = bundle.getString("title");
        Log.e("title", title);
        setTitle(title);
        ImageView myImage = (ImageView) findViewById(R.id.userpropicimageview);

        myImage.setImageBitmap(BitmapFactory.decodeByteArray(filename, 0, filename.length));


    }

}


