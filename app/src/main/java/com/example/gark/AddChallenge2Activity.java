package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gark.adapters.AddMatchAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.dialog.AddMatchDialog;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchType;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchRepository;

import java.util.ArrayList;

public class AddChallenge2Activity extends AppCompatActivity implements IRepository, AddMatchDialog.OnInputSelected {
    //WIDGET
    ProgressDialog dialogg;
    RecyclerView recycleViewListMatches;
    //VAR
    Challenge challenge;
    ArrayList<Match> matches;
    AddMatchAdapter addMatchAdapter;
    Boolean generat=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge2);
        initUI();

    }
    void initUI(){
        challenge= (Challenge) getIntent().getSerializableExtra("challenge");
        recycleViewListMatches=findViewById(R.id.recycleViewListMatches);
        generateMatches();
        initAddMatchRecyclerView();
    }

    void generateMatches(){
        matches=new ArrayList<Match>();
        int numberOfMatches =challenge.getMaxNumberOfTeams()/2;
        for (int i=0;i<numberOfMatches;i++){
            Match match=new Match();
            match.setState(ChallengeState.Pending);
            match.setType(MatchType.Round);
            matches.add(match);
        }
        int numberOfMatchesEnd =numberOfMatches/2;
        if(numberOfMatchesEnd==4){
            for (int i=0;i<numberOfMatchesEnd;i++){
                Match match=new Match();
                match.setState(ChallengeState.Pending);
                match.setType(MatchType.Quarter);
                matches.add(match);
            }
            numberOfMatchesEnd=numberOfMatchesEnd/2;
        }
        for (int i=0;i<numberOfMatchesEnd;i++){
            Match match=new Match();
            match.setState(ChallengeState.Pending);
            match.setType(MatchType.Semi);
            matches.add(match);
        }
        numberOfMatchesEnd=numberOfMatchesEnd/2;
        for (int i=0;i<numberOfMatchesEnd;i++){
            Match match=new Match();
            match.setState(ChallengeState.Pending);
            match.setType(MatchType.Final);
            matches.add(match);
        }
    }

    void initAddMatchRecyclerView(){
        recycleViewListMatches.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addMatchAdapter = new AddMatchAdapter(this, matches);
        recycleViewListMatches.setAdapter(addMatchAdapter);
    }
    public void addChallenge(View view) {
        if(validator()){
            challenge.setMatches(matches);
            challenge.setEnd_date(challenge.getMatches().get(0).getStart_date());
            challenge.setEnd_date(challenge.getMatches().get(challenge.getMaxNumberOfTeams()-2).getStart_date());
            MatchRepository.getInstance().setiRepository(this);
            dialogg = ProgressDialog.show(this, "", "Loading Data ..Wait..", true);
            ChallengeRepository.getInstance().setiRepository(this);
            ChallengeRepository.getInstance().add(this,challenge,null);


        }
    }

    boolean validator (){
        int i=0;
        for (Match row : matches){
            i++;
            if(row.getStart_date()==null){
                Toast.makeText(this,"You didn't specify the start date for the match N°"+i,Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return  true;
    }

    public void getBack(View view) {
        super.onBackPressed();
    }
    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {

        if (!ChallengeRepository.getInstance().getAddedChallengeId().isEmpty()) {
            challenge.setId(ChallengeRepository.getInstance().getAddedChallengeId());
            if (generat){
                for (Match row : challenge.getMatches()){
                    MatchRepository.getInstance().generateMatch(this,row,challenge);
                }
                generat=false;
            }

            if(MatchRepository.generatorMatches==(challenge.getMaxNumberOfTeams()-1)){
                MatchRepository.generatorMatches=0;
                Toast.makeText(AddChallenge2Activity.this,"Challenge Added Sucessfully !",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    @Override
    public void sendInput() {
        Log.e("TAG", "sendInput: " );
        initAddMatchRecyclerView();
    }
}