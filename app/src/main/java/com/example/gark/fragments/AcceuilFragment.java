package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.ChallengeAdapter;
import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceuilFragment#} factory method to
 * create an instance of mContext fragment.
 */
public class AcceuilFragment extends Fragment implements IRepository {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    TextView showAllLikedPosts;
    //UI
    RecyclerView recycleViewTopPlayers;
    RecyclerView recycleViewTeams;
    RecyclerView recycleViewPosts;
    RecyclerView recycleViewChallenges;
    ProgressDialog dialogg;
    SwipeRefreshLayout swipe_container;

    //VAR
    public static ArrayList<Skills> players;
    public static ArrayList<Team> teams;
    public static ArrayList<Challenge> challenges;
    public static ArrayList<Post> posts;
    public static ArrayList<Post> topTen;
    //Adapters
    TopPlayersAdapter topPlayersAdapter;
    TeamsAdapter teamsAdapter;
    PostAdapter postAdapter;
    ChallengeAdapter challengeAdapter;
    int generating = 0;

   //boolean
    boolean generated=false;
    private static final String FRAGMENT_NAME = "acceuil";
    public AcceuilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_acceuil, container, false);
        mContext = getContext();
        if(!generated) {
            initUI();
            generated = true;
        }else {
            initUIRecycleViewerTopPlayers();
            initUIRecycleViewerPosts();
            initUIRecycleViewChallenges();
            initUIRecycleViewerTopRatedTeams();
        }
        swipe_container=view.findViewById(R.id.swipe_container);
        showAllLikedPosts=view.findViewById(R.id.showAllLikedPosts);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialogg = ProgressDialog.show(mContext, "","Loading Data ..." , true);
                //generated = false;
                loadData();
                swipe_container.setRefreshing(false);
            }
        });

        showAllLikedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                intent.putExtra("sizeTen",topTen.size());
                mContext.startActivity(intent);
            }
        });
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }
    void initUI(){
            //\tournement
            dialogg = ProgressDialog.show(mContext, "","Loading Data ..." , true);
            getAllPlayers();
    }

     void loadData(){
            generating = 0;
            topTen.clear();
            getAllPlayers();
    }
    public void getAllChallenges(){
        ///challenges
        challenges=new  ArrayList<Challenge>();
        ChallengeRepository.getInstance().setiRepository(this);
        ChallengeRepository.getInstance().getAll(mContext,null);
    }
    public void getAllPlayers(){
        ///players
        players=new  ArrayList<Skills>();
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().getAll(mContext,null);
    }
    public void getAllTeams(){
        //teams
        teams=new  ArrayList<Team>();
        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().getAll(mContext,null);
    }
    public void getAllPosts(){
        //posts
        posts=new  ArrayList<Post>();
        topTen=new  ArrayList<Post>();
        PostRepository.getInstance().setiRepository(this);
        PostRepository.getInstance().getAll(mContext,null);
    }
    private void initUIRecycleViewChallenges() {
        recycleViewChallenges=view.findViewById(R.id.recycleViewChallenges);

        recycleViewChallenges.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        challengeAdapter = new ChallengeAdapter(mContext, challenges);
        recycleViewChallenges.setAdapter(challengeAdapter);
    }

    private void initUIRecycleViewerPosts() {
        recycleViewPosts=view.findViewById(R.id.recycleViewPosts);

        recycleViewPosts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        postAdapter = new PostAdapter(mContext, topTen);
        recycleViewPosts.setAdapter(postAdapter);
    }

    private void initUIRecycleViewerTopPlayers() {
        recycleViewTopPlayers=view.findViewById(R.id.recycleViewTopPlayers);

        recycleViewTopPlayers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        topPlayersAdapter = new TopPlayersAdapter(mContext, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
    }
    private void initUIRecycleViewerTopRatedTeams() {
        recycleViewTeams=view.findViewById(R.id.recycleViewTeams);
        recycleViewTeams.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        teamsAdapter = new TeamsAdapter(mContext, teams);
        recycleViewTeams.setAdapter(teamsAdapter);
    }

    @Override
    public void showLoadingButton() {
      //  dialogg.show();
    }

    @Override
    public void doAction() {
        switch (generating){
            case 0:
                //////skills
                players=SkillsRepository.getInstance().getList();
                initUIRecycleViewerTopPlayers();
                Log.e("TAG", "doAction: "+"hem" );
                generating++;
                getAllPosts();

                break;
            case 1:
                /////posts
                posts= PostRepository.getInstance().getList();
                generating++;
                if (topTen.isEmpty()){
                    int i=0;
                    for (Post row:posts){
                        if(i<5){
                            topTen.add(row);
                            i++;
                        }else {
                            break;
                        }
                    }
                }
                initUIRecycleViewerPosts();
                getAllChallenges();
                break;
            case 2:
                /////challenges
                challenges= ChallengeRepository.getInstance().getList();
                initUIRecycleViewChallenges();
                generating++;
                getAllTeams();
                break;
            case 3:
                /////teams
                teams= TeamRepository.getInstance().getList();
                initUIRecycleViewerTopRatedTeams();
                generating++;
                dialogg.dismiss();
                break;
        }
    }

    @Override
    public void dismissLoadingButton() {
      //  dialogg.dismiss();
    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}