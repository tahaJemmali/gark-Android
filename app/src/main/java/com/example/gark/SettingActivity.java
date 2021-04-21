package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gark.login.LoginActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingActivity extends AppCompatActivity {
//UI
    SwitchMaterial darkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        darkMode=findViewById(R.id.darkMode);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            darkMode.setChecked(true);
        }else {
            darkMode.setChecked(false);
        }
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int mode=-1;
                if(b){
                    mode=AppCompatDelegate.MODE_NIGHT_YES;
                    Toast.makeText(SettingActivity.this, R.string.dark_on,Toast.LENGTH_LONG).show();
                }
                else{
                    mode=AppCompatDelegate.MODE_NIGHT_NO;
                    Toast.makeText(SettingActivity.this, getString(R.string.dark_off),Toast.LENGTH_LONG).show();
                }

                AppCompatDelegate.setDefaultNightMode(mode);

                Intent intent=new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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