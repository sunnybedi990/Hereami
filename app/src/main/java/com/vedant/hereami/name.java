package com.vedant.hereami;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vedant.hereami.tracking.Main;

public class name extends Activity {
    private Button b1;
    public EditText nameedit;
    public String h;
    SharedPreferences sharedpreferences;
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        sharedpreferences = getSharedPreferences(mypreference123,
                Context.MODE_PRIVATE);

        b1 = (Button) findViewById(R.id.button4);
        nameedit = (EditText) findViewById(R.id.editText1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namedone();
            }
        });
    }

    public void namedone() {
        h = nameedit.getText().toString();
        sharedpreferences = getSharedPreferences(mypreference123,
                Context.MODE_PRIVATE);
        if (h.trim().length() > 0) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Pass, h);
            editor.commit();
            Toast.makeText(name.this, "Saved", Toast.LENGTH_LONG).show();
            Intent a = new Intent(name.this, Main.class);
            startActivity(a);

        } else {
            Toast.makeText(name.this, "Please Enter Name", Toast.LENGTH_LONG).show();
            // t1.setText("Please repeat the password");
        }
    }
}
