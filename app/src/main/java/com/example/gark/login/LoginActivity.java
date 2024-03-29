package com.example.gark.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gark.ForgotPasswordActivity;
import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.User;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.UserRepository;
import com.example.gark.tutorial.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity implements IRepository {
    //UI
    CircularProgressButton login;
    EditText emailET;
    EditText passwordET;
    CheckBox remeberMe;
    //VAR
    public static final String SHARED_PREFS = "SharedPrefsFile";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String LANGUAGE = "language";
    public static final String DARK_MODE = "dark_mode";
    public static final String CHECKBOX = "cbRememberMe";
    String email ;
    String password;
    boolean cbState ;
    String appLanguage;
    int darkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPreference();
        setContentView(R.layout.activity_login);
        UserRepository.getInstance().setiRepository(this);
        //InitUI
        initUI();
    }

    void initUI() {
        login = (CircularProgressButton) findViewById(R.id.btn_id);
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        remeberMe=findViewById(R.id.remeberMe);
        updatePreference();
    }
    public void loadPreference(){
        email = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(EMAIL, "");
        password = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(PASSWORD, "");
        cbState = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(CHECKBOX, false);
        appLanguage = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(LANGUAGE, "en");
        darkMode = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getInt(DARK_MODE, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(darkMode);
        Locale locale = new Locale(appLanguage);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
    public void updatePreference() {
        if (cbState) {
            emailET.setText(email);
            passwordET.setText(password);
            remeberMe.setChecked(cbState);
        }
    }

    public void login(View view) {
        login.startAnimation();
        if (!emailET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty() ){
            loginUser(emailET.getText().toString(),passwordET.getText().toString());
        }else{
            dismissLoadingButton();
            Toast.makeText(LoginActivity.this, getString(R.string.invalid_mail_password),Toast.LENGTH_LONG).show();
        }
    }

    void loginUser(final String email,final String password){
        UserRepository.getInstance().login(email,password,this);
        savePreference();
    }
    public void savePreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        if (remeberMe.isChecked()) {
            prefEditor.putString(EMAIL, emailET.getText().toString());
            prefEditor.putString(PASSWORD, passwordET.getText().toString());
            prefEditor.putBoolean(CHECKBOX, remeberMe.isChecked());
            prefEditor.putString(LANGUAGE, appLanguage);
            prefEditor.putInt(DARK_MODE, darkMode);
            prefEditor.apply();
            //apply shyncrone & commit ashyncrone
            return;
        }
        clearData();
    }

    public void clearData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sharedPreferences.edit().remove(EMAIL).remove(PASSWORD).remove(CHECKBOX).apply();
    }
    public void forgetPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        Toast.makeText(this, getString(R.string.welcome),Toast.LENGTH_SHORT).show();
        Intent intent=null;
        if (!MainActivity.getCurrentLoggedInUser().getCompletedInformation()){
             intent = new Intent(this, WelcomeActivity.class);
        }else{
             intent = new Intent(this,MainActivity.class);
        }

        startActivity(intent);
        finish();
    }


    @Override
    public void dismissLoadingButton() {
        login.revertAnimation();
    }
}