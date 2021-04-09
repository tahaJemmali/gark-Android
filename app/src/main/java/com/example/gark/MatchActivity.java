package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gark.adapters.MatchActionAdapter;
import com.example.gark.adapters.MatchAdapter;
import com.example.gark.adapters.MatchPlayerAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchVote;
import com.example.gark.entites.Skills;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchRepository;
import com.example.gark.repositories.MatchVoteRepository;
import com.example.gark.repositories.TeamRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MatchActivity extends AppCompatActivity implements IRepository {
    //UI
    ProgressDialog dialogg;
    TextView tournementName;
    TextView typeMatch;
    ImageView team1Image;
    TextView team1goals;
    ImageView team2Image;
    TextView team2goals;
    TextView end_date;
    RecyclerView matchActionTeam1RV;
    RecyclerView matchActionTeam2RV;
    TextView not_yet;
    Button setGameAction;
    RecyclerView team1PlayersRV;
    RecyclerView team2PlayersRV;

    //VAR
    public static Match match;
    ArrayList<Skills> team1Players;
    ArrayList<Skills> team2Players;
    int teamGeneration=0;
    MatchPlayerAdapter matchPlayerAdapter;
    int allowVote=1;
    public static String currentPlayerTeam="";
    public static ArrayList<MatchVote> matchVotes;
    ArrayList<MatchAction> matchActionsTeam1;
    ArrayList<MatchAction> matchActionsTeam2;
    MatchActionAdapter matchActionAdapterTeam1;
    MatchActionAdapter matchActionAdapterTeam2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        initUI();
    }
    void initUI(){
        tournementName=findViewById(R.id.tournementName);
        typeMatch=findViewById(R.id.typeMatch);
        team1Image=findViewById(R.id.team1Image);
        team1goals=findViewById(R.id.team1goals);
        team2Image=findViewById(R.id.team2Image);
        team2goals=findViewById(R.id.team2goals);
        end_date=findViewById(R.id.end_date);
        matchActionTeam1RV=findViewById(R.id.matchActionTeam1);
        matchActionTeam2RV=findViewById(R.id.matchActionTeam2);
        not_yet=findViewById(R.id.not_yet);
        setGameAction=findViewById(R.id.setGameAction);
        team1PlayersRV=findViewById(R.id.team1PlayersRV);
        team2PlayersRV=findViewById(R.id.team2PlayersRV);
        match=MatchAdapter.selectedMatch;
        matchActionsTeam1=new ArrayList<MatchAction>();
        matchActionsTeam2=new ArrayList<MatchAction>();
        dialogg = ProgressDialog.show(this, "", "Loading Data ..Wait..", true);
        if(!Objects.isNull(match.getTeam1())){
            TeamRepository.getInstance().setiRepository(this);
            TeamRepository.getInstance().findById(this,match.getTeam1().getId());
        }else {
            matchActionTeam1RV.setVisibility(View.GONE);
            matchActionTeam2RV.setVisibility(View.GONE);
            team1PlayersRV.setVisibility(View.GONE);
            team2PlayersRV.setVisibility(View.GONE);
            setGameAction.setVisibility(View.GONE);
            not_yet.setVisibility(View.VISIBLE);
            tournementName.setText(TournamentActivity.challenge.getName());
            typeMatch.setText(match.getType().toString());
            team1goals.setText("");
            team2goals.setText("");
            end_date.setText(match.getState().toString());
            dialogg.dismiss();
        }


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
        switch (teamGeneration){
            case 0:
                match.setTeam1(TeamRepository.getInstance().getElement());
                team1Players=new ArrayList<Skills>();
                team1Players.add(match.getTeam1().getCapitaine());
                for(Skills row:match.getTeam1().getTitulares()){
                    team1Players.add(row);
                }
                for(Skills row:match.getTeam1().getSubstitutes()){
                    team1Players.add(row);
                }
                TeamRepository.getInstance().findById(this,match.getTeam2().getId());
                teamGeneration++;
                break;
            case 1:
                match.setTeam2(TeamRepository.getInstance().getElement());
                team2Players=new ArrayList<Skills>();
                team2Players.add(match.getTeam2().getCapitaine());
                for(Skills row:match.getTeam2().getTitulares()){
                    team2Players.add(row);
                }
                for(Skills row:match.getTeam2().getSubstitutes()){
                    team2Players.add(row);
                }
                teamGeneration++;
                MatchVoteRepository.getInstance().setiRepository(this);
                MatchVoteRepository.getInstance().findVotesByVoter(this,MainActivity.currentPlayerSkills.getId());
                break;
            case 2:
                matchVotes=new ArrayList<MatchVote>();
                matchVotes= MatchVoteRepository.getInstance().getList();
                teamGeneration++;
               // validate();
                setActivity();
                break;
        }
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    void setActivity(){
         tournementName.setText(TournamentActivity.challenge.getName());
         typeMatch.setText(match.getType().toString());
        Bitmap bitmap = getBitmapFromString(match.getTeam1().getImage());
         team1Image.setImageBitmap(bitmap);
        bitmap = getBitmapFromString(match.getTeam2().getImage());
        team2Image.setImageBitmap(bitmap);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        if (match.getState().equals(ChallengeState.Finished)) {
            not_yet.setVisibility(View.GONE);
            setGameAction.setVisibility(View.GONE);
            int team1goalsNb=0;
            int team2goalsNb=0;
            for (MatchAction row:match.getGoals()){
                row.setMatch(match);
                row.setChallenge(TournamentActivity.challenge);
                if(row.getTeam().equals(match.getTeam1())){
                    row.setTeam(match.getTeam1());
                        if(team1Players.contains(row.getPlayer()))
                            row.setPlayer(team1Players.get(team1Players.indexOf(row.getPlayer())));
                    matchActionsTeam1.add(row);

                    team1goalsNb++;
                }else if (row.getTeam().equals(match.getTeam2())){
                    row.setTeam(match.getTeam2());
                    if(team2Players.contains(row.getPlayer()))
                        row.setPlayer(team1Players.get(team2Players.indexOf(row.getPlayer())));
                    matchActionsTeam2.add(row);

                    team2goalsNb++;
                }
            }
            populateMatchActions();
            team1goals.setText(""+team1goalsNb);
            team2goals.setText(""+team2goalsNb);

            end_date.setText(formatter.format(match.getEnd_date()));
            if(match.getGoals().size()+match.getRedCards().size()+match.getYellowCards().size()>0){
                initmatchActionTeam1RV();
                initmatchActionTeam2RV();
            }
        }else{
            team1goals.setText("");
            team2goals.setText("");
            end_date.setText(match.getState().toString()+" scheduled for "+formatter.format(match.getStart_date()));
            not_yet.setVisibility(View.VISIBLE);
            matchActionTeam1RV.setVisibility(View.GONE);
            matchActionTeam2RV.setVisibility(View.GONE);
        }
        if(MainActivity.getCurrentLoggedInUser().getId().equals(TournamentActivity.challenge.getCreator().getId())){
            setGameAction.setVisibility(View.VISIBLE);
        }else {
            setGameAction.setVisibility(View.GONE);
        }
        initTeam1PlayerRV();
        initTeam2PlayerRV();
        dialogg.dismiss();
    }
    void populateMatchActions(){
        for (MatchAction row:match.getYellowCards()){
            row.setMatch(match);
            row.setChallenge(TournamentActivity.challenge);
            if(row.getTeam().equals(match.getTeam1())){
                row.setTeam(match.getTeam1());
                if(team1Players.contains(row.getPlayer()))
                    row.setPlayer(team1Players.get(team1Players.indexOf(row.getPlayer())));
                matchActionsTeam1.add(row);


            }else if (row.getTeam().equals(match.getTeam2())){
                row.setTeam(match.getTeam2());
                if(team2Players.contains(row.getPlayer()))
                    row.setPlayer(team1Players.get(team2Players.indexOf(row.getPlayer())));
                matchActionsTeam2.add(row);

            }
        }
        for (MatchAction row:match.getRedCards()){
            row.setMatch(match);
            row.setChallenge(TournamentActivity.challenge);
            if(row.getTeam().equals(match.getTeam1())){
                row.setTeam(match.getTeam1());
                matchActionsTeam1.add(row);

            }else if (row.getTeam().equals(match.getTeam2())){
                row.setTeam(match.getTeam2());
                if(team2Players.contains(row.getPlayer()))
                    row.setPlayer(team1Players.get(team2Players.indexOf(row.getPlayer())));
                matchActionsTeam2.add(row);

            }
        }
    }
    void initTeam1PlayerRV(){
        team1PlayersRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        matchPlayerAdapter = new MatchPlayerAdapter(this,team1Players,match.getTeam1().getCapitaine().getId(),allowVote,match.getTeam1() );
        team1PlayersRV.setAdapter(matchPlayerAdapter);
    }
    void initTeam2PlayerRV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        team2PlayersRV.setLayoutManager(layoutManager);
        matchPlayerAdapter = new MatchPlayerAdapter(this,team2Players,match.getTeam2().getCapitaine().getId(),allowVote,match.getTeam2() );
        team2PlayersRV.setAdapter(matchPlayerAdapter);
    }
    void initmatchActionTeam1RV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        matchActionTeam1RV.setLayoutManager(layoutManager);
        matchActionAdapterTeam1 = new MatchActionAdapter(this,matchActionsTeam1,match,false);
        matchActionTeam1RV.setAdapter(matchActionAdapterTeam1);
    }
    void initmatchActionTeam2RV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        matchActionTeam2RV.setLayoutManager(layoutManager);
        matchActionAdapterTeam2 = new MatchActionAdapter(this,matchActionsTeam2,match,false);
        matchActionTeam2RV.setAdapter(matchActionAdapterTeam2);
    }
    @Override
    public void dismissLoadingButton() {

    }

    public void setGameActions(View view) {
        Intent intent=new Intent(this,AddActionActivity.class);
        startActivity(intent);
        finish();
    }
}