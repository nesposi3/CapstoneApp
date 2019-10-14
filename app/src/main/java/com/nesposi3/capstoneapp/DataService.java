package com.nesposi3.capstoneapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class DataService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG ="service";
    public DataService() {
    }
    @Override
    public void onNewToken(String token){
        Log.d(TAG, "onNewToken: ");
    }

}
