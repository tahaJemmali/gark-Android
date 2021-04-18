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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.TournamentActivity;
import com.example.gark.entites.Challenge;

import java.util.ArrayList;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeHolder>  {
    private Context mContext;
    private ArrayList<Challenge> challenges;
    Challenge challenge;
    public ChallengeAdapter(Context mContext, ArrayList<Challenge> challenges) {
        this.mContext = mContext;
        this.challenges = challenges;
    }
    @NonNull
    @Override
    public ChallengeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.challenge_single,parent,false);
        return new ChallengeAdapter.ChallengeHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeHolder holder, int position) {
        challenge = challenges.get(position);
        Bitmap bitmap ;
        bitmap = getBitmapFromString(challenge.getImage());
        Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
        holder.backgroundImageChallenge.setBackground(d);
        holder.challengeName.setText(challenge.getName());
        holder.numberOfTeams.setText(challenge.getMaxNumberOfTeams()+" teams");
        holder.typeTournement.setText(challenge.getType().toString());
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public class ChallengeHolder extends RecyclerView.ViewHolder {
        LinearLayout backgroundImageChallenge;
        TextView challengeName;
        TextView typeTournement;
        TextView numberOfTeams;
        public ChallengeHolder(@NonNull View itemView) {
            super(itemView);
            backgroundImageChallenge=itemView.findViewById(R.id.backgroundImageChallenge);
            challengeName=itemView.findViewById(R.id.challengeName);
            typeTournement=itemView.findViewById(R.id.typeTournement);
            numberOfTeams=itemView.findViewById(R.id.numberOfTeams);
            backgroundImageChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, TournamentActivity.class);
                    challenge=challenges.get(getAdapterPosition());
                    intent.putExtra("tournement",challenge.getName());
                    mContext.startActivity(intent);

                }
            });
        }
    }
}
