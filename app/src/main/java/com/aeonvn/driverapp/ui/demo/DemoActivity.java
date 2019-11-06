package com.aeonvn.driverapp.ui.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.aeonvn.driverapp.R;
import com.aeonvn.driverapp.databinding.ActivityDemoBinding;

public class DemoActivity extends AppCompatActivity {

    DemoViewModel demoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDemoBinding activityDemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        demoViewModel = ViewModelProviders.of(this).get(DemoViewModel.class);
        activityDemoBinding.setViewModel(demoViewModel);

        /*Tự động update location theo mảng cố định. Chạy background.
        * Có 2 nút start và stop.
        * Khi đến địa điểm cuối cùng thì tự động đảo chiều -> di chuyển ngược lại*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        //demoViewModel.showNotificationWhenBackground();
    }
}
