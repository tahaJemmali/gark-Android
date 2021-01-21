package com.example.gark.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.entites.Skills;

import java.util.ArrayList;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.SkillsHolder>{
    private Context mContext;
    Skills player;
    public SkillsAdapter(Context mContext, Skills player) {
        this.mContext = mContext;
        this.player=player;
    }
    
    @NonNull
    @Override
    public SkillsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.single_skill_single,parent,false);
        return new SkillsAdapter.SkillsHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsHolder holder, int position) {
        switch (position){
            case 0:
                holder.skills.setText("pace");
                holder.numberSkills.setText(""+player.getPace());
                holder.progress_bar.setProgress(player.getPace());
                break;
            case 1:
                holder.skills.setText("shooting");
                holder.numberSkills.setText(""+player.getShooting());
                holder.progress_bar.setProgress(player.getShooting());
                break;
            case 2:
                holder.skills.setText("passing");
                holder.numberSkills.setText(""+player.getPassing());
                holder.progress_bar.setProgress(player.getPassing());
                break;
            case 3:
                holder.skills.setText("dribbling");
                holder.numberSkills.setText(""+player.getDribbling());
                holder.progress_bar.setProgress(player.getDribbling());
                break;
            case 4:
                holder.skills.setText("defending");
                holder.numberSkills.setText(""+player.getDefending());
                holder.progress_bar.setProgress(player.getDribbling());
                break;
            case 5:
                holder.skills.setText("physical");
                holder.numberSkills.setText(""+player.getPhysical());
                holder.progress_bar.setProgress(player.getPhysical());
                break;
            case 6:
                holder.skills.setText("score");
                holder.numberSkills.setText(""+player.getScore());
                holder.progress_bar.setProgress(player.getScore());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class SkillsHolder extends RecyclerView.ViewHolder {
        TextView skills,numberSkills;
        ProgressBar progress_bar;
        public SkillsHolder(@NonNull View itemView) {
            super(itemView);
            skills=itemView.findViewById(R.id.skills);
            numberSkills=itemView.findViewById(R.id.numberSkills);
            progress_bar=itemView.findViewById(R.id.progress_bar);
            progress_bar.setIndeterminate(false);
        }
    }
}
