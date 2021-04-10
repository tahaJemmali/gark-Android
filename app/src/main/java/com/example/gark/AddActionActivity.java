package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gark.adapters.MatchActionAdapter;
import com.example.gark.adapters.MatchPlayerAdapter;
import com.example.gark.dialog.AddMatchActionDialog;
import com.example.gark.dialog.AddMatchDialog;
import com.example.gark.dialog.VoteDialog;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchActionType;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchActionRepository;
import com.example.gark.repositories.MatchRepository;

import java.util.ArrayList;

public class AddActionActivity extends AppCompatActivity implements  AddMatchActionDialog.OnInputSelected, IRepository {
//UI
ProgressDialog dialogg;
RecyclerView matchActionTeam1RV;
RecyclerView matchActionTeam2RV;
LinearLayout linearLayout8;
TextView not_yet;
Spinner teamSpinner;
//VAR
    MatchActionAdapter matchActionAdapterTeam1;
    MatchActionAdapter matchActionAdapterTeam2;
    ArrayList<MatchAction> matchActionsTeam1;
    ArrayList<MatchAction> matchActionsTeam2;
    Match match;
    Challenge challenge;
    ArrayList<MatchAction> tmp;
    boolean matchUpdated=false;
    @Override
    public void sendInput(MatchAction matchAction) {
        linearLayout8.setVisibility(View.VISIBLE);
        not_yet.setVisibility(View.GONE);
        Log.e("TAG", "sendInput: "+matchAction );
        if(matchAction.getTeam().equals(match.getTeam1())){
            matchActionsTeam1.add(matchAction);
            matchActionAdapterTeam1.notifyDataSetChanged();
        }else {
            matchActionsTeam2.add(matchAction);
            matchActionAdapterTeam2.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);
        initUI();
    }

    void initUI(){
        match= MatchActivity.match;
        challenge= TournamentActivity.challenge;
        //SET TEAM SPINNER
        teamSpinner=findViewById(R.id.teamSpinner);
        ArrayList<String> teams = new ArrayList<String>();
        teams.add(match.getTeam1().getName());
        teams.add(match.getTeam2().getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(adapter);

        linearLayout8=findViewById(R.id.linearLayout8);
        linearLayout8.setVisibility(View.GONE);
        not_yet=findViewById(R.id.not_yet);
        matchActionTeam1RV=findViewById(R.id.matchActionTeam1);
        matchActionTeam2RV=findViewById(R.id.matchActionTeam2);
        matchActionsTeam1=new ArrayList<MatchAction>();
        matchActionsTeam2=new ArrayList<MatchAction>();
        initmatchActionTeam1RV();
        initmatchActionTeam2RV();
    }
    public void getBack(View view) {
        super.onBackPressed();
    }

    void initmatchActionTeam1RV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        matchActionTeam1RV.setLayoutManager(layoutManager);
        matchActionAdapterTeam1 = new MatchActionAdapter(this,matchActionsTeam1,match,true);
        matchActionTeam1RV.setAdapter(matchActionAdapterTeam1);
    }
    void initmatchActionTeam2RV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        matchActionTeam2RV.setLayoutManager(layoutManager);
        matchActionAdapterTeam2 = new MatchActionAdapter(this,matchActionsTeam2,match,true);
        matchActionTeam2RV.setAdapter(matchActionAdapterTeam2);
    }
    public void finish(View view) {
        dialogg = ProgressDialog.show(this, "","Loading" , true);
        if(teamSpinner.getSelectedItemPosition()==0){
            match.setWinner(match.getTeam1());
        }else {
            match.setWinner(match.getTeam2());
        }
        match.setState(ChallengeState.Finished);
        tmp =new ArrayList<MatchAction>();
        tmp.addAll(matchActionsTeam1);
        tmp.addAll(matchActionsTeam2);
        for(MatchAction row : tmp){
            MatchActionRepository.getInstance().setiRepository(this);
            MatchActionRepository.getInstance().add(this,row,null);
        }
    }

    public void addMatchActions(View view) {
        AddMatchActionDialog dialog = new AddMatchActionDialog(match,challenge,match.getStart_date());
        dialog.show( getSupportFragmentManager(), "AddMatchActionDialog");
    }


    @Override
    public void showLoadingButton() {
dialogg.show();
    }

    @Override
    public void doAction() {
        if(!matchUpdated)
        tmp.get(MatchActionRepository.generatedMatchAction).setId(MatchActionRepository.id);

        if(tmp.size()-1==MatchActionRepository.generatedMatchAction){
            match.setGoals(new ArrayList<MatchAction>());
            match.setRedCards(new ArrayList<MatchAction>());
            match.setYellowCards(new ArrayList<MatchAction>());
            for(MatchAction row : tmp){
                if(row.getType().equals(MatchActionType.goal)){
                    match.getGoals().add(row);
                }else if (row.getType().equals(MatchActionType.yellowCard)){
                    match.getYellowCards().add(row);
                }else{
                    match.getRedCards().add(row);
                }
            }
            MatchRepository.getInstance().setiRepository(this);
            MatchRepository.getInstance().update(this,match,match.getId(),challenge);
            matchUpdated=true;
        }

        if(matchUpdated){
            Toast.makeText(AddActionActivity.this,"Your match editing have been saved sucessfully !",Toast.LENGTH_LONG).show();
            super.onBackPressed();
            finish();
        }

    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}