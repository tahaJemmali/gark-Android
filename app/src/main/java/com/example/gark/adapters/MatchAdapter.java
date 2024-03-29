package com.example.gark.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gark.MatchActivity;
import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.TournamentActivity;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MatchRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchHolder> {
    private final Context mContext;
    private final ArrayList<Match> matches;
    Match match;
    public static Match selectedMatch;
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
        if(match.getTeam1()!=null &&match.getTeam1().getImage()!=null){
            Bitmap bitmap = getBitmapFromString(match.getTeam1().getImage());
            holder.team1Image.setImageBitmap(bitmap);
        }
        if(match.getTeam2()!=null &&match.getTeam2().getImage()!=null){
            Bitmap bitmap = getBitmapFromString(match.getTeam2().getImage());
            holder.team2Image.setImageBitmap(bitmap);
        }
        int team1goalsNb=0;
        int team2goalsNb=0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date= new Date();
        if(date.before(match.getStart_date())){
            holder.end_date.setText(match.getState().toString()+ mContext.getString(R.string.scheduled)+formatter.format(match.getStart_date()));
        }else {
            if(!Objects.isNull(match.getEnd_date())){
                holder.end_date.setText(formatter.format(match.getEnd_date()));

                for (MatchAction row:match.getGoals()){
                    if(row.getTeam().equals(match.getTeam1())){
                        team1goalsNb++;
                    }else if (row.getTeam().equals(match.getTeam2())){
                        team2goalsNb++;
                    }
                }
            }else {
                holder.end_date.setText(mContext.getString(R.string.game_must_finsh));
            }
            if(match.getGoals().size()>0){
                holder.team1goals.setText(""+team1goalsNb);
                holder.team2goals.setText(""+team2goalsNb);
            }else {
                holder.team1goals.setText("");
                holder.team2goals.setText("");
            }

        }
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder implements IRepository {
        ImageView team1Image;
        ImageView team2Image;
        TextView team1goals;
        TextView team2goals;
        TextView end_date;
        ProgressDialog dialogg;
        public MatchHolder(@NonNull View itemView) {
            super(itemView);
            team1Image=itemView.findViewById(R.id.team1Image);
            team2Image=itemView.findViewById(R.id.team2Image);
            team1goals=itemView.findViewById(R.id.team1goals);
            team2goals=itemView.findViewById(R.id.team2goals);
            end_date=itemView.findViewById(R.id.end_date);
            dialogg = ProgressDialog.show(itemView.getContext(), "", mContext.getString(R.string.loading), true);
            dialogg.dismiss();
            MatchRepository.getInstance().setiRepository(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MatchRepository.getInstance().findById(itemView.getContext(),matches.get(getAdapterPosition()).getId());
                }
            });

        }

        @Override
        public void showLoadingButton() {
            dialogg.show();
        }

        @Override
        public void doAction() {
            Intent intent=new Intent(mContext, MatchActivity.class);
            selectedMatch=MatchRepository.getInstance().getElement();
            mContext.startActivity(intent);
        }

        @Override
        public void dismissLoadingButton() {
            dialogg.dismiss();
        }
    }
}
