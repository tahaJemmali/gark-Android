package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MatchActivity;
import com.example.gark.R;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;

import java.util.ArrayList;

public class MatchActionAdapter extends RecyclerView.Adapter<MatchActionAdapter.MatchActionHolder>{
    private Context mContext;
    private ArrayList<MatchAction> matchActions;
    MatchAction matchAction;
    boolean editing;
    Match match;
    public MatchActionAdapter(Context mContext, ArrayList<MatchAction> matchActions,Match match,Boolean editing) {
        this.mContext = mContext;
        this.matchActions = matchActions;
        this.editing = editing;
        this.match=match;
    }
    public MatchActionAdapter(Context mContext, ArrayList<MatchAction> matchActions,Match match) {
        this.mContext = mContext;
        this.matchActions = matchActions;
        this.match=match;
    }
    @NonNull
    @Override
    public MatchActionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.match_action_single,parent,false);
        return new MatchActionAdapter.MatchActionHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchActionHolder holder, int position) {
        Log.e("TAG", "onBindViewHolder: "+position );
    matchAction=matchActions.get(position);
        SimpleDateFormat format = new SimpleDateFormat("mm");
    holder.minutes.setText(format.format(matchAction.getDate())+"’");
    switch (matchAction.getType()){
        case goal:
            holder.icon_matchAction.setImageResource(R.drawable.goal);
            break;
        case redCard:
            holder.icon_matchAction.setImageResource(R.drawable.red_card);
            break;
        case yellowCard:
            holder.icon_matchAction.setImageResource(R.drawable.yellow_card);
            break;
    }
   String playerFullName= matchAction.getPlayer().getPlayer().getFirstName()+" "+matchAction.getPlayer().getPlayer().getLastName();
    holder.playerName.setText(playerFullName);
    if(editing){
        holder.edit.setVisibility(View.VISIBLE);
    }
    }

    @Override
    public int getItemCount() {
        return matchActions.size();
    }

    public class MatchActionHolder extends RecyclerView.ViewHolder {
        TextView minutes;
        ImageView icon_matchAction;
        TextView playerName;
        ImageButton edit;
        public MatchActionHolder(@NonNull View itemView) {
            super(itemView);
            minutes=itemView.findViewById(R.id.minutes);
            icon_matchAction=itemView.findViewById(R.id.icon_matchAction);
            playerName=itemView.findViewById(R.id.playerName);
            edit=itemView.findViewById(R.id.edit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG", "onClick: "+matchActions.size() );
                    Log.e("TAG", "onClick: "+getAdapterPosition() );
                }
            });
        }
    }
}
