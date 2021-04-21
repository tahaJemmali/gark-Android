package com.example.gark.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gark.AddPostActivity;
import com.example.gark.MainActivity;
import com.example.gark.MatchActivity;
import com.example.gark.R;

import com.example.gark.TournamentActivity;
import com.example.gark.entites.MatchVote;
import com.example.gark.entites.Skills;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchVoteRepository;
import com.example.gark.repositories.SkillsRepository;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class VoteDialog extends DialogFragment implements IRepository {
    //UI
    View view;
    CircleImageView palyerImage;
    TextView fullname;
    SeekBar paceSB;
    SeekBar shootingSB;
    SeekBar passingSB;
    SeekBar dribblingSB;
    SeekBar defendingSB;
    SeekBar physicalSB;
    SeekBar ratingSB;
    TextView paceV;
    TextView shootingV;
    TextView passingV;
    TextView dribblingV;
    TextView defendingV;
    TextView physicalV;
    TextView ratingV;
    Button vote;
    //Var
    int pace;
    int shooting;
    int passing;
    int dribbling;
    int defending;
    int physical;
    int rating;
    private static final String TAG = "VoteDialog";
    Skills player;


    public VoteDialog(Skills player){
        this.player=player;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vote_dialog, container, false);
        initUI();
        if (getDialog() != null && getDialog().getWindow() != null) {
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }
        return view;
    }
    void initUI() {
        palyerImage=view.findViewById(R.id.palyerImage);
        fullname=view.findViewById(R.id.fullname);
         paceSB=view.findViewById(R.id.paceSB);
         shootingSB=view.findViewById(R.id.shootingSB);
         passingSB=view.findViewById(R.id.passingSB);
         dribblingSB=view.findViewById(R.id.dribblingSB);
         defendingSB=view.findViewById(R.id.defendingSB);
         physicalSB=view.findViewById(R.id.physicalSB);
         ratingSB=view.findViewById(R.id.ratingSB);
         paceV=view.findViewById(R.id.paceV);
         shootingV=view.findViewById(R.id.shootingV);
         passingV=view.findViewById(R.id.passingV);
         dribblingV=view.findViewById(R.id.dribblingV);
         defendingV=view.findViewById(R.id.defendingV);
         physicalV=view.findViewById(R.id.physicalV);
         ratingV=view.findViewById(R.id.ratingV);
         vote=view.findViewById(R.id.vote);
        setSlider();
        Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        palyerImage.setImageBitmap(bitmap);
        fullname.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVote();
            }
        });

    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    void addVote(){
        MatchVote matchVote=new MatchVote(MainActivity.currentPlayerSkills,
                player,  pace,  shooting,  passing,  dribbling,  defending,  physical,  rating,new Date() );
        MatchVoteRepository.getInstance().setiRepository(this);
        MatchVoteRepository.getInstance().add(getContext(),matchVote,null);
        MatchActivity.matchVotes.add(matchVote);
        int score=(pace+shooting+passing+dribbling+defending+physical)/6;
        player.setScore(score+player.getScore()/2);
        rating=(rating+player.getRating())/2;
        player.setRating(rating);
        physical=(physical+player.getPhysical())/2;
        player.setPhysical(physical);
        defending=(defending+player.getDefending())/2;
        player.setDefending(defending);
        dribbling=(dribbling+player.getDribbling())/2;
        player.setDribbling(dribbling);
        shooting=(shooting+player.getShooting())/2;
        player.setShooting(shooting);
        pace=(pace+player.getPace())/2;
        player.setPace(pace);
        passing=(passing+player.getPassing())/2;
        player.setPassing(passing);
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().update(getContext(),player,player.getId());
        Toast.makeText(getContext(), getContext().getString(R.string.vote_added),Toast.LENGTH_LONG).show();
        getDialog().dismiss();
    }
void setSlider(){
    paceSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            paceV.setText(""+i);
            pace=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    shootingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            shootingV.setText(""+i);
            shooting=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    passingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            passingV.setText(""+i);
            passing=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    dribblingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            dribblingV.setText(""+i);
            dribbling=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    defendingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            defendingV.setText(""+i);
            defending=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    physicalSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            physicalV.setText(""+i);
            physical=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
    ratingSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            ratingV.setText(""+i);
            rating=i;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
}

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {

    }

    @Override
    public void dismissLoadingButton() {

    }
}



