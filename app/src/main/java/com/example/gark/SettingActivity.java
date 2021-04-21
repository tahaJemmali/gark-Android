package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gark.entites.Nationality;
import com.example.gark.login.LoginActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    //UI
    SwitchMaterial darkMode;
    Spinner languagePicker;
    //var
    public static final String SHARED_PREFS = "SharedPrefsFile";
    public static final String LANGUAGE = "language";
    public static final String DARK_MODE = "dark_mode";
    String appLanguage;
    int dark_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        darkMode = findViewById(R.id.darkMode);
        languagePicker = findViewById(R.id.languagePicker);

        loadPreference();
        if (appLanguage.equals("en"))
            languagePicker.setSelection(0);
        else
            languagePicker.setSelection(1);
        languagePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && !appLanguage.equals("en")) {
                    refresh("en");
                } else if (position == 1 && !appLanguage.equals("fr")) {
                    refresh("fr");
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        darkMode.setChecked(dark_mode==AppCompatDelegate.MODE_NIGHT_YES);
        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    dark_mode = AppCompatDelegate.MODE_NIGHT_YES;
                    Toast.makeText(SettingActivity.this, getString(R.string.dark_on), Toast.LENGTH_LONG).show();
                } else {
                    dark_mode = AppCompatDelegate.MODE_NIGHT_NO;
                    Toast.makeText(SettingActivity.this, getString(R.string.dark_off), Toast.LENGTH_LONG).show();
                }

                AppCompatDelegate.setDefaultNightMode(dark_mode);

                goToMain();
            }
        });
    }

    public void loadPreference() {
        appLanguage = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getString(LANGUAGE, "en");
        dark_mode = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).getInt(DARK_MODE, AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void updatePrefabe() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.remove(LANGUAGE);
        prefEditor.remove(DARK_MODE);
        prefEditor.putString(LANGUAGE, appLanguage);
        prefEditor.putInt(DARK_MODE, dark_mode);
        prefEditor.apply();
    }

    void refresh(String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        appLanguage=languageToLoad;
        goToMain();
    }

    void goToMain() {
        updatePrefabe();
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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