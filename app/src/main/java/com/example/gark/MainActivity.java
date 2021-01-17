package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.example.gark.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements IRepository {
    //UI
    RecyclerView recycleViewTopPlayers;
    RecyclerView recycleViewTeams;
    ProgressDialog dialogg;
    //VAR
    ArrayList<Skills> players;
    ArrayList<Team> teams;
    //Adapters
    TopPlayersAdapter topPlayersAdapter;
    TeamsAdapter teamsAdapter;
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
        initUI();
    }

    void initUI(){
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
        ///players
        players=new  ArrayList<Skills>();
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().getAll(this,null);
        recycleViewTopPlayers=findViewById(R.id.recycleViewTopPlayers);
        initUIRecycleViewerTopPlayers();
        //teams
        teams=new  ArrayList<Team>();
        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().getAll(this,null);
        recycleViewTeams=findViewById(R.id.recycleViewTeams);
        initUIRecycleViewerTopRatedTeams();
    }
    private void initUIRecycleViewerTopPlayers() {

        recycleViewTopPlayers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        topPlayersAdapter = new TopPlayersAdapter(this, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
    }
    private void initUIRecycleViewerTopRatedTeams() {

        recycleViewTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        teamsAdapter = new TeamsAdapter(this, teams);
        recycleViewTeams.setAdapter(topPlayersAdapter);
    }
    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        //////skills
         players=SkillsRepository.getInstance().getList();
        //pour que le premier s'affiche à gauche
        Collections.reverse(players);
        topPlayersAdapter = new TopPlayersAdapter(this, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
        /////teams
        teams= TeamRepository.getInstance().getList();
        teamsAdapter = new TeamsAdapter(this, teams);
        recycleViewTeams.setAdapter(teamsAdapter);
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}