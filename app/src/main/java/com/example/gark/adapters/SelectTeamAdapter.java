package com.example.gark.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.TeamProfileActivity;
import com.example.gark.entites.Team;

import java.util.ArrayList;

public class SelectTeamAdapter extends RecyclerView.Adapter<SelectTeamAdapter.TeamHolder> {
    private Context mContext;
    private ArrayList<Team> teams;
    public static Team team;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;

    public SelectTeamAdapter(Context mContext, ArrayList<Team> teams) {
        this.mContext = mContext;
        this.teams = teams;
    }

    @NonNull
    @Override
    public SelectTeamAdapter.TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.top_team_select,parent,false);
        return new SelectTeamAdapter.TeamHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTeamAdapter.TeamHolder holder, int position) {
        team = teams.get(position);
        Bitmap bitmap = getBitmapFromString(team.getImage());
        holder.topTeamImage.setImageBitmap(bitmap);
        holder.teamNameTextView.setText(team.getName().substring(0,1).toUpperCase() + team.getName().substring(1).toLowerCase());
        holder.teamWins.setText(team.getVictories()+" wins");
        holder.radio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (lastChecked==null){
                    lastChecked=holder.radio;
                    team=teams.get(position);
                }else {
                    lastChecked.setChecked(false);
                    lastChecked=holder.radio;
                    team=teams.get(position);
                }
            }
        });
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class TeamHolder extends RecyclerView.ViewHolder {
        ImageView topTeamImage;
        TextView teamNameTextView,teamWins;
        RadioButton radio;
        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            topTeamImage = itemView.findViewById(R.id.topTeamImage);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            teamWins = itemView.findViewById(R.id.teamWins);
            radio= itemView.findViewById(R.id.radio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG", "onClick: " );
                }
            });
        }
    }
}
