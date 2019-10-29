package com.aeonvn.driverapp.prefsdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class PrefsDataLogin {

    private static final String PREFS_NAME = "aeon_driver_local_data";
    private static final String DRIVER_USERNAME = "driver_username";
    private static final String DRIVER_PASSWORD = "driver_password";

    private Context mContext;

    public PrefsDataLogin(Context context) {
        this.mContext = context;
    }

    public String getDriverUsername() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(DRIVER_USERNAME, "");
    }

    public String getDriverPassword() {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(DRIVER_PASSWORD, "");
    }

    public void saveDataLogin(String username, String password) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(DRIVER_USERNAME, username);
        editor.putString(DRIVER_PASSWORD, password);
        editor.apply();
        Log.e("LOG", "saveDataLogin");
    }
}
