package com.vedant.hereami.Fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.SinchClient;
import com.vedant.hereami.R;
import com.vedant.hereami.chatfolder.ReferenceUrl;
import com.vedant.hereami.chatfolder.viewcurrentuserprofile;
import com.vedant.hereami.database.DBHelper;
import com.vedant.hereami.database.messagedatabse;
import com.vedant.hereami.database.saverecentmessage;
import com.vedant.hereami.firebasepushnotification.EndPoints;
import com.vedant.hereami.firebasepushnotification.MyVolley;
import com.vedant.hereami.login.login;
import com.vedant.hereami.login.phonenumber;
import com.vedant.hereami.tracking.ListActivity;
import com.vedant.hereami.tracking.Sendlocation;
import com.vedant.hereami.voip.Activesinchservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Cipher;

import static com.vedant.hereami.firebasepushnotification.EndPoints.URL_REGISTER_DEVICE;

//import com.vedant.hereami.chatfolder.DirectReplyActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {
    public Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private static final int REQUEST_PERMISSIONS = 5;
    public SharedPreferences sharedpreferences;
    public static final String mypreference123 = "mypref123";
    public static final String Pass = "password";
    String savedpass;
    private TextView textViewUserEmail;
    public String firstname;
    public HashMap<String, String> hashMap;
    public HashMap<String, String> hashMap1;
    public Firebase fb_to_read;
    private FirebaseAuth firebaseAuth;
    public String name3;
    private String usermail;
    private Locale mCurrentLocale;
    private FirebaseUser user;
    private ValueEventListener mConnectedListener;
    private Firebase mFirebaseChatRef;
    private Firebase myConnectionsStatusRef;
    private Firebase mFireChatUsersRef;
    private CoordinatorLayout coordinatorLayout1;
    private Firebase myConnectionsStatusRef1;
    private String tsTemp;
    private int hour;
    private int minutes;
    private String timest;
    private Calendar calendar;
    private String tsTemp1;
    private Calendar calendar1;
    private int hour1;
    private int minutes1;
    private String currentuser;
    private String token;
    public Thread t;

    private String publicKeyBytesBase64;
    private String privateKeyBytesBase64;
    public static final String mypreference1 = "privatekey";
    public static final String mypreference = "publickey";
    private ProgressDialog mSpinner;
    private SinchClient mSinchClient;
    private static final String mypreference2 = "username";
    private SharedPreferences prefs;
    private String publickey;
    private Firebase mFirebaseMessagesChatconnectioncheck;
    private Firebase mFireChatUsersRef1;
    String messa, message1, timestamp, sender, tendigitnumber;
    private messagedatabse mydb;
    private DBHelper mydb1;
    private String contactmatch;
    private String tendigitnumber1;
    private List<String> lst1;


    public CallsFragment() {
        // Required empty public constructor
    }

    public static String getLastThree(String myString) {
        if (myString.length() > 10)
            return myString.substring(myString.length() - 10);
        else
            return myString;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    //    listView = (ListView) getActivity().findViewById(R.id.listview_chatmain1);
        // listView = getListView(); //EX:
      //  listView.setTextFilterEnabled(true);
      //  registerForContextMenu(listView);
      //  super.onActivityCreated(savedInstanceState);
        Firebase.setAndroidContext(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseMessagesChatconnectioncheck = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child("userkey");
        mFirebaseChatRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL);
        mFireChatUsersRef = new Firebase(ReferenceUrl.FIREBASE_CHAT_URL).child(ReferenceUrl.CHILD_USERS);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        lst1 = new ArrayList<String>();
        final int a;
        lst1.clear();
        // getdata();
        hashMap = new HashMap<>();
        hashMap1 = new HashMap<>();
        prefs = getActivity().getSharedPreferences(mypreference123, Context.MODE_PRIVATE);

        // set up rules for Daylight Saving Time
        //      pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        //     pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        TimeZone pdt = TimeZone.getTimeZone("UTC");
        calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();
        int date1 = calendar1.get(Calendar.DATE);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
        minutes1 = calendar1.get(Calendar.MINUTE);
        final int ampm = calendar1.get(Calendar.AM_PM);
        tsTemp1 = String.format("%02d:%02d", hour1, minutes1) + "%" + date1 + "/" + month1 + "/" + year1;



        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing getActivity() activity
            getActivity().finish();
            //starting login activity
            startActivity(new Intent(getActivity(), login.class));
        } else {
            //getting current user
            user = firebaseAuth.getCurrentUser();

            usermail = user.getEmail();
            name3 = user.getDisplayName();
            if (name3 == null) {
                //      Bundle bundle = getIntent().getExtras();
                //    firstname = bundle.getString("first_name");

        /*        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(firstname).build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //   Log.d(TAG, "User profile updated.");
                                    // finish();

                                }
                            }
                        });
                        */

                Intent intent4 = new Intent(getActivity(), phonenumber.class);
                startActivity(intent4);
                //   textViewUserEmail.setText("Welcome " + firstname);
            } else {
//                textViewUserEmail.setText("Welcome " + usermail);
                System.out.println("Main.onCreate: " + FirebaseInstanceId.getInstance().getToken());

/*                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(mypreference123, Context.MODE_PRIVATE);

                String encrytionprivatekey = sharedpreferences.getString("privatekey", "");
                String encrytionprivatekey1 = sharedpreferences.getString("publickey", "");

                mFirebaseMessagesChatconnectioncheck.child(currentuser).child("publickey").setValue(encrytionprivatekey1);
              //  mFireChatUsersRef.child(usermail1).child("Publickey").setValue(publicKeyBytesBase64);
                mFirebaseMessagesChatconnectioncheck.child(currentuser).child("privatekey").setValue(encrytionprivatekey);
                */
                connectionstatus();
                keys();
                currentuser = usermail.replace(".", "dot") + user.getDisplayName();
                token = FirebaseInstanceId.getInstance().getToken();
                sendTokenToServer();
                getActivity().startService(new Intent(getActivity(), Activesinchservice.class));
                //     ProviderQueryResult s = firebaseAuth.fetchProvidersForEmail("sunnybedi990@gmail.com").getResult();
                //     System.out.println("getdata: " + s);
                mydb = new messagedatabse(getActivity());
                mydb1 = new DBHelper(getContext());

            }

            //initializing views


            //displaying logged in user name


            //adding listener to button





            //    if (sharedpreferences.contains(Pass)) {
            //      savedpass = (sharedpreferences.getString(Pass, ""));


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        //  listView = (ListView) view.findViewById(R.id.listview_chatmain1);
        b1 = view.findViewById(R.id.button6);
        b2 = view.findViewById(R.id.button2);
        b3 = view.findViewById(R.id.button7);
        b4 = view.findViewById(R.id.button8);
        coordinatorLayout1 = view.findViewById(R.id
                .coordinatorLayoutmain);


        //    textViewUserEmail = (TextView) view.findViewById(R.id.textView2);

        //    mCustomSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ListActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Sendlocation.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), saverecentmessage.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), viewcurrentuserprofile.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        /*
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), testdata.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), chatmain.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
*/
        if (!isInternetOn()) {


            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout1, "Check Your internet connection", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        }
        //    view = inflater.inflate(android.R.layout.list_content, null);
        //   listView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }


    public void connectionstatus() {

        myConnectionsStatusRef = mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child(ReferenceUrl.CHILD_CONNECTION);
        myConnectionsStatusRef1 = mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child(ReferenceUrl.timestamp);

        mConnectedListener = mFirebaseChatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();

                if (connected) {


                    t = new Thread() {

                        @Override
                        public void run() {
                            try {
                                while (!isInterrupted()) {
                                    Thread.sleep(1000);
                                    if (getActivity() == null)
                                        return;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TimeZone pdt = TimeZone.getTimeZone("UTC");
                                            calendar = new GregorianCalendar(pdt);
                                            Date trialTime = new Date();
                                            calendar.setTime(trialTime);
                                            Date now = new Date();
                                            int date = calendar.get(Calendar.DATE);
                                            int month = calendar.get(Calendar.MONTH);
                                            int year = calendar.get(Calendar.YEAR);
                                            hour = calendar.get(Calendar.HOUR_OF_DAY);
                                            minutes = calendar.get(Calendar.MINUTE);
                                            final int ampm = calendar.get(Calendar.AM_PM);
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                                            String test = sdf.format(calendar.getTime());
                                            tsTemp = String.format("%02d:%02d", hour, minutes) + "%" + date + "/" + month + "/" + year;

                                            myConnectionsStatusRef1.onDisconnect().setValue(tsTemp);
                                            //tsTemp.add(tsTemp1);
                                            //    Log.e("ts14", String.valueOf(tsTemp));
                                            // update TextView here!
                                        }
                                    });
                                }
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    };

                    t.start();

                    //   String s = tsTemp;


                    myConnectionsStatusRef1.setValue(tsTemp1);
                    myConnectionsStatusRef.setValue(ReferenceUrl.KEY_ONLINE);


                    // When getActivity() device disconnects, remove it
                    //    String s = tsTemp.toString();
                    //    Log.e("sssssss", s);

                    myConnectionsStatusRef.onDisconnect().setValue(ReferenceUrl.KEY_OFFLINE);
                    //    myConnectionsStatusRef1.onDisconnect().setValue(tsTemp);
                    Toast.makeText(getActivity(), "Connected to Firebase", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getActivity(), "Disconnected from Firebase", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            Toast.makeText(getActivity(), " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(getActivity(), " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    private void sendTokenToServer() {
        //  progressDialog = new ProgressDialog(getActivity());
        //  progressDialog.setMessage("Registering Device...");
        //  progressDialog.show();


        System.out.println("tokennnnn " + token);

        final String email = user.getEmail();

        if (token == null) {

            Toast.makeText(getActivity(), "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }
        String urldefined = URL_REGISTER_DEVICE + "?id=" + name3;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE + "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", name3);
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public static String reverseIt(String source) {
        int i, len = source.length();
        StringBuilder dest = new StringBuilder(len);

        for (i = (len - 1); i >= 0; i--) {
            dest.append(source.charAt(i));
        }

        return dest.toString();
    }



    public static String encryptRSAToString(String clearText, String publicKey) {
        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");
    }

    public static String decryptRSAToString(String encryptedBase64, String privateKey) {

        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }

    private void keys() {
        usermail = user.getEmail().replace(".", "dot") + user.getDisplayName();


        mFirebaseMessagesChatconnectioncheck.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {

                for (DataSnapshot connectionchild : dataSnapshot1.getChildren()) {
                    if (connectionchild.getKey().contains(usermail)) {


                        if (dataSnapshot1.child(usermail).child("publickey").exists()) {
                            publickey = dataSnapshot1.child(usermail).child("publickey").getValue().toString();
                            String privatekey = dataSnapshot1.child(usermail).child("privatekey").getValue().toString();
                            Log.e("horaha hai pub", publickey);
                            Log.e("horaha hai pri", privatekey);
                            sharedpreferences = getActivity().getSharedPreferences(mypreference123,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            if (!sharedpreferences.contains(mypreference)) {
                                //    mFireChatUsersRef.child(usermail.replace(".", "dot") + name3).child("Publickey").setValue(publicKeyBytesBase64);
                                //   mFireChatUsersRef.setValue(publicKeyBytesBase64);
                                editor.putString("publickey", publickey);
                                editor.apply();
                                editor.commit();
                            }
                            if (!sharedpreferences.contains(mypreference1)) {
                                editor.putString("privatekey", privatekey);
                                editor.apply();
                                editor.commit();

                            }
                            if (!sharedpreferences.contains(mypreference2)) {
                                editor.putString("username", usermail);
                                editor.apply();
                                editor.commit();
                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    private void getip() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.IP_GET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //                    progressDialog.dismiss();

                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), viewcurrentuserprofile.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.testdata) {
            getip();
            Intent intent = new Intent(getActivity(), saverecentmessage.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            sharedpreferences = getActivity().getSharedPreferences(mypreference123,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            mydb.deletedatabse();
            mydb1.deletedatabse();

            if (!sharedpreferences.contains(mypreference123)) {
                firebaseAuth.signOut();
                getActivity().finish();
                //starting login activity
                startActivity(new Intent(getActivity(), login.class));
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void getdata() {

        fb_to_read.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void onDataChange(DataSnapshot result) {
                // Result will be holded Here

                for (DataSnapshot dsp : result.getChildren()) {
                    String keyname = String.valueOf(dsp.getKey()).replace("+", ":");

                    String[] parts = keyname.split(":"); // escape .
                    String part1 = parts[0];
                    String part2 = parts[1];
                    tendigitnumber1 = getLastThree(part2);

                    hashMap1.put(tendigitnumber1, keyname.replace(":", "+"));

                    long sun = result.getChildrenCount();
                    if (lst1.size() > sun) {
                        lst1.clear();
                    } else {

                        contactmatch = getContactDisplayNameByNumber(tendigitnumber1);
                        if (!contactmatch.equals("?")) {
                            lst1.add(contactmatch);
                        }

                    }

                }

                for (int i = 0; i < lst1.size(); i++) {


                    ArrayList<String> arrTblNames = new ArrayList<String>();
                    SQLiteDatabase db = mydb1.getReadableDatabase();
                    Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

                    if (c.moveToFirst()) {
                        while (!c.isAfterLast()) {
                            arrTblNames.add(c.getString(c.getColumnIndex("name")));
                            c.moveToNext();
                        }
                    }
                    if (!arrTblNames.contains("recentchat")) {
                        Log.e("name", lst1.get(i));
                        Log.e("username", hashMap1.get(hashMap.get(lst1.get(i))));
                        Log.e("phone", tendigitnumber1);
                        //            if (mydb1.insertContact(lst1.get(i), hashMap1.get(hashMap.get(lst1.get(i))), tendigitnumber1)) {
                        //            Toast.makeText(getContext(), "inserted", Toast.LENGTH_LONG).show();
                        //      }
                        //  } else {
                        //          if (mydb1.updateContact(lst1.get(i), hashMap1.get(hashMap.get(lst1.get(i))), tendigitnumber1)) {
                        Toast.makeText(getContext(), "updated", Toast.LENGTH_LONG).show();
                        //    }
                    }
                }

            }


        });
    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                hashMap.put(name, number);
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }


}
