package com.example.gark.adapters;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.MatchActivity;
import com.example.gark.PlayerProfileActvity;
import com.example.gark.R;
import com.example.gark.dialog.SelectTeamDialog;
import com.example.gark.dialog.VoteDialog;
import com.example.gark.entites.MatchVote;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MatchPlayerAdapter  extends RecyclerView.Adapter<MatchPlayerAdapter.TopPlayerHolder>{
    private final Context mContext;
    private final ArrayList<Skills> players;
    Skills player;
    String captinId;
    int allowVote;
    Team team;
    public MatchPlayerAdapter(Context mContext, ArrayList<Skills> players) {
        this.mContext = mContext;
        this.players = players;
    }
    public MatchPlayerAdapter(Context mContext, ArrayList<Skills> players, String captinId, int allowVote, Team team) {
        this.mContext = mContext;
        this.players = players;
        this.captinId=captinId;
        this.allowVote=allowVote;
        this.team=team;
    }

    @NonNull
    @Override
    public TopPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.match_player_single,parent,false);
        return new MatchPlayerAdapter.TopPlayerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlayerHolder holder, int position) {
        player = players.get(position);
        if (player.getPlayer().getPhoto().equals("Not mentioned")){
            holder.topPlayerImage.setImageResource(R.drawable.ic_default_user);
        }else {
            Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
            holder.topPlayerImage.setImageBitmap(bitmap);
        }

        if (player.getId().equals(captinId)){
            Log.e("TAG", "onBindViewHolder: "+"capitain" );
            holder.captinBadge.setVisibility(View.VISIBLE);
        }
        holder.fullname.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class TopPlayerHolder extends RecyclerView.ViewHolder {
        CircleImageView topPlayerImage;
        ImageView captinBadge;
        TextView fullname;
        public TopPlayerHolder(@NonNull View itemView) {
            super(itemView);
            topPlayerImage = itemView.findViewById(R.id.topPlayerImage);
            captinBadge = itemView.findViewById(R.id.captinBadge);
            fullname = itemView.findViewById(R.id.fullname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player=players.get(getAdapterPosition());

                    if(MainActivity.currentPlayerSkills.getTeams().contains(team)){
                        allowVote=2;
                    }else if(player.equals(MainActivity.currentPlayerSkills)){
                        allowVote=3;
                    }else {
                        allowVote=1;
                    }
                    if(MatchActivity.matchVotes.size()>0){
                        for(MatchVote row:MatchActivity.matchVotes){
                            if( row.getVotedOn().equals(player))
                                allowVote=4;
                        }
                    }


             switch (allowVote){
                 case 0:
                     VoteDialog dialog = new VoteDialog(player);
                     dialog.show( ((MatchActivity) mContext).getSupportFragmentManager(), "VoteDialog");
                     break;
                 case 1:
                     Toast.makeText(mContext, mContext.getString(R.string.not_played_in_game),Toast.LENGTH_LONG).show();
                     break;
                 case 2:
                     Toast.makeText(mContext, mContext.getString(R.string.no_teamMate),Toast.LENGTH_LONG).show();
                     break;
                 case 3:
                     Toast.makeText(mContext, mContext.getString(R.string.no_self),Toast.LENGTH_LONG).show();
                     break;
                 case 4:
                     Toast.makeText(mContext, mContext.getString(R.string.already_voted),Toast.LENGTH_LONG).show();
                     break;
             }
                }
            });
        }

    }
}
