package com.vedant.hereami.voip;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.sinch.android.rtc.SinchError;

public class Activesinchservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // startActivity(service.class);
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //  startTimer();
        return START_STICKY;
    }

    public abstract class service extends BaseActivity implements SinchService.StartFailedListener {


        private ProgressDialog mSpinner;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


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
            openPlaceCallActivity();
        }

        private void loginClicked() {
            String userName = "sunny";
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
                return;
            }

            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(userName);
                showSpinner();
            } else {
                openPlaceCallActivity();
            }
        }

        private void openPlaceCallActivity() {
            //    Intent mainActivity = new Intent(this, PlaceCallActivity.class);
            //  startActivity(mainActivity);
        }

        private void showSpinner() {
            mSpinner = new ProgressDialog(this);
            mSpinner.setTitle("Logging in");
            mSpinner.setMessage("Please wait...");
            mSpinner.show();
        }
    }
}

