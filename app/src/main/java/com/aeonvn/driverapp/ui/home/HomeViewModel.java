package com.aeonvn.driverapp.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.aeonvn.driverapp.MyApplication;
import com.aeonvn.driverapp.model.Driver;

public class HomeViewModel extends AndroidViewModel {

    private final String TAG = "TAG";
    public final ObservableField<String> driverName = new ObservableField<>();

    private MyApplication myApplication;
    private Driver mDriver;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        myApplication = MyApplication.getInstance();
        mDriver = myApplication.getmDriver();
        driverName.set(mDriver.getFullname());
    }
}
