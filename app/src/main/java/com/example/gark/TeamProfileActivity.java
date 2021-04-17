package com.example.gark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gark.adapters.CardAdapter;
import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.SkillsAdapter;
import com.example.gark.adapters.StatsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.login.LoginActivity;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.ultramegasoft.radarchart.RadarHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TeamProfileActivity extends AppCompatActivity implements IRepository {
    //UI
ImageView teamImage,teamCountry,start_one, start_two, start_three, start_four, start_five;
TextView teamName,teamCategorie,dateCreatedTeam,descriptionTeam;
RecyclerView statRecyclerView,teamMemberRecyclerView;
    ProgressDialog dialogg;
    ImageButton addFavoirs,playerDisplayType;
    Button joinTeam,infoBtn,statsBtn;
    View info_team_layout,stats_layout;

//VAR
    boolean changeDisplay=true;
    ArrayList<Skills> players;
    Team team;
    StatsAdapter statsAdapter;
    TopPlayersAdapter topPlayersAdapter;
    CardAdapter cardAdapter;
    int generator=0;
    public static final String TEAMS = "teams";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);
        initUI();
    }

    void initUI(){
        dialogg = ProgressDialog.show(this, "","Loading" , true);
        joinTeam=findViewById(R.id.joinTeam);
        playerDisplayType=findViewById(R.id.playerDisplayType);
        teamImage=findViewById(R.id.teamImage);
        teamCountry=findViewById(R.id.teamCountry);
        start_one=findViewById(R.id.start_one);
        start_two=findViewById(R.id.start_two);
        start_three=findViewById(R.id.start_three);
        start_four=findViewById(R.id.start_four);
        start_five=findViewById(R.id.start_five);
        teamName=findViewById(R.id.teamName);
        teamCategorie=findViewById(R.id.teamCategorie);
        dateCreatedTeam=findViewById(R.id.dateCreatedTeam);
        descriptionTeam=findViewById(R.id.descriptionTeam);
        statRecyclerView=findViewById(R.id.statRecyclerView);
        teamMemberRecyclerView=findViewById(R.id.teamMemberRecyclerView);
        addFavoirs=findViewById(R.id.addFavoirs);
        infoBtn=findViewById(R.id.infoBtn);
        statsBtn=findViewById(R.id.statsBtn);
        info_team_layout=findViewById(R.id.info_team_layout);
        stats_layout=findViewById(R.id.stats_layout);
        addFavoirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().findById(this,getIntent().getStringExtra("teamId"));
        statRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        teamMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void savePreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(FavorisTeamActivity.SHARED_PREFS_Fav, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        Set<String> teamsFav = getSharedPreferences(FavorisTeamActivity.SHARED_PREFS_Fav, MODE_PRIVATE).getStringSet(TEAMS, null);
        if (teamsFav ==null){
            teamsFav =new HashSet<String>();
        }
        teamsFav.add(team.getId());
        prefEditor.clear();
        prefEditor.putStringSet(TEAMS, teamsFav);
        prefEditor.apply();
        prefEditor.commit();
    }

    void validate(){
        if(getSharedPreferences(FavorisTeamActivity.SHARED_PREFS_Fav, MODE_PRIVATE).getStringSet(TEAMS, null) !=null){
            if(getSharedPreferences(FavorisTeamActivity.SHARED_PREFS_Fav, MODE_PRIVATE).getStringSet(TEAMS, null).contains(team.getId()))
                Toast.makeText(TeamProfileActivity.this,"Team Already added to your favorite",Toast.LENGTH_LONG).show();
        }

        savePreference();
        Toast.makeText(TeamProfileActivity.this,"Team Added to your Favorite",Toast.LENGTH_LONG).show();
    }

    public void joinTeam(View view) {
    team.getSubstitutes().add(MainActivity.currentPlayerSkills);
    TeamRepository.getInstance().update(this,team,team.getId(),false);
    SkillsRepository.getInstance().addTeamToPlayer(this,MainActivity.currentPlayerSkills.getId(),team.getId());
        Toast.makeText(TeamProfileActivity.this,"You have joined sucessfully "+team.getName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        //team
        team=TeamRepository.getInstance().getElement();
        statsAdapter = new StatsAdapter(this, team);
        statRecyclerView.setAdapter(statsAdapter);
      //player
        players=new ArrayList<Skills>();
            players.add(team.getCapitaine());
            if(team.getTitulares().size()>0){
                for (Skills row :team.getTitulares()){
                    players.add(row);
                }
            }
        if(team.getSubstitutes().size()>0) {
            for (Skills row : team.getSubstitutes()) {
                players.add(row);
            }
        }
        if(players.size()>0){
            if(players.contains(MainActivity.currentPlayerSkills))
                joinTeam.setVisibility(View.GONE);
        }
            topPlayersAdapter = new TopPlayersAdapter(this,players,team.getCapitaine().getId() );
            teamMemberRecyclerView.setAdapter(topPlayersAdapter);
            Bitmap bitmap = getBitmapFromString(team.getImage());
            teamImage.setImageBitmap(bitmap);
            teamCountry.setImageResource(this.getResources().getIdentifier(team.getNationality().toString(), "drawable", this.getPackageName()));
            switch (team.getRating()) {
                case 1:
                    start_one.setImageResource(R.drawable.ic_rating_start_checked);
                    break;
                case 2:
                    start_one.setImageResource(R.drawable.ic_rating_start_checked);
                    start_two.setImageResource(R.drawable.ic_rating_start_checked);
                    break;
                case 3:
                    start_one.setImageResource(R.drawable.ic_rating_start_checked);
                    start_two.setImageResource(R.drawable.ic_rating_start_checked);
                    start_three.setImageResource(R.drawable.ic_rating_start_checked);
                    break;
                case 4:
                    start_one.setImageResource(R.drawable.ic_rating_start_checked);
                    start_two.setImageResource(R.drawable.ic_rating_start_checked);
                    start_three.setImageResource(R.drawable.ic_rating_start_checked);
                    start_four.setImageResource(R.drawable.ic_rating_start_checked);
                    break;
                case 5:
                    start_one.setImageResource(R.drawable.ic_rating_start_checked);
                    start_two.setImageResource(R.drawable.ic_rating_start_checked);
                    start_three.setImageResource(R.drawable.ic_rating_start_checked);
                    start_four.setImageResource(R.drawable.ic_rating_start_checked);
                    start_five.setImageResource(R.drawable.ic_rating_start_checked);
                    break;
                default:
                    break;
            }
            teamName.setText(team.getName());
            teamCategorie.setText(team.getCategorie().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            dateCreatedTeam.setText(formatter.format(team.getDate_created()));
            descriptionTeam.setText(team.getDescription());

    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    public void showInfo(View view) {
        info_team_layout.setVisibility(View.VISIBLE);
        stats_layout.setVisibility(View.GONE);
        infoBtn.setTextColor(Color.WHITE);
        statsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
    }

    public void showStats(View view) {
        info_team_layout.setVisibility(View.GONE);
        stats_layout.setVisibility(View.VISIBLE);
        infoBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        statsBtn.setTextColor(Color.WHITE);
    }

    public void changeSkillsDisplay(View view) {
        if(changeDisplay){
            playerDisplayType.setImageResource(R.drawable.ic_baseline_groups_24);
            cardAdapter = new CardAdapter(this,players,team.getCapitaine().getId() );
            teamMemberRecyclerView.setAdapter(cardAdapter);
            changeDisplay=false;
        }else {
            playerDisplayType.setImageResource(R.drawable.small_shield);
            topPlayersAdapter = new TopPlayersAdapter(this,players,team.getCapitaine().getId() );
            teamMemberRecyclerView.setAdapter(topPlayersAdapter);
            changeDisplay=true;
        }
    }
}