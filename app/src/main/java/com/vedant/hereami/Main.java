package com.vedant.hereami;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends  RuntimePermissionsActivity {
    Button b1,b2,b3;
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
            b2 =(Button)findViewById(R.id.btn_list);
            b3 =(Button)findViewById(R.id.button2);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Main.this,MainActivity.class);
                startActivity(intent1);
            }
        });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this,ListActivity.class);
                    startActivity(intent1);
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(Main.this,Sendlocation.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LocationFound();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void LocationFound() {
        Intent intent = new Intent(this, name.class);
        startActivity(intent);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}
