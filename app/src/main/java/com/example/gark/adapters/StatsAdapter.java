package com.example.gark.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.entites.Team;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsHolder>{
    private final Context mContext;
    Team team;
    public StatsAdapter(Context mContext, Team team) {
        this.mContext = mContext;
        this.team=team;
    }

    @NonNull
    @Override
    public StatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.single_skill_single,parent,false);
        return new StatsAdapter.StatsHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsHolder holder, int position) {
       switch (position){
           case 0:
               holder.stats.setText(mContext.getString(R.string.wins));
               holder.numberStats.setText(""+team.getVictories());
               holder.progress_bar.setProgress(team.getVictories());
               break;
           case 1:
               holder.stats.setText(mContext.getString(R.string.loses));
               holder.numberStats.setText(""+team.getDefeats());
               holder.progress_bar.setProgress(team.getDefeats());
               break;
           case 2:
               holder.stats.setText(mContext.getString(R.string.draws));
               holder.numberStats.setText(""+team.getDraws());
               holder.progress_bar.setProgress(team.getDraws());
               break;
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class StatsHolder extends RecyclerView.ViewHolder {
        TextView stats,numberStats;
        ProgressBar progress_bar;
        public StatsHolder(@NonNull View itemView) {
            super(itemView);
            stats=itemView.findViewById(R.id.skills);
            numberStats=itemView.findViewById(R.id.numberSkills);
            progress_bar=itemView.findViewById(R.id.progress_bar);
            progress_bar.setIndeterminate(false);
            progress_bar.setMax(team.getDefeats()+team.getVictories()+team.getDraws());
        }
    }
}
