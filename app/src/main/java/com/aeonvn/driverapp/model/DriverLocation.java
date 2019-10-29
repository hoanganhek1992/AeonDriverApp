package com.aeonvn.driverapp.model;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class DriverLocation {
    private String latitude;
    private String longitude;

    public DriverLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DriverLocation(DocumentSnapshot documentSnapshot) {
        {
            if (documentSnapshot != null) {
                try {
                    this.latitude = Objects.requireNonNull(documentSnapshot.get("latitude")).toString();
                    this.longitude = Objects.requireNonNull(documentSnapshot.get("longitude")).toString();
                } catch (Exception ignored) {

                }
            }
        }

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
