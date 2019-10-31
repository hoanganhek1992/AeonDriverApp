package com.aeonvn.driverapp.ui.home;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.aeonvn.driverapp.MyApplication;
import com.aeonvn.driverapp.firebase.MyFirebase;
import com.aeonvn.driverapp.model.Driver;
import com.aeonvn.driverapp.model.DriverLocation;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends AndroidViewModel {

    private final String TAG = "TAG";
    //public final ObservableField<String> driverName = new ObservableField<>();
    public final ObservableField<String> logDistance = new ObservableField<>();

    private MyApplication myApplication;
    private MyFirebase myFirebase;
    private Driver mDriver;
    private Location oldLocation;

    String str_log = "";

    public HomeViewModel(@NonNull Application application) {
        super(application);
        myFirebase = MyFirebase.getInstance(FirebaseFirestore.getInstance());
        myApplication = MyApplication.getInstance();
        mDriver = myApplication.getmDriver();
    }

    void onLocationUpdate(Location location) {
        str_log += "Location: " + location.getLatitude() + " " + location.getLongitude() + "\n";
        logDistance.set(str_log);
        Log.e(TAG, "onLocationUpdate: " + location.getLatitude() + " " + location.getLongitude());
        if (oldLocation != null && isLocationValid(oldLocation, location) && mDriver != null) {
            mDriver.setLicenseplates(new DriverLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
            myFirebase.updateDriverLocation(myApplication.getmDriver(), new MyFirebase.UpdateLocationCallback() {
                @Override
                public void onUpdateLocationSuccess() {
                    Log.e(TAG, "onUpdateLocationSuccess: ");
                }
            });
            oldLocation = location;
        }
        if (oldLocation == null) {
            oldLocation = location;
        }
    }

    private boolean isLocationValid(Location location, Location location2) {
        Log.e(TAG, "isLocationValid: " + location.distanceTo(location2));
        str_log += "Distance: " + location.distanceTo(location2) + " m \n";
        logDistance.set(str_log);
        //return location.distanceTo(location2) > 100;
        return true;
    }
}
