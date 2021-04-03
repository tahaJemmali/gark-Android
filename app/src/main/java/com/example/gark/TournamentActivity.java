package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gark.adapters.MatchAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.dialog.SelectTeamDialog;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.Match;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchRepository;
import com.example.gark.repositories.SkillsRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TournamentActivity extends AppCompatActivity implements IRepository,SelectTeamDialog.OnInputSelected{
//UI
    TextView location;
    Button subscribe;
    Button unsubscribe;
    Button join;
    Button dont;
    TextView teamNumber;
    RecyclerView recycleViewTeams;
    RecyclerView matchRecyclerView;
    RelativeLayout backgroundImage;
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
    com.applandeo.materialcalendarview.CalendarView calender_date_picker;
//Var
    String tournementType;
   public static Challenge challenge;
    public static Skills skill;
    Boolean generateMatches=true;
    Boolean parseDataInMatches=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        tournementType=getIntent().getStringExtra("tournement");
       // tournementType="rapid";
        initUI();
        ChallengeRepository.getInstance().setiRepository(this);
        ChallengeRepository.getInstance().findByName(this,tournementType);
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().findPlayerById(this, MainActivity.getCurrentLoggedInUser().getId());
    }

    void initUI(){
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
        location=findViewById(R.id.location);
        subscribe=findViewById(R.id.subscribe);
        unsubscribe=findViewById(R.id.unsubscribe);
        join=findViewById(R.id.join);
        dont=findViewById(R.id.dont);
        teamNumber=findViewById(R.id.teamNumber);
        recycleViewTeams=findViewById(R.id.recycleViewTeams);
        matchRecyclerView=findViewById(R.id.matchRecyclerView);
        backgroundImage=findViewById(R.id.backgroundImage);
        academy=findViewById(R.id.link);
        typeTournement=findViewById(R.id.typeTournement);
        description=findViewById(R.id.description);
        not_yet=findViewById(R.id.not_yet);
        not_yet_match=findViewById(R.id.not_yet_match);
         startday=findViewById(R.id.startday);
         endday=findViewById(R.id.endday);
        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
         calender_date_picker=findViewById(R.id.calender_date_picker);
    }

    private void initUIRecycleViewerTeams() {
        if (challenge.getTeams().size()==0){
            recycleViewTeams.setVisibility(View.GONE);
            not_yet.setVisibility(View.VISIBLE);
        }else{
            recycleViewTeams.setVisibility(View.VISIBLE);
            not_yet.setVisibility(View.GONE);
            recycleViewTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            TeamsAdapter teamsAdapter = new TeamsAdapter(this, (ArrayList<Team>) challenge.getTeams());
            recycleViewTeams.setAdapter(teamsAdapter);
        }
    }

    private void        initUIRecyclerViewMatches(){
        if (challenge.getMatches().size()==0){
            matchRecyclerView.setVisibility(View.GONE);
            not_yet_match.setVisibility(View.VISIBLE);
        }else{
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

    void setTournement(){
        location.setText(challenge.getLocation());
        academy.setText(challenge.getType().toString());
        teamNumber.setText(challenge.getTeams().size()+" / "+challenge.getMaxNumberOfTeams()+" Teams");
        description.setText(challenge.getDescription());
        Bitmap bitmap = getBitmapFromString(challenge.getImage());
        Drawable d = new BitmapDrawable(this.getResources(), bitmap);
        backgroundImage.setBackground(d);
        startday.setText(getDayName(challenge.getStart_date()));
        endday.setText(getDayName(challenge.getEnd_date()));
        startdate.setText(getDayNumber(challenge.getStart_date()));
        enddate.setText(getDayNumber(challenge.getEnd_date()));
        //calender
        List<Calendar> calendars = new ArrayList<>();
        calendars.add(dateToCalendar(challenge.getStart_date()));
        calendars.add(dateToCalendar(new Date()));
        calender_date_picker.setHighlightedDays(calendars);
        //calender
        initUIRecycleViewerTeams();
        initUIRecyclerViewMatches();
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    String getDayName(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayWeekText =  new SimpleDateFormat("EEEE" ).format(date);
        return  dayWeekText;
    }
    String getDayNumber(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dayWeekText =  new SimpleDateFormat("dd" ).format(date);
        return  dayWeekText;
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {

        skill=SkillsRepository.getInstance().getElement();
    challenge= ChallengeRepository.getInstance().getElement();
    if(skill!=null && challenge!=null){
        if(challenge.getTeams().size()==challenge.getMaxNumberOfTeams()){
            unsubscribe.setVisibility(View.GONE);
            subscribe.setVisibility(View.GONE);
            dont.setVisibility(View.GONE);
            join.setVisibility(View.GONE);
            if(generateMatches){
                if(challenge.getMatches().size()==0){
                    generateMatches();
                }else if(parseDataInMatches){
                    for (Match row:challenge.getMatches()){
                        if (challenge.getTeams().contains(row.getTeam1())){
                            row.setTeam1(challenge.getTeams().get(challenge.getTeams().indexOf(row.getTeam1())));
                        }
                        if (challenge.getTeams().contains(row.getTeam2())){
                            row.setTeam2(challenge.getTeams().get(challenge.getTeams().indexOf(row.getTeam2())));
                        }
                        initUIRecyclerViewMatches();
                        parseDataInMatches=false;
                    }
                }
                generateMatches=false;
            }
            if(MatchRepository.generator==(challenge.getMaxNumberOfTeams()/2)){
                MatchRepository.generator=0;
                dialogg.dismiss();
            }
        }else{
            for (Team row : challenge.getTeams()){
                if(skill.getTeams().contains(row)){
                    unsubscribe.setVisibility(View.VISIBLE);
                    subscribe.setVisibility(View.GONE);
                    dont.setVisibility(View.VISIBLE);
                    join.setVisibility(View.GONE);
                }
        }

            }
        setTournement();
    }
    }
    void generateMatches(){
        int numberOfMatches =challenge.getMaxNumberOfTeams()/2;
        Date start_date=challenge.getStart_date();
        ArrayList<Team> teams = (ArrayList<Team>) challenge.getTeams();
      //  MatchRepository.getInstance().setiRepository(this);
        for (int i=0;i<teams.size();i+=2){
            Match match=new Match(start_date, teams.get(i), teams.get(i+1));
            challenge.getMatches().add(match);
            if (challenge.getMatches().size()==(numberOfMatches/2)){
                Calendar c = Calendar.getInstance();
                c.setTime(start_date);
                c.add(Calendar.DATE, 1);
                start_date = c.getTime();
            }
        }
        MatchRepository.getInstance().setiRepository(this);
        dialogg.show();
        for (Match row : challenge.getMatches()){
            MatchRepository.getInstance().generateMatch(this,row,challenge);
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
        ChallengeRepository.getInstance().addTeamToChallenge(this,challenge.getId(),input.getId());
    }

    public void removeTeamfromtournement(View view) {
        Team team=null;
        for (Team row : challenge.getTeams()){
            if(skill.getTeams().contains(row)){
                team=row;
                challenge.getTeams().remove(row);
                break;
            }
        }
        if (team !=null){
            ChallengeRepository.getInstance().removeTeamFromChallenge(this,challenge.getId(),team.getId());
            unsubscribe.setVisibility(View.GONE);
            subscribe.setVisibility(View.VISIBLE);
            dont.setVisibility(View.GONE);
            join.setVisibility(View.VISIBLE);
        }
    }
}