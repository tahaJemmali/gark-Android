package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.SkillsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;

public class PlayerProfileActvity extends AppCompatActivity implements IRepository {
    //UI
    ImageView playerImage,nationality,start_one, start_two, start_three, start_four, start_five;
    TextView playerNom,age,rolePlayer,descriptionPlayer;
    RecyclerView skillsRecyclerView,postRecyclerView;
    ProgressDialog dialogg;
    RadarView radar;
    Button contact;

    //VAR
    ArrayList<Post> posts;
    Skills player;
    SkillsAdapter skillsAdapter;
    PostAdapter postAdapter;
    Boolean generated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        initUI();
    }
    void initUI(){
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
        playerImage=findViewById(R.id.playerImage);
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
        SkillsRepository.getInstance().setiRepository(this);
       SkillsRepository.getInstance().findById(this,getIntent().getStringExtra("playerId"));
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
            postAdapter = new PostAdapter(this, posts);
            postRecyclerView.setAdapter(postAdapter);
        }

        if(player!=null){
            if(!generated){
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
}