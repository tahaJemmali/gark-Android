package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.SkillsAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchActionType;
import com.example.gark.entites.MatchType;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchActionRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.squareup.picasso.Picasso;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerProfileActvity extends AppCompatActivity implements IRepository {
    //UI
    View infoFragment, statsfragment, postsfragment, dashboardfragment;
    ImageView playerImage, nationality, start_one, start_two, start_three, start_four, start_five;
    TextView playerNom, age, rolePlayer, descriptionPlayer, not_yet_teams, not_yet_posts;
    ImageButton skillsType;
    RecyclerView skillsRecyclerView, postRecyclerView;
    ProgressDialog dialogg;
    ProgressDialog dialoggPost;
    RadarView radar;
    Button contact, infoBtn, statsBtn, postsBtn, dashboardBtn;
    //View cardView;
    RecyclerView recycleViewTeams;
    //////Stats layout
    TextView votes,yellowCards,redCards,goals,score,xp,gamePlayed,manOfTheTournement;
    //info
    ImageView TeamInternation, TeamNational, PlayerNational, PlayerTunisia;
    TextView TeamInternationName, TeamNationalName, PlayerNationalName, PlayerTunisiaName,height,weight;
    //VAR
    TeamsAdapter teamsAdapter;
    ArrayList<Post> posts;
    Skills player;
    SkillsAdapter skillsAdapter;
    PostAdapter postAdapter;
    Boolean generated = false;
    Boolean showListRadar = true;
    int playerinfo=0;
    ArrayList<MatchAction> matchActions;
    ArrayList<MatchAction> matchActionsTeams;
    int interingDoAction=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initUI();
    }

    void initUI() {
        dialogg = ProgressDialog.show(this, "", "Loading", true);

        TeamInternation = findViewById(R.id.TeamInternation);
        TeamNational = findViewById(R.id.TeamNational);
        PlayerNational = findViewById(R.id.PlayerNational);
        PlayerTunisia = findViewById(R.id.PlayerTunisia);
        TeamInternationName = findViewById(R.id.TeamInternationName);
        TeamNationalName = findViewById(R.id.TeamNationalName);
        PlayerNationalName = findViewById(R.id.PlayerNationalName);
        PlayerTunisiaName = findViewById(R.id.PlayerTunisiaName);
        height= findViewById(R.id.height);
        weight= findViewById(R.id.weight);

        playerImage = findViewById(R.id.playerImage);
        recycleViewTeams = findViewById(R.id.recycleViewTeams);
        not_yet_posts = findViewById(R.id.not_yet_posts);
        not_yet_teams = findViewById(R.id.not_yet_teams);
      //  cardView = findViewById(R.id.cardView);
        skillsType = findViewById(R.id.skillsType);
        nationality = findViewById(R.id.nationality);
        start_one = findViewById(R.id.start_one);
        start_two = findViewById(R.id.start_two);
        start_three = findViewById(R.id.start_three);
        start_four = findViewById(R.id.start_four);
        start_five = findViewById(R.id.start_five);
        playerNom = findViewById(R.id.playerNom);
        age = findViewById(R.id.age);
        rolePlayer = findViewById(R.id.rolePlayer);
        descriptionPlayer = findViewById(R.id.descriptionPlayer);
        skillsRecyclerView = findViewById(R.id.skillsRecyclerView);
        postRecyclerView = findViewById(R.id.postRecyclerView);
        radar = findViewById(R.id.radar);
        contact = findViewById(R.id.contact);
        infoBtn = findViewById(R.id.infoBtn);
        statsBtn = findViewById(R.id.statsBtn);
        postsBtn = findViewById(R.id.postsBtn);
        dashboardBtn = findViewById(R.id.dashboardBtn);
        infoFragment = findViewById(R.id.infoFragment);
        statsfragment = findViewById(R.id.statsfragment);
        postsfragment = findViewById(R.id.postsfragment);
        dashboardfragment = findViewById(R.id.dashboardfragment);
        initStatsLayout();
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().findById(this, getIntent().getStringExtra("playerId"));
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    void initStatsLayout(){
        votes=findViewById(R.id.votes);
        yellowCards=findViewById(R.id.yellowCards);
        redCards=findViewById(R.id.redCards);
        goals=findViewById(R.id.goals);
        score=findViewById(R.id.score);
        xp=findViewById(R.id.xp);
        gamePlayed=findViewById(R.id.gamePlayed);
        manOfTheTournement=findViewById(R.id.manOfTheTournement);
    }

    private void initUIRecycleViewerTeams() {
        if (player.getTeams().size() == 0) {
            not_yet_teams.setVisibility(View.VISIBLE);
        } else {
            not_yet_teams.setVisibility(View.GONE);
            recycleViewTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            teamsAdapter = new TeamsAdapter(this, (ArrayList<Team>) player.getTeams());
            recycleViewTeams.setAdapter(teamsAdapter);
        }
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        //player
        player = SkillsRepository.getInstance().getElement();
        skillsAdapter = new SkillsAdapter(this, player);
        skillsRecyclerView.setAdapter(skillsAdapter);
        //posts
        if (posts == null) {
            posts = new ArrayList<Post>();
            PostRepository.getInstance().setiRepository(this);
        } else {
            posts = PostRepository.getInstance().getList();
            if (posts.size() > 0) {
                postAdapter = new PostAdapter(this, posts);
                postRecyclerView.setAdapter(postAdapter);
            } else {
                not_yet_posts.setVisibility(View.VISIBLE);
            }
            dialoggPost.dismiss();

        }

        if (player != null) {

            if (!generated) {
                dialoggPost = ProgressDialog.show(this, "", "Loading Posts", true);
                PostRepository.getInstance().findByCreator(this, player.getPlayer().getId());
                generated = true;
                Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
                playerImage.setImageBitmap(bitmap);
                // radar array list
                ArrayList<RadarHolder> data = new ArrayList<RadarHolder>();
                int x = Math.round(player.getDefending() * 10 / 100);
                data.add(new RadarHolder("defending", x));
                x = Math.round(player.getShooting() * 10 / 100);
                data.add(new RadarHolder("shooting", x));
                x = Math.round(player.getPassing() * 10 / 100);
                data.add(new RadarHolder("passing", x));
                x = Math.round(player.getDribbling() * 10 / 100);
                data.add(new RadarHolder("dribbling", x));
                x = Math.round(player.getDefending() * 10 / 100);
                data.add(new RadarHolder("defending", x));
                x = Math.round(player.getPhysical() * 10 / 100);
                data.add(new RadarHolder("physical", x));
                x = Math.round(player.getPace() * 10 / 100);
                data.add(new RadarHolder("pace", x));
                radar.setMaxValue(10);
                radar.setData(data);

                nationality.setImageResource(this.getResources().getIdentifier(player.getNationality().toString(), "drawable", this.getPackageName()));
                switch (player.getRating()) {
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
                if (player.getPlayer().getId().equals(MainActivity.getCurrentLoggedInUser().getId()))
                    contact.setVisibility(View.GONE);
                playerNom.setText(player.getPlayer().getFirstName() + " " + player.getPlayer().getLastName());
                rolePlayer.setText(player.getRole().toString());
                age.setText(player.getAge() + " years");
                descriptionPlayer.setText(player.getDescription());

                //initstate

                Picasso.get().load(player.getBestTeamWorld().getImage()).into(TeamInternation);
                Picasso.get().load(player.getBestTeamTunisia().getImage()).into(TeamNational);
                Picasso.get().load(player.getBestPlayerWorld().getImage()).into(PlayerNational);
                Picasso.get().load(player.getBestPlayerTunisia().getImage()).into(PlayerTunisia);

                TeamInternationName.setText(player.getBestTeamWorld().getName());
                TeamNationalName.setText(player.getBestTeamTunisia().getName());
                PlayerNationalName.setText(player.getBestPlayerWorld().getFirstName()+" "+player.getBestPlayerWorld().getLastName());
                PlayerTunisiaName.setText(player.getBestPlayerTunisia().getFirstName()+" "+player.getBestPlayerTunisia().getLastName());
                height.setText(player.getHeight()+" cm");
                weight.setText(player.getWeight()+" kg");
                initUIRecycleViewerTeams();
            }else{
                switch (playerinfo){
                    case 0:
                        MatchActionRepository.getInstance().setiRepository(this);
                        MatchActionRepository.getInstance().findBy(this,"player",player.getId());
                        playerinfo++;
                        break;
                    case 1:
                        matchActions=MatchActionRepository.getInstance().getList();
                        matchActionsTeams=new ArrayList<MatchAction>();
                        if(player.getTeams().size()==0)
                            setStatTotal();

                       for (Team team :player.getTeams()){
                        MatchActionRepository.getInstance().findBy(this,"team",team.getId());
                         }
                        playerinfo++;
                        break;
                    case 2:
                        interingDoAction++;
                        if(matchActionsTeams.size()==0){
                            matchActionsTeams=MatchActionRepository.getInstance().getList();
                        }else {
                            matchActionsTeams.addAll(MatchActionRepository.getInstance().getList());
                        }

                        if(interingDoAction==player.getTeams().size()){
                            setStatTotal();
                            playerinfo++;
                        }
                        break;
                }
            }
        }
    }
    void setStatTotal(){
        //SET VALUES
        int red=0;
        int goal=0;
        int yellow=0;
        int vote=0;
        int scoreValue=player.getScore();
        int XP=0;
        int games=0;
        int manOf=0;
        //GOALS /RED / YELLOW
        for(MatchAction row :matchActions){
            if(row.getType().equals(MatchActionType.goal)){
                XP+=6;
                goal++;
            }else if(row.getType().equals(MatchActionType.redCard)){
                XP+=-4;
                red++;
            }else {
                XP+=-2;
                yellow++;
            }
        }
        //GAMES
        for (Team row:player.getTeams()){
            games+=row.getDefeats()+row.getDraws()+row.getVictories();
        }
        //Clean matches
        ArrayList<Match> matches=new ArrayList<>();
        for (MatchAction row:matchActionsTeams){
            if(!matches.contains(row.getMatch()))
                matches.add(row.getMatch());
        }
        //XP
        for (Match row:matches){
            if(row.getType().equals(MatchType.Semi)){
                XP+=5;
            } else if(row.getType().equals(MatchType.Final)){
                XP+=10;

                if(player.getTeams().contains(row.getWinner())){
                    XP+=20;

                }
            }
        }
        if(player.getXp()!=XP){
            player.setXp(XP);
            SkillsRepository.getInstance().updateInBackground(this,player,player.getId());
        }

        //SET UI
        votes.setText(""+vote);
        yellowCards.setText(""+yellow);
        redCards.setText(""+red);
        goals.setText(""+goal);
        score.setText(scoreValue+"%");
        xp.setText(""+XP);
        gamePlayed.setText(""+games);
        manOfTheTournement.setText(""+manOf);
    }
    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void showCard(View view) {
        Intent intent = new Intent(PlayerProfileActvity.this, CardActivity.class);
        intent.putExtra("player", player);
        startActivity(intent);
    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    public void contact(View view) {
    }

    public void showDashboard(View view) {
        infoFragment.setVisibility(View.GONE);
        statsfragment.setVisibility(View.GONE);
        postsfragment.setVisibility(View.GONE);
        dashboardfragment.setVisibility(View.VISIBLE);
        infoBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        statsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        dashboardBtn.setTextColor(Color.WHITE);
        postsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
    }

    public void showposts(View view) {
        infoFragment.setVisibility(View.GONE);
        statsfragment.setVisibility(View.GONE);
        postsfragment.setVisibility(View.VISIBLE);
        dashboardfragment.setVisibility(View.GONE);
        infoBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        statsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        dashboardBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        postsBtn.setTextColor(Color.WHITE);
    }

    public void showStats(View view) {
        infoFragment.setVisibility(View.GONE);
        statsfragment.setVisibility(View.VISIBLE);
        postsfragment.setVisibility(View.GONE);
        dashboardfragment.setVisibility(View.GONE);
        infoBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        statsBtn.setTextColor(Color.WHITE);
        dashboardBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        postsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
    }

    public void showInfo(View view) {
        infoFragment.setVisibility(View.VISIBLE);
        statsfragment.setVisibility(View.GONE);
        postsfragment.setVisibility(View.GONE);
        dashboardfragment.setVisibility(View.GONE);
        infoBtn.setTextColor(Color.WHITE);
        statsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        dashboardBtn.setTextColor(getResources().getColor(R.color.gray_scale));
        postsBtn.setTextColor(getResources().getColor(R.color.gray_scale));
    }

    public void changeSkillsDisplay(View view) {
        if (!showListRadar) {
            skillsType.setImageResource(R.drawable.ic_baseline_workspaces_24);
            skillsRecyclerView.setVisibility(View.GONE);
            radar.setVisibility(View.VISIBLE);
            showListRadar = true;
        } else {
            skillsType.setImageResource(R.drawable.ic_baseline_track_changes_24);
            radar.setVisibility(View.GONE);
            skillsRecyclerView.setVisibility(View.VISIBLE);
            showListRadar = false;
        }
    }
}