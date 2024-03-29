package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.example.gark.FavorisTeamActivity;
import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.adapters.CommunityTopTeamsAdapter;

import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //UI
    RadioButton buttonTeams;
    EditText search_bar_community;
    RadioButton buttonPlayers;
    ImageButton showFavoirs;
    //VAR
    ArrayList<Skills> searchPlayers;
    ArrayList<Team> searchTeams;
    //fragment
    TopPlayerFragment topPlayerFragment;
    TopTeamFragment topTeamFragment;
    //already generated
    boolean playerGenerated=false;
    boolean teamGenerated=false;
    ///statitc bool for mainActivity smooth navigation
    public static boolean showAllTeams=true;
    public static boolean showAllPlayer=false;
    private static final String FRAGMENT_NAME = "community";

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_community, container, false);
        mContext = getContext();
        initUI();

        if (showAllPlayer)
            addTopPlayerFragment();
        if(showAllTeams)
            addTopTeamFragment();

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
        buttonPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllTeams=false;
                showAllPlayer=true;
                addTopPlayerFragment();
            }
        });
        buttonTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllTeams=true;
                showAllPlayer=false;
                addTopTeamFragment();
            }
        });
        return view;
    }

    void initUI() {
        fragmentManager = getChildFragmentManager();
        buttonTeams =view.findViewById(R.id.buttonTeams);
        buttonPlayers =view.findViewById(R.id.buttonPlayers);
        search_bar_community=view.findViewById(R.id.search_bar_community);
        showFavoirs=view.findViewById(R.id.showFavoirs);
        showFavoirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavorisTeamActivity.class);
                getActivity().startActivity(intent);
            }
        });

        searchTeams=new ArrayList<Team>();
        searchPlayers=new ArrayList<Skills>();
        search_bar_community.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                find(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        addTopTeamFragment();
    }

    void find(CharSequence charSequence){
        if (showAllTeams){
            searchTeams.clear();
            for (Team row: TopTeamFragment.teams){
                if (row.getName().contains(charSequence)){
                    searchTeams.add(row);
                }
            }
            topTeamFragment.teamNumbers.setText(searchTeams.size()+getString(R.string.teams));
                topTeamFragment.communityTopTeamsAdapter =  new CommunityTopTeamsAdapter(mContext, searchTeams);

            topTeamFragment.topTeamsRecyclerView.setAdapter(topTeamFragment.communityTopTeamsAdapter);
        }else{
            searchPlayers.clear();
            for (Skills row: TopPlayerFragment.players){
                if (row.getPlayer().getFirstName().contains(charSequence)||row.getPlayer().getLastName().contains(charSequence)){
                    searchPlayers.add(row);
                }
            }
            topPlayerFragment.playerNumbers.setText(searchPlayers.size()+getString(R.string.teams));
            topPlayerFragment.communityTopPlayerAdapter =  new CommunityTopPlayerAdapter(mContext, searchPlayers);
            topPlayerFragment.topPlayerRecyclerView.setAdapter(topPlayerFragment.communityTopPlayerAdapter);
        }

    }


    public void addTopPlayerFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        buttonTeams.setChecked(false);
        buttonPlayers.setChecked(true);
        if (!playerGenerated){
            topPlayerFragment = new TopPlayerFragment();
            playerGenerated=true;
        }
        fragmentTransaction.replace(R.id.community_fragment_container,topPlayerFragment);
        fragmentTransaction.commit();

    }
    public void addTopTeamFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        buttonTeams.setChecked(true);
        buttonPlayers.setChecked(false);
        if(!teamGenerated){
           topTeamFragment = new TopTeamFragment();
            teamGenerated=true;
        }
        fragmentTransaction.replace(R.id.community_fragment_container,topTeamFragment);
        fragmentTransaction.commit();
    }


    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}