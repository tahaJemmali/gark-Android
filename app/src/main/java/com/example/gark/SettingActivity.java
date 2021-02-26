package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gark.login.LoginActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        MainActivity.setCurrentLoggedInUser(null);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}