package com.aeonvn.driverapp;

import android.app.Application;

import com.aeonvn.driverapp.firebase.MyFirebase;
import com.aeonvn.driverapp.model.Driver;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyApplication extends Application {

    static MyApplication myApplication;

    private Driver mDriver;

    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Driver getmDriver() {
        return mDriver;
    }

    public void setmDriver(Driver mDriver) {
        this.mDriver = mDriver;
    }
}
