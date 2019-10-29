package com.aeonvn.driverapp.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

@IgnoreExtraProperties
public class Driver {
    private String fullname;
    private DriverLocation licenseplates;
    private String username;
    private String pass;

    public Driver() {
    }

    public Driver(String fullname, DriverLocation licenseplates, String username, String pass) {
        this.fullname = fullname;
        this.licenseplates = licenseplates;
        this.username = username;
        this.pass = pass;
    }

    public Driver(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null) {
            try {
                this.fullname = Objects.requireNonNull(documentSnapshot.get("fullname")).toString();
                this.username = Objects.requireNonNull(documentSnapshot.get("username")).toString();
                this.pass = Objects.requireNonNull(documentSnapshot.get("pass")).toString();
            } catch (Exception ignored) {
            }
        }
    }

    public void updateDriver(Driver driver){
        this.fullname = driver.getFullname();
        this.licenseplates = driver.getLicenseplates();
        this.username = driver.getUsername();
        this.pass = driver.getPass();
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public DriverLocation getLicenseplates() {
        return licenseplates;
    }

    public void setLicenseplates(DriverLocation licenseplates) {
        this.licenseplates = licenseplates;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
