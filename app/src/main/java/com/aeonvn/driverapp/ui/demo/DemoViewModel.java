package com.aeonvn.driverapp.ui.demo;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.aeonvn.driverapp.services.DemoLocationUpdatesService;

public class DemoViewModel extends AndroidViewModel {

    private DemoLocationUpdatesService mService = null;
    private boolean mBound = false;

    public DemoViewModel(@NonNull Application application) {
        super(application);

        Intent intent = new Intent(getApplication(), DemoLocationUpdatesService.class);
        getApplication().bindService(intent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }


    public void onStartSendClick() {
        if (mService != null)
            mService.onSendLocation();
    }

    public void showNotificationWhenBackground() {
        if (mBound) {
            getApplication().unbindService(mServiceConnection);
            mBound = false;
        }
    }

    public void onStopSendClick() {
        if (mService != null)
            mService.onStopSendLocation();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DemoLocationUpdatesService.LocalBinder binder = (DemoLocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
}
