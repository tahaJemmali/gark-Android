package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gark.R;
import com.example.gark.entites.Match;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchHolder> {
    private Context mContext;
    private ArrayList<Match> matches;
    Match match;
    public MatchAdapter(Context mContext, ArrayList<Match> matches) {
        this.mContext = mContext;
        this.matches = matches;
    }
    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.match_single,parent,false);
        return new MatchAdapter.MatchHolder(rootView);
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHolder holder, int position) {
        match = matches.get(position);
        Bitmap bitmap = getBitmapFromString(match.getTeam1().getImage());
        holder.team1Image.setImageBitmap(bitmap);
        bitmap = getBitmapFromString(match.getTeam2().getImage());
        holder.team2Image.setImageBitmap(bitmap);
        holder.team1goals.setText("");
        holder.team2goals.setText("");
        Date date= new Date();
        if(date.before(match.getStart_date())){
            holder.end_date.setText(match.getState().toString());
        }else if (match.getEnd_date()!=null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            holder.end_date.setText(formatter.format(match.getEnd_date()));
        }else {
            holder.end_date.setText("none");
        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder {
        ImageView team1Image;
        ImageView team2Image;
        TextView team1goals;
        TextView team2goals;
        TextView end_date;
        public MatchHolder(@NonNull View itemView) {
            super(itemView);
            team1Image=itemView.findViewById(R.id.team1Image);
            team2Image=itemView.findViewById(R.id.team2Image);
            team1goals=itemView.findViewById(R.id.team1goals);
            team2goals=itemView.findViewById(R.id.team2goals);
            end_date=itemView.findViewById(R.id.end_date);


        }
    }
}
