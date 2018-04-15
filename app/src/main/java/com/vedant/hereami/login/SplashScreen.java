package com.vedant.hereami.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sinch.android.rtc.SinchError;
import com.vedant.hereami.BuildConfig;
import com.vedant.hereami.R;
import com.vedant.hereami.ViewPager.TabWOIconActivity;
import com.vedant.hereami.chatfolder.AppUtill;
import com.vedant.hereami.chatfolder.BadgeUtils;
import com.vedant.hereami.voip.BaseActivity;
import com.vedant.hereami.voip.SinchService;

public class SplashScreen extends BaseActivity implements SinchService.StartFailedListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private TextView mWelcomeTextView;
    private static final String LOADING_PHRASE_CONFIG_KEY = "splash_screen";
    private ProgressDialog mSpinner;
    private String message1;
    private String checkFlag;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startHeavyProcessing();
        mWelcomeTextView = findViewById(R.id.textView5);
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        fetchWelcome();


        bundle = getIntent().getExtras();

        AppUtill.savePreferenceLong("NOTICOUNT", 0, this);
        BadgeUtils.clearBadge(this);
    }

    @Override
    public void onServiceConnected() {
        //    mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {

    }
    private void startHeavyProcessing() {
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "whatever result you have";
        }

        @Override
        protected void onPostExecute(String result) {
            if (firebaseAuth.getCurrentUser() == null) {
                //closing this activity
                finish();
                //starting login activity
                startActivity(new Intent(SplashScreen.this, login.class));
            } else {
                //getting current user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                getSinchServiceInterface().startClient(user.getEmail().replace(".", "dot") + user.getDisplayName());

            }
            if (bundle != null) {
                message1 = bundle.getString("callid");
                //  Intent intent = getIntent();
                checkFlag = bundle.getString("flag");
                if (checkFlag != null) {
                    if (checkFlag.equals("A")) {
                        finish();

       /*           Intent intent = new Intent(SplashScreen.this, IncomingCallScreenActivity.class);
                    intent.putExtra("CALL_ID", message1);
              //           intent.putExtra(LOCATION, call.getHeaders().get("location"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 //   intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
              //      Intent intent1 = new Intent(SplashScreen.this,SinchService.class);
                //    startService(intent1);
                */

                    }
                } else {
                    Intent i = new Intent(SplashScreen.this, TabWOIconActivity.class);
                    i.putExtra("data", result);
                    startActivity(i);
                    finish();
                }
            } else {
                Intent i = new Intent(SplashScreen.this, TabWOIconActivity.class);
                i.putExtra("data", result);
                startActivity(i);
                finish();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void fetchWelcome() {
        mWelcomeTextView.setText(mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //  Toast.makeText(MainActivity.this, "Fetch Succeeded",
                            //        Toast.LENGTH_SHORT).show();

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            // Toast.makeText(MainActivity.this, "Fetch Failed",
                            //         Toast.LENGTH_SHORT).show();
                        }
                        //  displayWelcomeMessage();
                    }
                });
        // [END fetch_config_with_callback]
    }
}
