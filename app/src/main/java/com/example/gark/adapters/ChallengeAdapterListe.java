package com.example.gark.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.TournamentActivity;
import com.example.gark.entites.Challenge;

import java.util.ArrayList;

public class ChallengeAdapterListe extends RecyclerView.Adapter<ChallengeAdapterListe.ChallengeAdapterListeHolder> {
    private final Context mContext;
    private final ArrayList<Challenge> challenges;
    Challenge challenge;

    public ChallengeAdapterListe(Context mContext, ArrayList<Challenge> challenges) {
        this.mContext = mContext;
        this.challenges = challenges;
    }
    @NonNull
    @Override
    public ChallengeAdapterListeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.big_challenge_single,parent,false);
        return new ChallengeAdapterListe.ChallengeAdapterListeHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapterListeHolder holder, int position) {
        challenge = challenges.get(position);
        Bitmap bitmap ;
        bitmap = getBitmapFromString(challenge.getImage());
        Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
        holder.challengeImage.setImageBitmap(bitmap);
        holder.challengeName.setText(challenge.getName());
        holder.challengeType.setText(challenge.getType().toString());
        holder.teamNumber.setText(challenge.getTeams().size()+"/"+challenge.getMaxNumberOfTeams());
        holder.challengeStatus.setText(challenge.getState().toString());
        holder.terrainName.setText(challenge.getLocation());

    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public class ChallengeAdapterListeHolder extends RecyclerView.ViewHolder {
        ImageButton challengeImage;
        TextView challengeName;
        TextView challengeType;
        TextView teamNumber;
        TextView challengeStatus;
        TextView terrainName;
        public ChallengeAdapterListeHolder(@NonNull View itemView) {
            super(itemView);
            challengeImage=itemView.findViewById(R.id.challengeImage);
            challengeName=itemView.findViewById(R.id.challengeName);
            challengeType=itemView.findViewById(R.id.challengeType);
            teamNumber=itemView.findViewById(R.id.teamNumber);
            challengeStatus=itemView.findViewById(R.id.challengeStatus);
            terrainName=itemView.findViewById(R.id.terrainName);
            challengeImage.setOnClickListener(new View.OnClickListener() {
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
