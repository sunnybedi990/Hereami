package com.vedant.hereami.secureencryption;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;

import com.vedant.hereami.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class BrokenKeyDerivationActivity extends Activity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static SecretKey deriveKeyInsecurely(String password, int keySizeInBytes) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(
                InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, keySizeInBytes),
                "AES");
    }

    /**
     * Example use of a key derivation function, derivating a key securely from a password.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private SecretKey deriveKeySecurely(String password, int keySizeInBytes) {
        // Use this to derive the key from the password:
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), retrieveSalt(),
                100 /* iterationCount */, keySizeInBytes * 8 /* key size in bits */);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Deal with exceptions properly!", e);
        }
    }

    /**
     * Retrieve encrypted data using a password. If data is stored with an insecure key, re-encrypt
     * with a secure key.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String retrieveData(String password) {
        String decryptedString;
        if (isDataStoredWithInsecureKey()) {
            SecretKey insecureKey = deriveKeyInsecurely(password, KEY_SIZE);
            byte[] decryptedData = decryptData(retrieveEncryptedData(), retrieveIv(), insecureKey);
            SecretKey secureKey = deriveKeySecurely(password, KEY_SIZE);
            storeDataEncryptedWithSecureKey(encryptData(decryptedData, retrieveIv(), secureKey));
            decryptedString = "Warning: data was encrypted with insecure key\n"
                    + new String(decryptedData, StandardCharsets.UTF_8);
        } else {
            SecretKey secureKey = deriveKeySecurely(password, KEY_SIZE);
            byte[] decryptedData = decryptData(retrieveEncryptedData(), retrieveIv(), secureKey);
            decryptedString = "Great!: data was encrypted with secure key\n"
                    + new String(decryptedData, StandardCharsets.UTF_8);
        }
        return decryptedString;
    }
    /*
     ***********************************************************************************************
     * The essential point of this example are the three methods above. Everything below this
     * comment just gives a concrete example of usage and defines mock methods.
     ***********************************************************************************************
     */

    /**
     * Retrieves encrypted data twice and displays the results.
     * <p>
     * The mock data is encrypted with an insecure key (see {@link #cleanRoomStart()}) and so the
     * first time {@link #retrieveData(String)} reencrypts it and returns the plain text with a
     * warning message. The second time, as the data is properly encrypted, the plain text is
     * returned with a congratulations message.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove any files from previous executions of this app and initialize mock encrypted data.
        // Just so that the application has the same behaviour every time is run. You don't need to
        // do this in your app.
        cleanRoomStart();
        // Set the layout for this activity.  You can find it
        // in res/layout/brokenkeyderivation_activity.xml
        View view = getLayoutInflater().inflate(R.layout.brokenkeyderivation_activity, null);
        setContentView(view);
        // Find the text editor view inside the layout.
        EditText mEditor = (EditText) findViewById(R.id.text);
        String password = "unguessable";
        String firstResult = retrieveData(password);
        String secondResult = retrieveData(password);
        mEditor.setText("First result: " + firstResult + "\nSecond result: " + secondResult);
    }

    private static byte[] encryptOrDecrypt(
            byte[] data, SecretKey key, byte[] iv, boolean isEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key,
                    new IvParameterSpec(iv));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("This is unconceivable!", e);
        }
    }

    private static byte[] encryptData(byte[] data, byte[] iv, SecretKey key) {
        return encryptOrDecrypt(data, key, iv, true);
    }

    private static byte[] decryptData(byte[] data, byte[] iv, SecretKey key) {
        return encryptOrDecrypt(data, key, iv, false);
    }

    /**
     * Remove any files from previous executions of this app and initialize mock encrypted data.
     * <p>
     * <p>Just so that the application has the same behaviour every time is run. You don't need to
     * do this in your app.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void cleanRoomStart() {
        removeFile("salt");
        removeFile("iv");
        removeFile(SECURE_ENCRYPTION_INDICATOR_FILE_NAME);
        // Mock initial data
        encryptedData = encryptData(
                "I hope it helped!".getBytes(), retrieveIv(),
                deriveKeyInsecurely("unguessable", KEY_SIZE));
    }

    /*
     ***********************************************************************************************
     * Everything below this comment is a succession of mocks that would rarely interest someone on
     * Earth. They are merely intended to make the example self contained.
     ***********************************************************************************************
     */
    private boolean isDataStoredWithInsecureKey() {
        // Your app should have a way to tell whether the data has been re-encrypted in a secure
        // fashion, in this mock we use the existence of a file with a certain name to indicate
        // that.
        return !fileExists("encrypted_with_secure_key");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] retrieveIv() {
        byte[] iv = new byte[IV_SIZE];
        // Ideally your data should have been encrypted with a random iv. This creates a random iv
        // if not present, in order to encrypt our mock data.
        readFromFileOrCreateRandom("iv", iv);
        return iv;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] retrieveSalt() {
        // Salt must be at least the same size as the key.
        byte[] salt = new byte[KEY_SIZE];
        // Create a random salt if encrypting for the first time, and save it for future use.
        readFromFileOrCreateRandom("salt", salt);
        return salt;
    }

    private byte[] encryptedData = null;

    private byte[] retrieveEncryptedData() {
        return encryptedData;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeDataEncryptedWithSecureKey(byte[] encryptedData) {
        // Mock implementation.
        this.encryptedData = encryptedData;
        writeToFile(SECURE_ENCRYPTION_INDICATOR_FILE_NAME, new byte[1]);
    }

    /**
     * Read from file or return random bytes in the given array.
     * <p>
     * <p>Save to file if file didn't exist.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readFromFileOrCreateRandom(String fileName, byte[] bytes) {
        if (fileExists(fileName)) {
            readBytesFromFile(fileName, bytes);
            return;
        }
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        writeToFile(fileName, bytes);
    }

    private boolean fileExists(String fileName) {
        File file = new File(getFilesDir(), fileName);
        return file.exists();
    }

    private void removeFile(String fileName) {
        File file = new File(getFilesDir(), fileName);
        file.delete();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeToFile(String fileName, byte[] bytes) {
        try (FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to " + fileName, e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readBytesFromFile(String fileName, byte[] bytes) {
        try (FileInputStream fis = openFileInput(fileName)) {
            int numBytes = 0;
            while (numBytes < bytes.length) {
                int n = fis.read(bytes, numBytes, bytes.length - numBytes);
                if (n <= 0) {
                    throw new RuntimeException("Couldn't read from " + fileName);
                }
                numBytes += n;
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read from " + fileName, e);
        }
    }

    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 32;
    private static final String SECURE_ENCRYPTION_INDICATOR_FILE_NAME =
            "encrypted_with_secure_key";
}
