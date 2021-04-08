package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.AddChallenge2Activity;
import com.example.gark.MatchActivity;
import com.example.gark.R;
import com.example.gark.dialog.AddMatchDialog;
import com.example.gark.dialog.SelectTeamDialog;
import com.example.gark.dialog.VoteDialog;
import com.example.gark.entites.Match;

import java.util.ArrayList;

public class AddMatchAdapter extends RecyclerView.Adapter<AddMatchAdapter.MatchHolder>  {
    private Context mContext;
    private ArrayList<Match> matches;
    Match match;
    public static Match selectedMatch;
    public AddMatchAdapter(Context mContext, ArrayList<Match> matches) {
        this.mContext = mContext;
        this.matches = matches;
    }

    @NonNull
    @Override
    public MatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.add_match_single,parent,false);
        return new AddMatchAdapter.MatchHolder(rootView);
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHolder holder, int position) {
        match = matches.get(position);
        holder.typeMatch.setText(match.getType().toString());
        if(match.getStart_date()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            holder.match_confirmed.setChecked(true);
            holder.match_start_date.setText(format.format(match.getStart_date()));
        }

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class MatchHolder extends RecyclerView.ViewHolder {
        TextView typeMatch;
        TextView match_start_date;
        CheckBox match_confirmed;
        public MatchHolder(@NonNull View itemView) {
            super(itemView);
            typeMatch=itemView.findViewById(R.id.typeMatch);
            match_start_date=itemView.findViewById(R.id.match_start_date);
            match_confirmed=itemView.findViewById(R.id.match_confirmed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedMatch=matches.get(getAdapterPosition());
                    AddMatchDialog dialog = new AddMatchDialog();
                    dialog.show( ((AddChallenge2Activity) mContext).getSupportFragmentManager(), "AddMatchDialog");
                }
            });
        }
    }
}
