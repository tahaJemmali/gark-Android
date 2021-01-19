package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopPlayerFragment#} factory method to
 * create an instance of this fragment.
 */
public class TopPlayerFragment extends Fragment implements IRepository {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    ProgressDialog dialogg;
    //UI
    RecyclerView topPlayerRecyclerView;
    TextView playerNumbers;
    //VAR
    ArrayList<Skills> players;
    boolean playerGenerated=false;
    //Adapters
    CommunityTopPlayerAdapter communityTopPlayerAdapter;


    public TopPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_top_player, container, false);
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
        topPlayerRecyclerView=view.findViewById(R.id.topPlayerRecyclerView);
        playerNumbers=view.findViewById(R.id.playerNumbers);
        if (!playerGenerated){
            reloadData();
            playerGenerated=true;
        }else {
            playerNumbers.setText(players.size()+" players");
        }
        initUIRecycleViewerListPlayers();
    }
    private void initUIRecycleViewerListPlayers() {
        topPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        communityTopPlayerAdapter = new CommunityTopPlayerAdapter(mContext, players);
        topPlayerRecyclerView.setAdapter(communityTopPlayerAdapter);
    }
    void reloadData(){
        dialogg = ProgressDialog.show(mContext, "", "Loading Data ..Wait..", true);
        players=new  ArrayList<Skills>();
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().getAll(mContext,null);
    }
    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        players=SkillsRepository.getInstance().getList();
        playerNumbers.setText(players.size()+" players");
        communityTopPlayerAdapter = new CommunityTopPlayerAdapter(mContext, players);
        topPlayerRecyclerView.setAdapter(communityTopPlayerAdapter);
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }


}