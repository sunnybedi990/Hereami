package com.vedant.hereami;

import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.vedant.hereami.ReceiveSMS.mypreference;

public class TestActivity extends FragmentActivity implements GeoQueryEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraChangeListener, OnMapReadyCallback {
    private static final GeoLocation INITIAL_CENTER = new GeoLocation(23.0977, 72.5491);
    private static final int INITIAL_ZOOM_LEVEL = 5;
    private static final String GEO_FIRE_DB = "https://iamhere-29f2b.firebaseio.com";
    public String savedpass;
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/data";
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";
    public GoogleMap map;
   // public Circle searchCircle;
    public GeoFire geoFire;
    public GeoQuery geoQuery;
    private Map<String, Marker> markers;
    public Marker marker;
    public GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public SharedPreferences sharedpreferences;
    public String[] stockArr;
    public List<String> lst;
    PolylineOptions polylineOptions;
    public String message;


    @Override
    public void onMapReady(GoogleMap map1) {
        sharedpreferences = getSharedPreferences(mypreference123,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Pass)) {
            savedpass = (sharedpreferences.getString(Pass, ""));

        }
        this.map = map1;








        settingsrequest();



        geoFire.getLocation(message, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {


                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                    //if (marker != null) {
                   // searchCircle = map.addCircle(new CircleOptions().center(new LatLng(location.latitude,location.longitude)).radius(100000));
                    //searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
                    //searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
                    LatLng latLngCenter = new LatLng(location.latitude, location.longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
                    map.setOnCameraChangeListener(TestActivity.this);

                    map.setMyLocationEnabled(true);
                      //  marker.remove();
                    //}
                    //marker = map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(message));
                    //marker.setTag(0);
                   // System.out.println(marker.getId());
                    //markers.put(key, marker);
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
        //Log.d("LatlngCenter", latLngCenter.toString());
        polylineOptions = new PolylineOptions();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("key_position");
       Firebase.setAndroidContext(this);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        Firebase fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        fb_to_read.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                geoFire.getLocation(message, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location != null) {
                            if (key.equals(message)) {


                                TestActivity.this.animateMarkerTo(marker, location.latitude, location.longitude);
                                polylineOptions.add(new LatLng(location.latitude, location.longitude));
                                map.addPolyline(polylineOptions);
                                System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                                        if (marker != null) {

                                                     marker.remove();
                                                }
                                marker = map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(message));
                                marker.setTag(key);

                            }
                            // markers.put(key, marker);
                        } else {
                            System.out.println(String.format("There is no location for key %s in GeoFire", key));
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.err.println("There was an error getting the GeoFire location: " + databaseError);
                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        //FirebaseApp app = FirebaseApp.initializeApp(this, options);
       /* fb_to_read.addValueEventListener(new ValueEventListener(){


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void onDataChange(DataSnapshot result){
                lst = new ArrayList<String>(); // Result will be holded Here
                for(DataSnapshot dsp : result.getChildren()){
                    lst.add(String.valueOf(dsp.getKey())); //add result into array list
                    //    stockArr = new String[lst.size()];
                    //  stockArr = lst.toArray(stockArr);
                }}


        });*/
        // setup GeoFire
        this.geoFire = new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(GEO_FIRE_REF));
        // radius in km
        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 0.1);


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

    /* Class My Location Listener */
/*    public class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location loc) {

            //loc.getLatitude();
            //loc.getLongitude();

            //  Toast.make(context, data, Toast.LONG_LENGTH).show;

            geoFire.setLocation(savedpass, new GeoLocation(loc.getLatitude(), loc.getLongitude()), new GeoFire.CompletionListener() {
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
*/
    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        // Add a new marker to the map
        System.out.println(location.latitude);
        System.out.println(location.longitude);

        geoFire.getLocation(message, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {

                    polylineOptions.add(new LatLng(location.latitude, location.longitude));
                    map.addPolyline(polylineOptions);
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                    //if (marker != null) {

//                        marker.remove();
  //                  }
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(message));
                    marker.setTag(0);
                    System.out.println(marker.getId());
                    //markers.put(key, marker);
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });
        //Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
        //markers.put(key, marker);

    }


    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        System.out.println("Key is " + key);

        marker.getTag();
        this.marker.remove();
        //if (marker != null) {
        //   marker.remove();
        // this.markers.remove(key);
        //markers.clear();
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        //Marker marker = this.markers.get(key);
        //if (marker != null) {
        // this.animateMarkerTo(marker, location.latitude, location.longitude);
        //}



    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000 / Math.pow(2, zoomLevel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        this.geoQuery.removeAllListeners();
        //for (Marker marker: this.markers.values()) {
        //  marker.remove();
        //}
        //this.markers.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
        this.geoQuery.addGeoQueryEventListener(this);
    }

    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed / DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        Log.e(">>>>>radius",radius+"");
        //this.searchCircle.setCenter(center);
        //this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        if(radius/500 > 8){this.geoQuery.setRadius(radius/500);}
        else{
            Log.e(">>>>>radius nt ",radius/500 +"");
    }}

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
                            status.startResolutionForResult(TestActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Toast.makeText(TestActivity.this, "pass nai hua" + ".", Toast.LENGTH_LONG).show();

                        break;
                }
            }
        });
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == android.view.KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}

