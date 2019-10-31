package com.aeonvn.driverapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aeonvn.driverapp.helper.MyHelper;

public class StickyService extends Service {
    private static final String TAG = "StickyService";


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        /*Check application is open?
         * If not, open application.
         * If skill open: return;
         */
        if (!MyHelper.isAppRunning(getApplicationContext(), "com.aeonvn.driverapp")) {
            // App not running
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.aeonvn.driverapp");
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        sendBroadcast(new Intent("YouWillNeverKillMe"));
    }
}
