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

import com.example.gark.PlayerProfileActvity;
import com.example.gark.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.entites.Skills;
import com.example.gark.repositories.SkillsRepository;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopPlayersAdapter  extends RecyclerView.Adapter<TopPlayersAdapter.TopPlayerHolder>{
    private Context mContext;
    private ArrayList<Skills> players;
    Skills player;
    String captinId;
    public TopPlayersAdapter(Context mContext, ArrayList<Skills> players) {
        this.mContext = mContext;
        this.players = players;
    }
    public TopPlayersAdapter(Context mContext, ArrayList<Skills> players,String captinId) {
        this.mContext = mContext;
        this.players = players;
        this.captinId=captinId;
    }

    @NonNull
    @Override
    public TopPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.top_player_single,parent,false);
        return new TopPlayersAdapter.TopPlayerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlayerHolder holder, int position) {
        player = players.get(position);
        Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        holder.topPlayerImage.setImageBitmap(bitmap);
        if (player.getId().equals(captinId)){
            holder.captinBadge.setVisibility(View.VISIBLE);
        }
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
        public TopPlayerHolder(@NonNull View itemView) {
            super(itemView);
            topPlayerImage = itemView.findViewById(R.id.topPlayerImage);
            captinBadge = itemView.findViewById(R.id.captinBadge);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, PlayerProfileActvity.class);
                    intent.putExtra("playerId",players.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
