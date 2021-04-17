package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gark.adapters.MatchAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.dialog.SelectTeamDialog;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchType;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchRepository;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class TournamentActivity extends AppCompatActivity implements IRepository, SelectTeamDialog.OnInputSelected {
    //UI
    TextView location;
    Button subscribe;
    Button unsubscribe;
    Button join;
    Button dont;
    Button finish;
    TextView teamNumber;
    RecyclerView recycleViewTeams;
    RecyclerView matchRecyclerView;
    ImageView backgroundImage;
    TextView academy;
    TextView typeTournement;
    TextView description;
    ProgressDialog dialogg;
    TextView not_yet;
    TextView not_yet_match;
    TextView startday;
    TextView endday;
    TextView startdate;
    TextView enddate;
    TextView locationIn;
    TextView challengeFinished;
    MaterialCalendarView calender_date_picker;
    //Var
    String tournementType;
    public static Challenge challenge;
    public static Skills skill;
    boolean fetchingTournement=false;
    boolean removeTeamFromMatches=false;
    boolean allowFinishingChallenge=false;
    boolean updatedChallenge=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        tournementType = getIntent().getStringExtra("tournement");
        initUI();
        ChallengeRepository.getInstance().setiRepository(this);
        ChallengeRepository.getInstance().findByName(this, tournementType);
        fetchingTournement=true;
    }

    void initUI() {
        dialogg = ProgressDialog.show(this, "","Loading" , true);
        finish = findViewById(R.id.finish);
        challengeFinished = findViewById(R.id.challengeFinished);
        location = findViewById(R.id.location);
        subscribe = findViewById(R.id.subscribe);
        unsubscribe = findViewById(R.id.unsubscribe);
        join = findViewById(R.id.join);
        dont = findViewById(R.id.dont);
        teamNumber = findViewById(R.id.teamNumber);
        recycleViewTeams = findViewById(R.id.recycleViewTeams);
        matchRecyclerView = findViewById(R.id.matchRecyclerView);
        backgroundImage = findViewById(R.id.backgroundImage);
        academy = findViewById(R.id.link);
        typeTournement = findViewById(R.id.typeTournement);
        description = findViewById(R.id.description);
        not_yet = findViewById(R.id.not_yet);
        not_yet_match = findViewById(R.id.not_yet_match);
        startday = findViewById(R.id.startday);
        endday = findViewById(R.id.endday);
        locationIn = findViewById(R.id.locationIn);
        startdate = findViewById(R.id.startdate);
        enddate = findViewById(R.id.enddate);
        calender_date_picker = findViewById(R.id.calender_date_picker);

    }

    private void initUIRecycleViewerTeams() {
        if (challenge.getTeams().size() == 0) {
            recycleViewTeams.setVisibility(View.GONE);
            not_yet.setVisibility(View.VISIBLE);
        } else {
            recycleViewTeams.setVisibility(View.VISIBLE);
            not_yet.setVisibility(View.GONE);
            recycleViewTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            TeamsAdapter teamsAdapter = new TeamsAdapter(this, (ArrayList<Team>) challenge.getTeams());
            recycleViewTeams.setAdapter(teamsAdapter);
        }
    }

    private void initUIRecyclerViewMatches() {
        if (challenge.getMatches().size() == 0) {
            matchRecyclerView.setVisibility(View.GONE);
            not_yet_match.setVisibility(View.VISIBLE);
        } else {
            matchRecyclerView.setVisibility(View.VISIBLE);
            not_yet_match.setVisibility(View.GONE);
            matchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            MatchAdapter matchAdapter = new MatchAdapter(this, (ArrayList<Match>) challenge.getMatches());
            matchRecyclerView.setAdapter(matchAdapter);
        }
    }

    private Bitmap getBitmapFromString(String image) {
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    void setTournement() {
        location.setText(challenge.getLocation());
        academy.setText(challenge.getType().toString());
        typeTournement.setText(challenge.getName());
        locationIn.setText(challenge.getLocation());
        teamNumber.setText(challenge.getTeams().size() + " / " + challenge.getMaxNumberOfTeams() + " Teams");
        description.setText(challenge.getDescription());
        Bitmap bitmap = getBitmapFromString(challenge.getImage());
        Drawable d = new BitmapDrawable(this.getResources(), bitmap);
        backgroundImage.setImageBitmap(bitmap);
        startday.setText(getDayName(challenge.getStart_date()));
        endday.setText(getDayName(challenge.getEnd_date()));
        startdate.setText(getDayNumber(challenge.getStart_date()));
        enddate.setText(getDayNumber(challenge.getEnd_date()));
        //calender
        // disable dates before today
        calender_date_picker.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        for(Match row: challenge.getMatches()){
            calender_date_picker.setDateSelected(dateToCalendarDay(row.getStart_date()),true);
        }
        initUIRecyclerViewMatches();
        initUIRecycleViewerTeams();
    }


    private CalendarDay dateToCalendarDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
       return CalendarDay.from(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    String getDayName(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayWeekText = new SimpleDateFormat("EEEE").format(date);
        return dayWeekText;
    }

    String getDayNumber(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayWeekText = new SimpleDateFormat("dd").format(date);
        return dayWeekText;
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        if(!updatedChallenge){
            if (fetchingTournement){
                skill = MainActivity.currentPlayerSkills;

                challenge = ChallengeRepository.getInstance().getElement();
                fetchingTournement=false;
                verifyCurrentUserTeamExistance();
                fetchImagesInMatches();
                if(challenge.getState().equals(ChallengeState.Finished)){
                    finish.setVisibility(View.GONE);
                    challengeFinished.setVisibility(View.VISIBLE);
                    challengeFinished.setText(challengeFinished.getText()+challenge.getWinner().getName());
                }else {
                    if(MainActivity.getCurrentLoggedInUser().getId().equals(challenge.getCreator().getId())&&allowFinishingChallenge){
                        finish.setVisibility(View.VISIBLE);
                    }else {
                        finish.setVisibility(View.GONE);
                    }
                }

            }
            if (challenge.getTeams().size() == challenge.getMaxNumberOfTeams()) {
                unsubscribe.setVisibility(View.GONE);
                subscribe.setVisibility(View.GONE);
                dont.setVisibility(View.GONE);
                join.setVisibility(View.GONE);
            }
            if(MatchRepository.generator==(challenge.getMaxNumberOfTeams()/2)){
                MatchRepository.generator=0;
                initUIRecyclerViewMatches();
                dialogg.dismiss();
            }
            setTournement();
        }else {
            Toast.makeText(this,"Your challenge editing have been saved sucessfully !",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    void verifyCurrentUserTeamExistance(){
        for (Team row : challenge.getTeams()) {
            if (skill.getTeams().contains(row)) {
                unsubscribe.setVisibility(View.VISIBLE);
                subscribe.setVisibility(View.GONE);
                dont.setVisibility(View.VISIBLE);
                join.setVisibility(View.GONE);
            }
        }
    }

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    void fetchImagesInMatches(){
        int j=0;
        for (int i = 0; i < challenge.getTeams().size()-1; i+=2) {
            challenge.getMatches().get(j).setTeam1(challenge.getTeams().get(i));
            challenge.getMatches().get(j).setTeam2(challenge.getTeams().get(i + 1));
            j++;
        }
        //FetchQuarterFinal semi final and final
        fetchRestOfGames();

        if(removeTeamFromMatches){
            challenge.getMatches().get(j).setTeam1(null);
            challenge.getMatches().get(j).setTeam2(null);
            removeTeamFromMatches=false;
        }
        initUIRecyclerViewMatches();
    }

    void fetchRestOfGames(){
        //Quarter
        ArrayList<Team> winners=new ArrayList<Team>();
        for(Match row : challenge.getMatches()){
            if(!Objects.isNull(row.getWinner())&&row.getType().equals(MatchType.Round)){
                int indexTeam=challenge.getTeams().indexOf(row.getWinner());
                winners.add(challenge.getTeams().get(indexTeam));
            }
        }
        int matchIndex = winners.size();
        for(int i=0; i<(winners.size()/2);i++){
            boolean updateMatch=false;
            if(Objects.isNull(challenge.getMatches().get(i+matchIndex).getTeam1()))
                updateMatch=true;
            challenge.getMatches().get(i+matchIndex).setTeam1(winners.get(i));
            challenge.getMatches().get(i+matchIndex).setTeam2(winners.get(i+1));
            matchIndex+=i;
            if(updateMatch){
                updateMatch=false;
                challenge.getMatches().get(i+matchIndex).setEnd_date(addHoursToJavaUtilDate(challenge.getMatches().get(i+matchIndex).getStart_date(), 2));
                MatchRepository.getInstance().updateQuarter(this, challenge.getMatches().get(i+matchIndex), challenge.getMatches().get(i+matchIndex).getId(), challenge);
            }
        }
        //Semi
        winners.clear();
        for(Match row : challenge.getMatches()){
            if(!Objects.isNull(row.getWinner())&&row.getType().equals(MatchType.Quarter)){
                int indexTeam=challenge.getTeams().indexOf(row.getWinner());
                winners.add(challenge.getTeams().get(indexTeam));
            }
        }
        matchIndex ++;
        for(int i=0; i<(winners.size()/2);i++){
            boolean updateMatch=false;
            if(Objects.isNull(challenge.getMatches().get(i+matchIndex).getTeam1()))
                updateMatch=true;
            challenge.getMatches().get(i+matchIndex).setTeam1(winners.get(i));
            challenge.getMatches().get(i+matchIndex).setTeam2(winners.get(i+1));
            matchIndex+=i;
            if(updateMatch){
                challenge.getMatches().get(i+matchIndex).setEnd_date(addHoursToJavaUtilDate(challenge.getMatches().get(i+matchIndex).getStart_date(), 2));
                MatchRepository.getInstance().updateQuarter(this, challenge.getMatches().get(i+matchIndex), challenge.getMatches().get(i+matchIndex).getId(), challenge);
            }
        }
        //Final
        matchIndex ++;
        if(challenge.getMatches().size()==matchIndex){
            winners.clear();
            for(Match row : challenge.getMatches()){
                if(!Objects.isNull(row.getWinner())&&row.getType().equals(MatchType.Semi)){
                    int indexTeam=challenge.getTeams().indexOf(row.getWinner());
                    winners.add(challenge.getTeams().get(indexTeam));
                }
            }
            int index =challenge.getMatches().size()-2;
            boolean updateMatch=false;
            if(Objects.isNull(challenge.getMatches().get(index+1).getTeam1()))
                updateMatch=true;
            challenge.getMatches().get(index+1).setTeam1(winners.get(winners.size()-2));
            challenge.getMatches().get(index+1).setTeam2(winners.get(winners.size()-1));
            if(updateMatch){
                challenge.getMatches().get(index+1).setEnd_date(addHoursToJavaUtilDate(challenge.getMatches().get(index+1).getStart_date(), 2));
                MatchRepository.getInstance().updateQuarter(this, challenge.getMatches().get(index+1), challenge.getMatches().get(index+1).getId(), challenge);
            }

                if(!Objects.isNull(challenge.getMatches().get(matchIndex-1).getWinner())){
                    allowFinishingChallenge=true;
                }
        }
    }

    void generateMatchesNow() {
        MatchRepository.getInstance().setiRepository(this);
        dialogg.show();
        int j=0;
        for (int i = 0; i < challenge.getTeams().size()-1; i+=2) {
            challenge.getMatches().get(j).setTeam1(challenge.getTeams().get(i));
            challenge.getMatches().get(j).setTeam2(challenge.getTeams().get(i + 1));
            challenge.getMatches().get(j).setEnd_date(addHoursToJavaUtilDate(challenge.getMatches().get(j).getStart_date(), 2));
            MatchRepository.getInstance().update(this, challenge.getMatches().get(j), challenge.getMatches().get(j).getId(), challenge);
            j++;
        }
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    public void addTeamTotournement(View view) {
        SelectTeamDialog dialog = new SelectTeamDialog();
        dialog.show(getSupportFragmentManager(), "SelectTeamDialog");
    }

    @Override
    public void sendInput(Team input) {
        challenge.getTeams().add(input);
        verifyCurrentUserTeamExistance();
        if(challenge.getTeams().size()==challenge.getMaxNumberOfTeams()){
            generateMatchesNow();
        }else{
            fetchImagesInMatches();
        }
        ChallengeRepository.getInstance().addTeamToChallenge(this, challenge.getId(), input.getId());
    }


    public void finishChallenge(View view) {
        challenge.setWinner(challenge.getMatches().get(challenge.getMatches().size()-1).getWinner());
        challenge.setState(ChallengeState.Finished);
        updatedChallenge=true;
        ChallengeRepository.getInstance().update(this,challenge,challenge.getId());
        }

    public void removeTeamfromtournement(View view) {
        Team team = null;
        for (Team row : challenge.getTeams()) {
            if (skill.getTeams().contains(row)) {
                team = row;
                challenge.getTeams().remove(row);
                break;
            }
        }
        removeTeamFromMatches=true;
        if (team != null) {
            ChallengeRepository.getInstance().removeTeamFromChallenge(this, challenge.getId(), team.getId());
            unsubscribe.setVisibility(View.GONE);
            subscribe.setVisibility(View.VISIBLE);
            dont.setVisibility(View.GONE);
            join.setVisibility(View.VISIBLE);
        }
        fetchImagesInMatches();
    }
}