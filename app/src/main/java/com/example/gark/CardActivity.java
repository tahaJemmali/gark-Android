package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gark.entites.Skills;

public class CardActivity extends AppCompatActivity {
    //UI
TextView score,role,playerName,pace,shooting,passing,dribbling,defending,physical;
        ImageView teamImage,countryImage,playerImage;
        RelativeLayout relativeLayout6;
        //Var
    Skills player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initUI();
    }
    @SuppressLint("ResourceAsColor")
    public void initUI(){
        player=(Skills)getIntent().getSerializableExtra("player");
        relativeLayout6=findViewById(R.id.relativeLayout6);
        score=findViewById(R.id.score);
        role=findViewById(R.id.role);
        playerName=findViewById(R.id.playerName);
        pace=findViewById(R.id.pace);
        shooting=findViewById(R.id.shooting);
        passing=findViewById(R.id.passing);
        dribbling=findViewById(R.id.dribbling);
        defending=findViewById(R.id.defending);
        physical=findViewById(R.id.physical);
        teamImage=findViewById(R.id.teamImage);
        countryImage=findViewById(R.id.countryImage);
        playerImage=findViewById(R.id.playerImage);

        Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        playerImage.setImageBitmap(bitmap);
        if (player.getTeams().size()>0){
            bitmap = getBitmapFromString(player.getTeams().get(0).getImage());
            teamImage.setImageBitmap(bitmap);
        }
        bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        countryImage.setImageResource(this.getResources().getIdentifier(player.getNationality().toString(), "drawable", this.getPackageName()));
        if(player.getScore()<=17){
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_bronze", "drawable", this.getPackageName()));
        }else if(player.getScore()<=34){
            score.setTextColor(Color.WHITE);
            pace.setTextColor(Color.WHITE);
            playerName.setTextColor(Color.WHITE);
            shooting.setTextColor(Color.WHITE);
            passing.setTextColor(Color.WHITE);
            dribbling.setTextColor(Color.WHITE);
            defending.setTextColor(Color.WHITE);
            physical.setTextColor(Color.WHITE);
            role.setTextColor(Color.WHITE);
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_dark_bronze", "drawable", this.getPackageName()));

        }else if(player.getScore()<=51){
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_silver", "drawable", this.getPackageName()));

        }else if(player.getScore()<=67){
            score.setTextColor(Color.WHITE);
            pace.setTextColor(Color.WHITE);
            playerName.setTextColor(Color.WHITE);
            shooting.setTextColor(Color.WHITE);
            passing.setTextColor(Color.WHITE);
            dribbling.setTextColor(Color.WHITE);
            defending.setTextColor(Color.WHITE);
            physical.setTextColor(Color.WHITE);
            role.setTextColor(Color.WHITE);
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_dark_silver", "drawable", this.getPackageName()));

        }else if(player.getScore()<=84){
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_gold", "drawable", this.getPackageName()));

        }else if(player.getScore()<100){
            score.setTextColor(Color.WHITE);
            pace.setTextColor(Color.WHITE);
            playerName.setTextColor(Color.WHITE);
            shooting.setTextColor(Color.WHITE);
            passing.setTextColor(Color.WHITE);
            dribbling.setTextColor(Color.WHITE);
            defending.setTextColor(Color.WHITE);
            physical.setTextColor(Color.WHITE);
            role.setTextColor(Color.WHITE);
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_dark_gold", "drawable", this.getPackageName()));

        }else {
            score.setTextColor(Color.WHITE);
            pace.setTextColor(Color.WHITE);
            playerName.setTextColor(Color.WHITE);
            shooting.setTextColor(Color.WHITE);
            passing.setTextColor(Color.WHITE);
            dribbling.setTextColor(Color.WHITE);
            defending.setTextColor(Color.WHITE);
            physical.setTextColor(Color.WHITE);
            role.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20,0,0);
            params.gravity = Gravity.CENTER;
            playerName.setLayoutParams(params);
            relativeLayout6.setBackgroundResource(this.getResources().getIdentifier("card_plat", "drawable", this.getPackageName()));

        }
        score.setText(""+player.getScore());
        role.setText(player.getRole().toString());
        playerName.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
        pace.setText("PAC "+player.getPace());
        shooting.setText("SHO "+player.getShooting());
        passing.setText("PAS "+player.getPassing());
        dribbling.setText(player.getDribbling()+" DRI");
        defending.setText(player.getDefending()+" DEF");
        physical.setText(player.getPhysical()+" PHY");


    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void getBack(View view) {
        super.onBackPressed();
    }
}