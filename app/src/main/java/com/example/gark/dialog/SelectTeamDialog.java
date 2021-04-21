package com.example.gark.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.AddPostActivity;
import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.TournamentActivity;
import com.example.gark.adapters.SelectTeamAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;

import java.util.ArrayList;

public class SelectTeamDialog  extends DialogFragment  {
    private static final String TAG = "SelectTeamDialog";

    public interface OnInputSelected{
        void sendInput(Team input);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    View view;
    RecyclerView selectTeamRecyclerView;
    Button confirm;
    //Var
    SelectTeamAdapter teamsAdapter;

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
         view = inflater.inflate(R.layout.select_team_dialog, container, false);
        initUI();
        if (getDialog() != null && getDialog().getWindow() != null) {
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }
        return view;
    }


    void initUI(){
        mOnInputSelected = (OnInputSelected) getActivity();
        selectTeamRecyclerView=view.findViewById(R.id.selectTeamRecyclerView);
        confirm=view.findViewById(R.id.confirm);
        initUIRecycleViewerTeams();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SelectTeamAdapter.team!=null){
                    if(!TournamentActivity.challenge.getTeams().contains(SelectTeamAdapter.team)){
                        mOnInputSelected.sendInput(SelectTeamAdapter.team);
                    }else {
                        Toast.makeText(getContext(), getString(R.string.team_already_joined),Toast.LENGTH_LONG).show();
                    }
                    getDialog().dismiss();
                }
            }
        });

    }
    private void initUIRecycleViewerTeams() {
        selectTeamRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        teamsAdapter = new SelectTeamAdapter(getContext(), (ArrayList<Team>) TournamentActivity.skill.getTeams());
        selectTeamRecyclerView.setAdapter(teamsAdapter);
    }
}
