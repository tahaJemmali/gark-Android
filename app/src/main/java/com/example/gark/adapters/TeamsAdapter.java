package com.example.gark.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.PlayerProfileActvity;
import com.example.gark.TeamProfileActivity;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;

import java.util.ArrayList;
import com.example.gark.R;
public class TeamsAdapter   extends RecyclerView.Adapter<TeamsAdapter.TeamHolder>{
    private Context mContext;
    private ArrayList<Team> teams;
    Team team;
    public TeamsAdapter(Context mContext, ArrayList<Team> teams) {
        this.mContext = mContext;
        this.teams = teams;
    }

    @NonNull
    @Override
    public TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.top_team_single,parent,false);
        return new TeamsAdapter.TeamHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHolder holder, int position) {
        team = teams.get(position);
        Bitmap bitmap = getBitmapFromString(team.getImage());
        Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
        holder.topTeamImage.setBackground(d);
        holder.teamNameTextView.setText(team.getName().substring(0,1).toUpperCase() + team.getName().substring(1).toLowerCase());
        holder.teamWins.setText(team.getVictories()+" wins");
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
        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            topTeamImage = itemView.findViewById(R.id.topTeamImage);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            teamWins = itemView.findViewById(R.id.teamWins);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, TeamProfileActivity.class);
                    intent.putExtra("teamId",teams.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
