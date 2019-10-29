package com.aeonvn.driverapp.ui.login;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.aeonvn.driverapp.MyApplication;
import com.aeonvn.driverapp.R;
import com.aeonvn.driverapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    LoginViewModel loginViewModel;
    EditText usernameEditext, passwordEditText;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = MyApplication.getInstance();
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.setMyApplication(myApplication);
        activityLoginBinding.setViewModel(loginViewModel);
        usernameEditext = findViewById(R.id.login_driver_username);
        passwordEditText = findViewById(R.id.login_driver_pass);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginViewModel.getDataLoginSaved();
    }

    /*public void setUsernameError(String error) {
        usernameEditext.setError(error);
        usernameEditext.requestFocus();
    }

    public void setPasswordError(String error) {
        passwordEditText.setError(error);
        passwordEditText.requestFocus();
    }*/

}
