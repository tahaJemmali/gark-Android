package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gark.entites.User;

public class MainActivity extends AppCompatActivity {
//UI
    //VAR
private static User CurrentLoggedInUser;

    public static User getCurrentLoggedInUser(){
        return CurrentLoggedInUser;
    }

    public static void setCurrentLoggedInUser (User user){
        CurrentLoggedInUser=user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}