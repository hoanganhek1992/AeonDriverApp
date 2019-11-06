package com.aeonvn.driverapp.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.aeonvn.driverapp.R;
import com.aeonvn.driverapp.firebase.MyFirebase;
import com.aeonvn.driverapp.model.DriverLocation;
import com.aeonvn.driverapp.ui.demo.DemoActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DemoLocationUpdatesService extends Service {

    private static final String TAG = DemoLocationUpdatesService.class.getSimpleName();
    private static final String CHANNEL_ID = "demo_location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = TAG +
            ".started_from_notification";
    private final IBinder mBinder = new LocalBinder();

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 456;
    private boolean mChangingConfiguration = false;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;

    private Handler mServiceHandler;

    private MyFirebase myFirebase;
    List<DriverLocation> mList;
    private boolean isIncrease = true;
    private int position = 0;

    public DemoLocationUpdatesService() {
    }

    @Override
    public void onCreate() {

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
        mList = new ArrayList<>();
        addListLocation();
        myFirebase = MyFirebase.getInstance(FirebaseFirestore.getInstance());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);
        if (startedFromNotification) {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy services");
        mServiceHandler.removeCallbacksAndMessages(null);
        sendDataHandler.removeCallbacks(sendDataRunnable);

    }


    private Notification getNotification() {
        Intent intent = new Intent(this, DemoLocationUpdatesService.class);
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DemoActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(R.drawable.ic_launcher_background, getString(R.string.location_updating),
                        activityPendingIntent)
                .setContentText("Demo Location")
                .setContentTitle(Utils.getLocationTitle(this))
                .setContentIntent(activityPendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Demo Location")
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    public class LocalBinder extends Binder {
        public DemoLocationUpdatesService getService() {
            return DemoLocationUpdatesService.this;
        }
    }

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onSendLocation() {
        sendDataHandler.post(sendDataRunnable);

        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }


    public void onStopSendLocation() {
        sendDataHandler.removeCallbacks(sendDataRunnable);

        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    private void addListLocation() {
        mList.clear();
        mList.add(new DriverLocation("10.786150", "106.665983"));
        mList.add(new DriverLocation("10.786869", "106.664590"));
        mList.add(new DriverLocation("10.785815", "106.663624"));
        mList.add(new DriverLocation("10.785056", "106.662927"));
        mList.add(new DriverLocation("10.784076", "106.662036"));
        mList.add(new DriverLocation("10.782507", "106.660502"));
        mList.add(new DriverLocation("10.780621", "106.658717"));
        mList.add(new DriverLocation("10.779361", "106.657549"));
        mList.add(new DriverLocation("10.777997", "106.656180"));
        mList.add(new DriverLocation("10.778520", "106.655873"));
        mList.add(new DriverLocation("10.780080", "106.655422"));
        mList.add(new DriverLocation("10.781746", "106.654980"));
        mList.add(new DriverLocation("10.782934", "106.654610"));
        mList.add(new DriverLocation("10.784538", "106.654159"));
        mList.add(new DriverLocation("10.787507", "106.653284"));
        mList.add(new DriverLocation("10.789138", "106.652806"));
        mList.add(new DriverLocation("10.790210", "106.652427"));
        mList.add(new DriverLocation("10.791663", "106.652770"));
        mList.add(new DriverLocation("10.792886", "106.653365"));
        mList.add(new DriverLocation("10.794100", "106.654448"));
        mList.add(new DriverLocation("10.795395", "106.655697"));
        mList.add(new DriverLocation("10.796529", "106.654646"));
        mList.add(new DriverLocation("10.796073", "106.654182"));
        mList.add(new DriverLocation("10.795666", "106.653756"));
    }

    private Handler sendDataHandler = new Handler();
    private Runnable sendDataRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "Update new location (demo):"
                    + mList.get(position).getLatitude()
                    + " " + mList.get(position).getLongitude());
            myFirebase.updateDemoLocation(mList.get(position), new MyFirebase.UpdateLocationCallback() {
                @Override
                public void onUpdateLocationSuccess() {
                    Log.e(TAG, "onUpdateLocationSuccess");
                }
            });
            if (isIncrease) {
                // location di chuyển tới
                Log.e(TAG, "Di chuyển chiều đi");
                position++;
                if (position >= mList.size()) {
                    isIncrease = false;
                    position = (mList.size() - 1);
                }
            } else {
                // location di chuyền lùi
                Log.e(TAG, "Di chuyển chiều về");
                position--;
                if (position < 0) {
                    isIncrease = true;
                    position = 0;
                }
            }

            sendDataHandler.postDelayed(sendDataRunnable, UPDATE_INTERVAL_IN_MILLISECONDS);
        }
    };
}
