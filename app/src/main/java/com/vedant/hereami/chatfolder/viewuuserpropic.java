package com.vedant.hereami.chatfolder;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuuserpropic);
        statusview = (TextView) findViewById(R.id.statustextview);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        filename = bundle.getByteArray("image");
        title = bundle.getString("title");
        status = bundle.getString("status");
        Log.e("title", title);
        setTitle(title);
        statusview.setText(status);
        ImageView myImage = (ImageView) findViewById(R.id.userpropicimageview);

        myImage.setImageBitmap(BitmapFactory.decodeByteArray(filename, 0, filename.length));


    }

}


