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
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Team;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TournamentActivity extends AppCompatActivity implements IRepository {
//UI
    TextView location;
    Button joinBtn;
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
    Challenge challenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        tournementType=getIntent().getStringExtra("tournement");
       // tournementType="rapid";
        ChallengeRepository.getInstance().setiRepository(this);
        initUI();
        ChallengeRepository.getInstance().findByName(this,tournementType);
    }

    void initUI(){
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
        location=findViewById(R.id.location);
        joinBtn=findViewById(R.id.joinBtn);
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

    private void initUIRecyclerViewMatches(){
        if (challenge.getMatches().size()==0){
            Log.e("TAG", "initUIRecyclerViewMatches: "+"heloo" );
            matchRecyclerView.setVisibility(View.GONE);
            not_yet_match.setVisibility(View.VISIBLE);
        }else{
            matchRecyclerView.setVisibility(View.VISIBLE);
            not_yet_match.setVisibility(View.GONE);
            matchRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
           // TeamsAdapter teamsAdapter = new TeamsAdapter(this, (ArrayList<Team>) challenge.getTeams());
        //    recycleViewTeams.setAdapter(teamsAdapter);
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
    challenge= ChallengeRepository.getInstance().getElement();
        setTournement();
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
}