package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gark.entites.Challenge;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class TournamentActivity extends AppCompatActivity implements IRepository {
//UI
    TextView location;
    Button joinBtn;
    TextView teamNumber;
    RecyclerView recycleViewTeams;
    RecyclerView matchRecyclerView;
    RelativeLayout backgroundImage;
    TextView link;
    TextView typeTournement;
    ProgressDialog dialogg;
//Var
    String tournementType;
    Challenge challenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        tournementType=getIntent().getStringExtra("tournement");
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
        link=findViewById(R.id.link);
        typeTournement=findViewById(R.id.typeTournement);

    }

    void setTournement(String tournement){
        location.setText(challenge.getLocation());
        link.setText(challenge.getDescription());
        teamNumber.setText(challenge.getTeams().size()+" / "+challenge.getMaxNumberOfTeams()+" Teams");

    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
    challenge= ChallengeRepository.getInstance().getElement();
        Log.e("TAG", "doAction: "+challenge );
        setTournement(tournementType);
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
}