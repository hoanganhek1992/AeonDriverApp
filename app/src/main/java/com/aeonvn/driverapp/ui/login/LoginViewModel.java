package com.aeonvn.driverapp.ui.login;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.aeonvn.driverapp.MyApplication;
import com.aeonvn.driverapp.R;
import com.aeonvn.driverapp.firebase.MyFirebase;
import com.aeonvn.driverapp.model.Driver;
import com.aeonvn.driverapp.prefsdata.PrefsDataLogin;
import com.aeonvn.driverapp.ui.home.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends AndroidViewModel {

    private MyApplication myApplication;
    private MyFirebase myFirebase;
    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<Boolean> loading = new ObservableField<>();

    private PrefsDataLogin prefsDataLogin;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        prefsDataLogin = new PrefsDataLogin(getApplication());
        loading.set(false);
        myFirebase = MyFirebase.getInstance(FirebaseFirestore.getInstance());
    }

    void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    public void onContinueClick() {

        loading.set(true);
        if (isLoginInValid()) {
            loading.set(false);
            Log.e("LOG", "inValid");
            Toast.makeText(getApplication(), getApplication().getText(R.string.login_valid_notification), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("LOG", "Valid");
            checkLogin();
        }
    }

    private boolean isLoginInValid() {
        return TextUtils.isEmpty(username.get()) || TextUtils.isEmpty(password.get());
    }


    private void checkLogin() {
        myFirebase.getDriverInformation(username.get(), new MyFirebase.GetDriverDataCallback() {
            @Override
            public void onGetDriverSuccess(Driver driver) {
                Log.e("LOG", "onGetDriverSuccess");
                if (driver != null) {
                    if (isTruePass(driver.getPass())) {
                        setLoginDriver(driver);
                        goToHome();
                    } else {
                        loading.set(false);
                        Toast.makeText(getApplication(), getApplication().getText(R.string.login_wrong_password), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.set(false);
                    Toast.makeText(getApplication(), getApplication().getText(R.string.login_get_driver_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetDriverError(Exception exception) {
                loading.set(false);
                Log.e("LOG", "onGetDriverError");
                Toast.makeText(getApplication(), getApplication().getText(R.string.login_cant_get_data), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTruePass(String pass) {
        return !TextUtils.isEmpty(pass) && pass.equals(password.get());
    }

    private void goToHome() {
        loading.set(false);
        prefsDataLogin.saveDataLogin(username.get(), password.get());
        Intent intent = new Intent(HomeActivity.getInstance(this.getApplication()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }

    void getDataLoginSaved() {
        Log.e("LOG", "getDataLoginSaved");
        if (!prefsDataLogin.getDriverUsername().equals("") && !prefsDataLogin.getDriverPassword().equals("")) {
            username.set(prefsDataLogin.getDriverUsername());
            password.set(prefsDataLogin.getDriverPassword());
            onContinueClick();
        }
    }

    private void setLoginDriver(Driver loginDriver) {
        myApplication.setmDriver(loginDriver);
    }

    /*private void AddNewDriver() {
        Driver driver = new Driver();
        driver.setFullname("Nguyen Hoang Anh");
        driver.setPass("123456");
        driver.setUsername("123456");
        driver.setLicenseplates(new DriverLocation("lat", "long"));
        myFirebase.addNewDriver(driver);
    }*/
}
