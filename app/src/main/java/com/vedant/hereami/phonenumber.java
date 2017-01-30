package com.vedant.hereami;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.vedant.hereami.CountryCodePicker.LIB_DEFAULT_COUNTRY_CODE;

public class phonenumber extends AppCompatActivity {
    View holderView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String useremail;
    public FirebaseDatabase database;
    public String countrycode;
    public EditText phoneno, editTextGetFullNumber;
    private Spinner county;

    public static final String EXTRA_INIT_TAB = "extraInitTab";

    int init = 0;
    boolean initLoaded = false;
    private Button btnsubmit;
    public String phonenoto;
    public String codecon;
    public Country selectedCountry;
    public CountryCodePicker codePicker;
    CountryCodePicker.Language customLanguage = CountryCodePicker.Language.ENGLISH;
    List<Country> preferredCountries;
    public int defaultCountryCode = LIB_DEFAULT_COUNTRY_CODE;
    private String getoa;
    CountryCodePicker.OnCountryChangeListener sun;
    private Country defaultCountry;
    CountryCodePicker ccp;
    private CountryCodePicker ccpGetNumber;
    private String getit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        // countrycode = (TextView) findViewById(R.id.textView_selectedCountry);
        editTextGetFullNumber = (EditText) findViewById(R.id.editTextGetFullNumber);

        Firebase.setAndroidContext(this);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        Firebase fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        // county = (Spinner) findViewById(R.id.spin_country);
        btnsubmit = (Button) findViewById(R.id.button3);
        useremail = user.getDisplayName();
        ccpGetNumber = (CountryCodePicker) findViewById(R.id.ccp);
        registerCarrierEditText();

        //   codecon = ccpGetNumber.getFullNumberWithPlus();
        // phonenoto = phoneno.getText().toString();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codecon = ccpGetNumber.getFullNumberWithPlus();
                if (useremail == null) {
                    //  Bundle bundle = getIntent().getExtras();
                    //  firstname = bundle.getString("first_name");

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(codecon).build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent4 = new Intent(phonenumber.this, Main.class);
                                        startActivity(intent4);
                                        //   Log.d(TAG, "User profile updated.");
                                        finish();

                                    }
                                }
                            });


                    //       phonenoto = phoneno.getText().toString();
                    //  Toast.makeText(phonenumber.this, codecon + phonenoto, Toast.LENGTH_LONG).show();
                    Log.e(">>>>asddddddd", phonenoto + "");
//                Toast.makeText(phonenumber.this, selectedCountry.getName(), Toast.LENGTH_LONG).show();
//                Log.e(">>>>selected", selectedCountry.getName() + "");
                    //              Log.e(">>>>selectedphonecode", selectedCountry.getPhoneCode() + "");
                    //            Log.e(">>>>selectednamecode", selectedCountry.getNameCode() + "");
                    //          Toast.makeText(phonenumber.this, getoa, Toast.LENGTH_LONG).show();
                    //          Log.e(">>>>codepicker", getoa + "");
                }
                Log.e(">>>>asddddddd", codecon + "");
            }
        });


    }

    private void registerCarrierEditText() {

        ccpGetNumber.registerCarrierNumberEditText(editTextGetFullNumber);
    }

}


