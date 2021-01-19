package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;

import java.util.ArrayList;

public class CommunityTopTeamsAdapter extends RecyclerView.Adapter<CommunityTopTeamsAdapter.CommunityTopTeamsHolder> {
    private Context mContext;
    private ArrayList<Team> teams;
    Team team;

    public CommunityTopTeamsAdapter(Context mContext, ArrayList<Team> teams) {
        this.mContext = mContext;
        this.teams = teams;
    }

    @NonNull
    @Override
    public CommunityTopTeamsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.community_top_player_single, parent, false);
        return new CommunityTopTeamsAdapter.CommunityTopTeamsHolder(rootView);
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityTopTeamsHolder holder, int position) {
        team = teams.get(position);
        Bitmap bitmap = getBitmapFromString(team.getImage());
        Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
        holder.playerImage.setBackground(d);
        holder.countryImage.setImageResource(mContext.getResources().getIdentifier(team.getCapitaine().getNationality().toString(), "drawable", mContext.getPackageName()));
        holder.role.setText(team.getCapitaine().getRole().toString());
        holder.playerName.setText(team.getName());
        switch (team.getRating()) {
            case 1:
                holder.start_one.setImageResource(R.drawable.ic_rating_start_checked);
                break;
            case 2:
                holder.start_one.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_two.setImageResource(R.drawable.ic_rating_start_checked);
                break;
            case 3:
                holder.start_one.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_two.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_three.setImageResource(R.drawable.ic_rating_start_checked);
                break;
            case 4:
                holder.start_one.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_two.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_three.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_four.setImageResource(R.drawable.ic_rating_start_checked);
                break;
            case 5:
                holder.start_one.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_two.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_three.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_four.setImageResource(R.drawable.ic_rating_start_checked);
                holder.start_five.setImageResource(R.drawable.ic_rating_start_checked);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class CommunityTopTeamsHolder extends RecyclerView.ViewHolder {
        ImageView playerImage, countryImage, start_one, start_two, start_three, start_four, start_five;
        TextView playerName, role;

        public CommunityTopTeamsHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.commTopPlayerImage);
            countryImage = itemView.findViewById(R.id.countryImage);
            start_one = itemView.findViewById(R.id.start_one);
            start_two = itemView.findViewById(R.id.start_two);
            start_three = itemView.findViewById(R.id.start_three);
            start_four = itemView.findViewById(R.id.start_four);
            start_five = itemView.findViewById(R.id.start_five);
            playerName = itemView.findViewById(R.id.playerName);
            role = itemView.findViewById(R.id.role);
        }
    }
}
