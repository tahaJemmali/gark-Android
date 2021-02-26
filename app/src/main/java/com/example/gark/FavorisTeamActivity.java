package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gark.adapters.CommunityTopTeamsAdapter;
import com.example.gark.entites.Team;
import com.example.gark.fragments.TopTeamFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavorisTeamActivity extends AppCompatActivity {
    RecyclerView topTeamsRecyclerView;
    CommunityTopTeamsAdapter communityTopTeamsAdapter;
    ArrayList<Team> teamsFavorites;
    public static final String SHARED_PREFS_Fav = "SharedPrefsFileFav";
    public static final String TEAMS = "teams";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris_team);
        topTeamsRecyclerView=findViewById(R.id.topTeamsRecyclerView);
        teamsFavorites = new ArrayList<Team>();
        loadFavoriteTeam();

    }

    private void loadFavoriteTeam() {
        Set<String> teamsFav =new HashSet<String>();
        teamsFav = getSharedPreferences(SHARED_PREFS_Fav, MODE_PRIVATE).getStringSet(TEAMS, null);
        if (teamsFav != null){
            Log.e("TAG", "loadFavoriteTeam: "+teamsFav );
            for (String row :teamsFav){
                for (Team key: TopTeamFragment.teams){
                    if(key.getId().equals(row)){
                        teamsFavorites.add(key);
                    }
                }
            }
            initUIRecycleViewerListTeams();
        }
    }

    private void initUIRecycleViewerListTeams() {
        topTeamsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        communityTopTeamsAdapter = new CommunityTopTeamsAdapter(this, teamsFavorites);
        topTeamsRecyclerView.setAdapter(communityTopTeamsAdapter);
    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    public void clearAll(View view) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_Fav,MODE_PRIVATE);
        teamsFavorites.clear();
        sharedPreferences.edit().clear().apply();
        communityTopTeamsAdapter = new CommunityTopTeamsAdapter(this, teamsFavorites);
        topTeamsRecyclerView.setAdapter(communityTopTeamsAdapter);
        Toast.makeText(FavorisTeamActivity.this,"Data cleared successfully !",Toast.LENGTH_LONG).show();
    }
}