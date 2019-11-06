package com.aeonvn.driverapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aeonvn.driverapp.model.Driver;
import com.aeonvn.driverapp.model.DriverLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MyFirebase {

    private static final String DRIVER_TABLE = "Driver";
    private static final String LOCATION_TABLE = "Location";

    private FirebaseFirestore mDatabase;

    private static MyFirebase myFirebase;

    public static MyFirebase getInstance(FirebaseFirestore mDatabase) {
        if (myFirebase == null) {
            myFirebase = new MyFirebase(mDatabase);
        }
        return myFirebase;
    }

    private MyFirebase(FirebaseFirestore mDatabase) {
        this.mDatabase = mDatabase;
    }

    public void updateDriverLocation(Driver driver, final UpdateLocationCallback callback) {
        mDatabase.collection(LOCATION_TABLE).document(driver.getUsername()).set(driver.getLicenseplates()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onUpdateLocationSuccess();
            }
        });
    }

    public void updateDemoLocation(DriverLocation location, final UpdateLocationCallback callback) {
        mDatabase.collection(LOCATION_TABLE).document("123456").set(location).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onUpdateLocationSuccess();
            }
        });
    }

    public void getDriverInformation(String username, final GetDriverDataCallback callback) {
        DocumentReference docRef = mDatabase.collection(DRIVER_TABLE).document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(task.getResult()).exists()) {
                        Driver driver = new Driver(task.getResult());
                        callback.onGetDriverSuccess(driver);
                    } else {
                        Log.d("LOG", "No such document");
                        callback.onGetDriverSuccess(null);
                    }
                } else {
                    Log.d("LOG", "get failed with ", task.getException());
                    callback.onGetDriverError(task.getException());
                }
            }
        });
    }

    /*public void addNewDriver(Driver driver) {
        mDatabase.collection(DRIVER_TABLE).document("123456").set(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }*/

    public interface UpdateLocationCallback {

        void onUpdateLocationSuccess();

    }

    public interface GetDriverDataCallback {
        void onGetDriverSuccess(Driver driver);

        void onGetDriverError(Exception exception);
    }

}
