package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.entites.Challenge;

import java.util.ArrayList;

public class ChallengeAdapterListe extends RecyclerView.Adapter<ChallengeAdapterListe.ChallengeAdapterListeHolder> {
    private Context mContext;
    private ArrayList<Challenge> challenges;
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
        public ChallengeAdapterListeHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
