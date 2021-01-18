package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.fragments.AcceuilFragment;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.example.gark.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CallBackInterface {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
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
        fragmentManager = getSupportFragmentManager();
        initUI();
    }
    public void initUI(){
        acceuilFragement();
    }


    public void addAcceuilFragment(View view) {
    acceuilFragement();
    }

    public void acceuilFragement(){
        fragmentTransaction = fragmentManager.beginTransaction();
        AcceuilFragment acceuilFragment = new AcceuilFragment();
        acceuilFragment.setCallBackInterface(this);
        fragmentTransaction.replace(R.id.fragment_container,acceuilFragment);
        fragmentTransaction.commit();
    }
    @Override
    public void popBack() {

    }

    @Override
    public void openFragment(String name) {

    }

    @Override
    public void popBackb() {

    }
}