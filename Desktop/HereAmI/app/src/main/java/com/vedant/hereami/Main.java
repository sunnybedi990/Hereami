package com.vedant.hereami;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends  RuntimePermissionsActivity {
    Button b1;
    private static final int REQUEST_PERMISSIONS = 5;
    public SharedPreferences sharedpreferences;
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";
    String savedpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedpreferences = getSharedPreferences(mypreference123,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Pass)) {
            savedpass = (sharedpreferences.getString(Pass, ""));


            re();
            b1 =(Button)findViewById(R.id.btn);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
        else{
            Intent b = new Intent(Main.this,name.class);
            startActivity(b);
        }
    }

    public void onPermissionsGranted(final int requestCode) {
       // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }
    public void re(){
        Main.super.requestAppPermissions(new
                        String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.SEND_SMS}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);

    }
}
