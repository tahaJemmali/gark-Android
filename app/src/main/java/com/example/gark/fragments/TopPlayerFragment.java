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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    Spinner sort;
    //VAR
    static ArrayList<Skills> players;
    boolean playerGenerated=false;
    //Adapters
    CommunityTopPlayerAdapter communityTopPlayerAdapter;
    private static final String FRAGMENT_NAME = "topPlayer";

    public TopPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_top_player, container, false);
        mContext = getActivity();
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
        sort=view.findViewById(R.id.sort);
        //sort
        ArrayList<String> sortingTypes = new ArrayList<String>();
        sortingTypes.add("Score");
        sortingTypes.add("XP");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortingTypes);
        sort.setAdapter(adapter);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Collections.sort(players, new Comparator<Skills>() {
                        @Override
                        public int compare(Skills u1, Skills u2) {
                            Integer obj1 = u1.getScore();
                            Integer obj2 = u2.getScore();
                            int res = obj1.compareTo(obj2);
                            return res;
                        }
                    });
                }else {
                    Collections.sort(players, new Comparator<Skills>() {
                        @Override
                        public int compare(Skills u1, Skills u2) {
                            Integer obj1 = u1.getXp();
                            Integer obj2 = u2.getXp();
                            int res = obj1.compareTo(obj2);
                            return res;
                        }
                    });
                }
                communityTopPlayerAdapter.notifyDataSetChanged();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////
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
        dialogg = ProgressDialog.show(mContext, "","Loading" , true);
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