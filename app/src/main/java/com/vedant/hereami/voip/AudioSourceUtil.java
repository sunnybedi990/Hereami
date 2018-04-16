package com.vedant.hereami.voip;

import android.media.AudioManager;

public class AudioSourceUtil {
    private static void reset(AudioManager audioManager) {
        if (audioManager != null) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.stopBluetoothSco();
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
            audioManager.setWiredHeadsetOn(false);
        }
    }

    public static void connectEarpiece(AudioManager audioManager) {
        reset(audioManager);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    public static void connectSpeaker(AudioManager audioManager) {
        reset(audioManager);
        audioManager.setSpeakerphoneOn(true);
    }

    public static void connectHeadphones(AudioManager audioManager) {
        reset(audioManager);
        audioManager.setWiredHeadsetOn(true);
    }

    public static void connectBluetooth(AudioManager audioManager) {
        reset(audioManager);
    }
}
