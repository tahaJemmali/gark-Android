package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;

import java.util.ArrayList;
import java.util.Collections;

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
    ProgressDialog dialogg;
    //VAR
    ArrayList<Skills> players;
    ArrayList<Team> teams;
    public static ArrayList<Post> posts;
    //Adapters
    TopPlayersAdapter topPlayersAdapter;
    TeamsAdapter teamsAdapter;
    PostAdapter postAdapter;
   //boolean
    boolean generated=false;
   public static ArrayList<Post> topTen;
    private static final String FRAGMENT_NAME = "acceuil";
    public AcceuilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_acceuil, container, false);
        mContext = getContext();
        initUI();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed from frag");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }
    void initUI(){
        recycleViewTopPlayers=view.findViewById(R.id.recycleViewTopPlayers);
        recycleViewTeams=view.findViewById(R.id.recycleViewTeams);
        recycleViewPosts=view.findViewById(R.id.recycleViewPosts);
        showAllLikedPosts=view.findViewById(R.id.showAllLikedPosts);
        showAllLikedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                intent.putExtra("sizeTen",topTen.size());
                mContext.startActivity(intent);
            }
        });
        if (!generated){
            dialogg = ProgressDialog.show(mContext
                    , "","Loading Data ..Wait.." , true);
            ///players
            players=new  ArrayList<Skills>();
            SkillsRepository.getInstance().setiRepository(this);
            SkillsRepository.getInstance().getAll(mContext,null);

            //teams
            teams=new  ArrayList<Team>();
            TeamRepository.getInstance().setiRepository(this);
            TeamRepository.getInstance().getAll(mContext,null);
            //posts
            posts=new  ArrayList<Post>();
            topTen=new  ArrayList<Post>();
            PostRepository.getInstance().setiRepository(this);
            PostRepository.getInstance().getAll(mContext,null);
            generated=true;
        }


        initUIRecycleViewerTopPlayers();
        initUIRecycleViewerTopRatedTeams();
        initUIRecycleViewerPosts();
    }
    private void initUIRecycleViewerPosts() {

        recycleViewPosts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        postAdapter = new PostAdapter(mContext, topTen);
        recycleViewPosts.setAdapter(postAdapter);
    }

    private void initUIRecycleViewerTopPlayers() {

        recycleViewTopPlayers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        topPlayersAdapter = new TopPlayersAdapter(mContext, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
    }
    private void initUIRecycleViewerTopRatedTeams() {

        recycleViewTeams.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        teamsAdapter = new TeamsAdapter(mContext, teams);
        recycleViewTeams.setAdapter(teamsAdapter);
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
     //   Collections.reverse(players);
        topPlayersAdapter = new TopPlayersAdapter(mContext, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
        /////teams
        teams= TeamRepository.getInstance().getList();
        teamsAdapter = new TeamsAdapter(mContext, teams);
        recycleViewTeams.setAdapter(teamsAdapter);
        /////posts
        posts= PostRepository.getInstance().getList();
        int i=0;
        for (Post row:posts){
            if(i<5){
                topTen.add(row);
                i++;
            }else {
                break;
            }

        }
        postAdapter = new PostAdapter(mContext, topTen);
        recycleViewPosts.setAdapter(postAdapter);


    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}