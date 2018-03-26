package com.vedant.hereami.chatfolder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private TextView statusview, number, name;
    private String mobile;
    private Bitmap myBitmap;
    private ImageView myImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuuserpropic);
        statusview = findViewById(R.id.statustextview);
        number = findViewById(R.id.textview_mobileno);
        name = findViewById(R.id.textview_nameuser);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;

        filename = bundle.getByteArray("image");
        title = bundle.getString("title");
        status = bundle.getString("status");
        mobile = bundle.getString("number");
        //   Log.e("title", title);
        setTitle(title);
        statusview.setText(status);
        name.setText(title);
        number.setText(mobile);
        myImage = findViewById(R.id.userpropicimageview);
        File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/HereamI/" + title + "1.jpg");
        if (imgFile.exists()) {

            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myImage.setImageBitmap(myBitmap);
        } else {
            myImage.setImageResource(R.drawable.headshot_7);
        }
        //  myImage.setImageBitmap(BitmapFactory.decodeFile(imgFile));
        //    myImage.setImageBitmap(BitmapFactory.decodeByteArray(filename, 0, filename.length));
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

    }

    public void showImage() {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);

        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setTitle(title);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(myBitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT));

        builder.show();
    }

}


