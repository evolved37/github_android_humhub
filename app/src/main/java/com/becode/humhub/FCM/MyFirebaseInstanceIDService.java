package com.becode.humhub.FCM;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    SharedPreferences preferences;
    private static final String TAG = "MyFirebaseIIDService";


    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        storeRegistrationId(this, refreshedToken);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use FCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void storeRegistrationId(Context mContext, String token) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fcm_token", token);
        editor.commit();

    }



}