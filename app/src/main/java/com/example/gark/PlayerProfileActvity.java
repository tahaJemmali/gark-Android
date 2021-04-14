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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.SkillsAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;

public class PlayerProfileActvity extends AppCompatActivity implements IRepository {
    //UI
    View infoFragment,statsfragment,postsfragment,dashboardfragment;
    ImageView playerImage,nationality,start_one, start_two, start_three, start_four, start_five;
    TextView playerNom,age,rolePlayer,descriptionPlayer,not_yet_teams,not_yet_posts;
    ImageButton skillsType;
    RecyclerView skillsRecyclerView,postRecyclerView;
    ProgressDialog dialogg;
    ProgressDialog dialoggPost;
    RadarView radar;
    Button contact,infoBtn,statsBtn,postsBtn,dashboardBtn;
    View cardView;
    RecyclerView recycleViewTeams;
    //VAR
    TeamsAdapter teamsAdapter;
    ArrayList<Post> posts;
    Skills player;
    SkillsAdapter skillsAdapter;
    PostAdapter postAdapter;
    Boolean generated=false;
    Boolean showListRadar=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initUI();
    }
    void initUI(){
        dialogg = ProgressDialog.show(this, "","Loading" , true);
        playerImage=findViewById(R.id.playerImage);
        recycleViewTeams= findViewById(R.id.recycleViewTeams);
        not_yet_posts= findViewById(R.id.not_yet_posts);
        not_yet_teams= findViewById(R.id.not_yet_teams);
        cardView=findViewById(R.id.cardView);
        skillsType=findViewById(R.id.skillsType);
        nationality=findViewById(R.id.nationality);
        start_one=findViewById(R.id.start_one);
        start_two=findViewById(R.id.start_two);
        start_three=findViewById(R.id.start_three);
        start_four=findViewById(R.id.start_four);
        start_five=findViewById(R.id.start_five);
        playerNom=findViewById(R.id.playerNom);
        age=findViewById(R.id.age);
        rolePlayer=findViewById(R.id.rolePlayer);
        descriptionPlayer=findViewById(R.id.descriptionPlayer);
        skillsRecyclerView=findViewById(R.id.skillsRecyclerView);
        postRecyclerView=findViewById(R.id.postRecyclerView);
        radar=findViewById(R.id.radar);
        contact=findViewById(R.id.contact);
        infoBtn=findViewById(R.id.infoBtn);
        statsBtn=findViewById(R.id.statsBtn);
        postsBtn=findViewById(R.id.postsBtn);
        dashboardBtn=findViewById(R.id.dashboardBtn);
        infoFragment=findViewById(R.id.infoFragment);
        statsfragment=findViewById(R.id.statsfragment);
        postsfragment=findViewById(R.id.postsfragment);
        dashboardfragment=findViewById(R.id.dashboardfragment);
        SkillsRepository.getInstance().setiRepository(this);
       SkillsRepository.getInstance().findById(this,getIntent().getStringExtra("playerId"));
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initUIRecycleViewerTeams() {
        if(player.getTeams().size()==0){
            not_yet_teams.setVisibility(View.VISIBLE);
        }else{
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
      player=SkillsRepository.getInstance().getElement();
        skillsAdapter = new SkillsAdapter(this, player);
        skillsRecyclerView.setAdapter(skillsAdapter);
        //posts
        if(posts==null){
            posts=new  ArrayList<Post>();
            PostRepository.getInstance().setiRepository(this);
        }else {
            posts= PostRepository.getInstance().getList();
            if(posts.size()>0){
                postAdapter = new PostAdapter(this, posts);
                postRecyclerView.setAdapter(postAdapter);
            }else{
                not_yet_posts.setVisibility(View.VISIBLE);
            }
            dialoggPost.dismiss();

        }

        if(player!=null){

            if(!generated){
                dialoggPost = ProgressDialog.show(this, "","Loading Posts" , true);
                PostRepository.getInstance().findByCreator(this,player.getPlayer().getId());
                generated=true;
            }
        Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        playerImage.setImageBitmap(bitmap);
        // radar array list
            ArrayList<RadarHolder> data = new ArrayList<RadarHolder>();
            int x=Math.round(player.getDefending()*10/100);
            data.add(new RadarHolder("defending",x));
            x=Math.round(player.getShooting()*10/100);
            data.add(new RadarHolder("shooting",x));
            x=Math.round(player.getPassing()*10/100);
            data.add(new RadarHolder("passing",x));
            x=Math.round(player.getDribbling()*10/100);
            data.add(new RadarHolder("dribbling",x));
            x=Math.round(player.getDefending()*10/100);
            data.add(new RadarHolder("defending",x));
            x=Math.round(player.getPhysical()*10/100);
            data.add(new RadarHolder("physical",x));
            x=Math.round(player.getPace()*10/100);
            data.add(new RadarHolder("pace",x));
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
        playerNom.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
        rolePlayer.setText(player.getRole().toString());
            age.setText(player.getAge()+" years");
            descriptionPlayer.setText(player.getDescription());
            initUIRecycleViewerTeams();
        }
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
      Intent intent = new Intent(PlayerProfileActvity.this,CardActivity.class);
        intent.putExtra("player",player);
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
        if(!showListRadar){
            skillsType.setImageResource(R.drawable.ic_baseline_workspaces_24);
            skillsRecyclerView.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            showListRadar=true;
        }else {
            skillsType.setImageResource(R.drawable.ic_baseline_track_changes_24);
            cardView.setVisibility(View.GONE);
            skillsRecyclerView.setVisibility(View.VISIBLE);
            showListRadar=false;
        }
    }
}