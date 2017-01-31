package com.vedant.hereami;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.vedant.hereami.code.Country;
import com.vedant.hereami.code.CountryCodePicker;

import java.util.List;

import static com.vedant.hereami.code.CountryCodePicker.LIB_DEFAULT_COUNTRY_CODE;

public class phonenumber extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String GEO_FIRE_DB = "https://iamhere-29f2b.firebaseio.com";
    public String savedpass;
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/data";
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";
    public GoogleMap map;
    public Circle searchCircle;
    public GeoFire geoFire;
    public GeoQuery geoQuery;
    public GoogleApiClient mGoogleApiClient;
    View holderView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String useremail;
    public FirebaseDatabase database;
    public String countrycode;
    public EditText phoneno, editTextGetFullNumber;
    private Spinner county;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
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
    private String useremailaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);
        settingsrequest();

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
        useremailaddress = user.getEmail();
        useremail = user.getDisplayName();
        ccpGetNumber = (CountryCodePicker) findViewById(R.id.ccp);
        registerCarrierEditText();

        //   codecon = ccpGetNumber.getFullNumberWithPlus();
        // phonenoto = phoneno.getText().toString();

        this.geoFire = new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(GEO_FIRE_REF));
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
                                        firsttimeregister();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location loc) {

            //loc.getLatitude();
            //loc.getLongitude();

            //  Toast.make(context, data, Toast.LONG_LENGTH).show;

       /*     geoFire.setLocation(mail + name1, new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }

            });
        */
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    public void firsttimeregister() {
        geoFire.setLocation(useremailaddress.replace(".", "dot") + codecon, new GeoLocation(23.0977, 72.5491), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }

        });
    }


    public void settingsrequest() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(phonenumber.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Toast.makeText(phonenumber.this, "pass nai hua" + ".", Toast.LENGTH_LONG).show();

                        break;
                }
            }
        });
    }

}


