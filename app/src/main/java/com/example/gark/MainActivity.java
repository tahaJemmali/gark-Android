package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.chat.ChatActivity;
import com.example.gark.chat.GroupListActivity;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.fragments.AcceuilFragment;
import com.example.gark.fragments.ChallengeFragment;
import com.example.gark.fragments.CommunityFragment;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.example.gark.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements CallBackInterface,IRepository {
    ProgressDialog dialogg;
    public static ImageView currentUserImage;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static User CurrentLoggedInUser;
    public static Skills currentPlayerSkills;
    //fragment
    AcceuilFragment acceuilFragment;
    CommunityFragment communityFragment;
    ChallengeFragment challengeFragment;
    //already generated
    boolean alreadyGenerated=false;
    boolean alreadyGeneratedCommunity=false;
    boolean alreadyGeneratedChallenges=false;


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
        dialogg = ProgressDialog.show(this, "","Loading Data ..." , true);
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().findPlayerById(this,MainActivity.getCurrentLoggedInUser().getId());
        currentUserImage=findViewById(R.id.currentUserImage);
        currentUserImage.setImageBitmap(getBitmapFromString(getCurrentLoggedInUser().getPhoto()));

    }
    public static void setUserImage(){
        currentUserImage.setImageBitmap(getBitmapFromString(getCurrentLoggedInUser().getPhoto()));
    }

    public static Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void addAcceuilFragment(View view) {
    acceuilFragement();
    }

    public void acceuilFragement(){
        fragmentTransaction = fragmentManager.beginTransaction();
        if(!alreadyGenerated){
             acceuilFragment = new AcceuilFragment();
            acceuilFragment.setCallBackInterface(this);
            alreadyGenerated=true;
        }
        fragmentTransaction.replace(R.id.fragment_container,acceuilFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void popBack() {

    }

    @Override
    public void openFragmentOrActivity(String name) {
        switch (name){
            case "acceuil":acceuilFragement();
                break;
            case "community":showCommunityFragment();
                break;
            case "topPlayer": CommunityFragment.showAllTeams=false;
                CommunityFragment.showAllPlayer=true;
                showCommunityFragment();
                break;
            case "topTeam": CommunityFragment.showAllTeams=true;
                CommunityFragment.showAllPlayer=false;
                showCommunityFragment();
                break;
            case "challenge":
                challengeFragement();
                break;

        }
    }

    @Override
    public void popBackb() {

    }
    public void challengeFragement(){
        fragmentTransaction = fragmentManager.beginTransaction();
        if(!alreadyGeneratedChallenges){
            challengeFragment = new ChallengeFragment();
            challengeFragment.setCallBackInterface(this);
            alreadyGeneratedChallenges=true;
        }
        fragmentTransaction.replace(R.id.fragment_container,challengeFragment);
        fragmentTransaction.commit();
    }
    public void showCommunityFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        if(!alreadyGeneratedCommunity){
            communityFragment = new CommunityFragment();
            communityFragment.setCallBackInterface(this);
            alreadyGeneratedCommunity=true;
        }
        fragmentTransaction.replace(R.id.fragment_container,communityFragment);
        fragmentTransaction.commit();
    }

    public void addCommunityFragment(View view) {
        showCommunityFragment();
    }

    public void addNotificationFragment(View view) {
        Log.e("TAG", "showNotificationFragment: " );
    }

    public void addCurrentUserProfileFragment(View view) {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    public void addMessengerFragment(View view) {
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivity(intent);
    }

    public void addTopViewedStories(View view) {
        if(AcceuilFragment.posts.size()>0){
            Intent intent = new Intent(this, StoryActivity.class);
            intent.putExtra("size",AcceuilFragment.posts.size());
            startActivity(intent);
        }else {
            Toast.makeText(this,"There's no story to show !",Toast.LENGTH_SHORT).show();
        }

    }

    public void showTopPlayers(View view) {
        openFragmentOrActivity("topPlayer");
    }

    public void showTopTeams(View view) {
        openFragmentOrActivity("topTeam");
    }


    public void greenAddButton(View view) {
        Log.e("TAG", "greenAddButton: Tournement BTN" );
    }

    public void showTChallenges(View view) {
        openFragmentOrActivity("challenge");
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
    currentPlayerSkills=SkillsRepository.getInstance().getElement();
        acceuilFragement();
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
}