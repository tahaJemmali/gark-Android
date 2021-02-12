package com.example.gark.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
    public static final String CHECKBOX = "cbRememberMe";
    String email ;
    String password;
    boolean cbState ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        loadPreference();
        updatePreference();
    }
    public void loadPreference(){
        email = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(EMAIL, "");
        password = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(PASSWORD, "");
        cbState = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getBoolean(CHECKBOX, false);
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
            Toast.makeText(LoginActivity.this,"Invalid Email or Password",Toast.LENGTH_LONG).show();
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
        Log.e("TAG", "forgetPassword: ");
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
        Toast.makeText(this,"Welcome !",Toast.LENGTH_SHORT).show();
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