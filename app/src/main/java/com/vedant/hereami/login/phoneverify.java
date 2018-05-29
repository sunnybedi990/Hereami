package com.vedant.hereami.login;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vedant.hereami.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class phoneverify extends AppCompatActivity {


    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60 * 60 * 1000;
    private static final String TAG = "phoneverify";
    private static String uniqueIdentifier = null;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth firebaseAuth;

    private String phoneNumber;
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;

    private EditText phoneNum;
    private EditText verifyCodeET;

    private FirebaseFirestore firestoreDB;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneverify);

        sendCodeButton = findViewById(R.id.send_code_b);
        verifyCodeButton = findViewById(R.id.verify_code_b);
        signOutButton = findViewById(R.id.auth_logout_b);

        phoneNum = findViewById(R.id.phone);
        verifyCodeET = findViewById(R.id.phone_auth_code);

        addOnClickListeners();

        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();

        createCallback();
        getInstallationIdentifier();
        getVerificationDataFromFirestoreAndVerify(null);
    }

    private void addOnClickListeners() {
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumberInit();
            }
        });
        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhoneNumberCode();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void createCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "verification completed" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "verification failed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneNum.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(phoneverify.this,
                            "Trying too many timeS",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "code sent " + verificationId);


                addVerificationDataToFirestore(phoneNumber, verificationId);
            }
        };
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNum.setError("Invalid phone number.");
            return false;
        }
        return true;
    }

    private void verifyPhoneNumberInit() {
        phoneNumber = phoneNum.getText().toString();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }
        verifyPhoneNumber(phoneNumber);

    }

    private void verifyPhoneNumber(String phno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phno, 70,
                TimeUnit.SECONDS, this, callbacks);
    }

    private void verifyPhoneNumberCode() {
        final String phone_code = verifyCodeET.getText().toString();
        getVerificationDataFromFirestoreAndVerify(phone_code);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "code verified signIn successful");
                            firebaseUser = task.getResult().getUser();
                            showSingInButtons();
                        } else {
                            Log.w(TAG, "code verification failed", task.getException());
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                verifyCodeET.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private void createCredentialSignIn(String verificationId, String verifyCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.
                getCredential(verificationId, verifyCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signOut() {
        firebaseAuth.signOut();
        showSendCodeButton();
    }

    private void addVerificationDataToFirestore(String phone, String verificationId) {
        Map verifyMap = new HashMap();
        verifyMap.put("phone", phone);
        verifyMap.put("verificationId", verificationId);
        verifyMap.put("timestamp", System.currentTimeMillis());

        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .set(verifyMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "phone auth info added to db ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding phone auth info", e);
                    }
                });
    }

    private void getVerificationDataFromFirestoreAndVerify(final String code) {
        initButtons();
        firestoreDB.collection("phoneAuth").document(uniqueIdentifier)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            if (ds.exists()) {
                                disableSendCodeButton(ds.getLong("timestamp"));
                                if (code != null) {
                                    createCredentialSignIn(ds.getString("verificationId"),
                                            code);
                                } else {
                                    verifyPhoneNumber(ds.getString("phone"));
                                }
                            } else {
                                showSendCodeButton();
                                Log.d(TAG, "Code hasn't been sent yet");
                            }

                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    public synchronized void getInstallationIdentifier() {
        if (uniqueIdentifier == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueIdentifier = sharedPrefs.getString(UNIQUE_ID, null);
            if (uniqueIdentifier == null) {
                uniqueIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(UNIQUE_ID, uniqueIdentifier);
                editor.commit();
            }
        }
    }

    private void disableSendCodeButton(long codeSentTimestamp) {
        long timeElapsed = System.currentTimeMillis() - codeSentTimestamp;
        if (timeElapsed > ONE_HOUR_MILLI) {
            showSendCodeButton();
        } else {
            findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
            findViewById(R.id.phone_auth_code_items).setVisibility(View.VISIBLE);
            findViewById(R.id.logout_items).setVisibility(View.GONE);
        }
    }

    private void showSendCodeButton() {
        findViewById(R.id.phone_auth_items).setVisibility(View.VISIBLE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        findViewById(R.id.logout_items).setVisibility(View.GONE);
    }

    private void showSingInButtons() {
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        findViewById(R.id.logout_items).setVisibility(View.VISIBLE);
    }

    private void initButtons() {
        findViewById(R.id.phone_auth_items).setVisibility(View.GONE);
        findViewById(R.id.phone_auth_code_items).setVisibility(View.GONE);
        findViewById(R.id.logout_items).setVisibility(View.GONE);
    }
}
