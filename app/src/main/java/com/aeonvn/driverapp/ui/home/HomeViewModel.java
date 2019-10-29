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
    public final ObservableField<String> driverName = new ObservableField<>();

    MyApplication myApplication;
    MyFirebase myFirebase;
    Driver mDriver;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        myFirebase = MyFirebase.getInstance(FirebaseFirestore.getInstance());
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
        driverName.set(myApplication.getmDriver().getFullname());
        mDriver = myApplication.getmDriver();
    }

    public void onLocationUpdate(Location location) {
        Log.e(TAG, "onLocationUpdate: " + location.getLatitude() + " " + location.getLongitude());
        /*mDriver.setLicenseplates(new DriverLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
        myFirebase.updateDriverLocation(myApplication.getmDriver(), new MyFirebase.UpdateLocationCallback() {
            @Override
            public void onUpdateLocationSuccess() {
                Log.e(TAG, "onUpdateLocationSuccess: ");
            }
        });*/
    }

}
