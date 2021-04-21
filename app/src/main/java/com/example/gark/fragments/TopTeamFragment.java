package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gark.AddTeamActivity;
import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.adapters.CommunityTopTeamsAdapter;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopTeamFragment#} factory method to
 * create an instance of this fragment.
 */
public class TopTeamFragment extends Fragment implements IRepository {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    ProgressDialog dialogg;
    //UI
    RecyclerView topTeamsRecyclerView;
    TextView teamNumbers;
    ImageButton addTeam;
    //VAR
    public static ArrayList<Team> teams;
    boolean teamsGenerated=false;
    //Adapters
    CommunityTopTeamsAdapter communityTopTeamsAdapter;
    private static final String FRAGMENT_NAME = "topTeam";

    public TopTeamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_top_team, container, false);
        mContext = getContext();
        initUI();


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    void initUI(){
        topTeamsRecyclerView=view.findViewById(R.id.topTeamsRecyclerView);
        teamNumbers=view.findViewById(R.id.teamNumbers);
        addTeam=view.findViewById(R.id.addTeam);
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTeamActivity.class);
                startActivity(intent);
            }
        });
        if (!teamsGenerated){
            reloadData();
            teamsGenerated=true;
        }else {
            teamNumbers.setText(teams.size()+mContext.getString(R.string.teams));
        }
        initUIRecycleViewerListPlayers();
    }

    private void initUIRecycleViewerListPlayers() {
        topTeamsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        communityTopTeamsAdapter = new CommunityTopTeamsAdapter(mContext, teams);
        topTeamsRecyclerView.setAdapter(communityTopTeamsAdapter);
    }
    void reloadData(){
        dialogg = ProgressDialog.show(mContext, "",getString(R.string.loading), true);
        teams=new  ArrayList<Team>();
        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().getAll(mContext,null);
    }
    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        teams=TeamRepository.getInstance().getList();
        teamNumbers.setText(teams.size()+mContext.getString(R.string.teams));
        communityTopTeamsAdapter = new CommunityTopTeamsAdapter(mContext, teams);
        topTeamsRecyclerView.setAdapter(communityTopTeamsAdapter);
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}