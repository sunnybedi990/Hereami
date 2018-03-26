package com.vedant.hereami.secureencryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vedant.hereami.R;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class testdata extends AppCompatActivity {
    String encrypted, decrypted;
    private String publicKeyBytesBase64;
    private String privateKeyBytesBase64;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testdata);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        final Button encrypt = (Button) findViewById(R.id.btn_encrypt);
        Button decrypt = (Button) findViewById(R.id.btn_decrypt);
        final TextView abcd = (TextView) findViewById(R.id.textView_decrypt);
        final EditText abcd2 = (EditText) findViewById(R.id.editText_encrpt);


        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestEncryptData(abcd2.getText().toString());

                encrypted = encryptRSAToString(abcd2.getText().toString(), publicKeyBytesBase64);
                abcd.setText(encrypted);
            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrypted = decryptRSAToString(encrypted, privateKeyBytesBase64);
                abcd.setText(decrypted);
            }
        });

    }

    public void TestEncryptData(String dataToEncrypt) {
        // generate a new public/private key pair to test with (note. you should only do this once and keep them!)
        KeyPair kp = getKeyPair();

        PublicKey publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();


        publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

        PrivateKey privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));
        SharedPreferences.Editor editor = sharedpreferences.edit();


        editor.putString("public key", publicKeyBytesBase64);
        editor.putString("private key", privateKeyBytesBase64);
        editor.apply();
        editor.commit();
        // test encryption


        // test decryption

    }

    public static KeyPair getKeyPair() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kp;
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

}
