package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.SkillsAdapter;
import com.example.gark.adapters.StatsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.ultramegasoft.radarchart.RadarHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TeamProfileActivity extends AppCompatActivity implements IRepository {
    //UI
ImageView teamImage,teamCountry,start_one, start_two, start_three, start_four, start_five;
TextView teamName,teamCategorie,dateCreatedTeam,descriptionTeam;
RecyclerView statRecyclerView,teamMemberRecyclerView;
    ProgressDialog dialogg;
//VAR
    ArrayList<Skills> players;
    Team team;
    StatsAdapter statsAdapter;
    TopPlayersAdapter topPlayersAdapter;
    Boolean generated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);
        initUI();
    }
    void initUI(){
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
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
        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().findById(this,getIntent().getStringExtra("teamId"));
        statRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        teamMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void joinTeam(View view) {
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
            for (Skills row :team.getTitulares()){
                players.add(row);
            }
        for (Skills row :team.getSubstitutes()){
            players.add(row);
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
}