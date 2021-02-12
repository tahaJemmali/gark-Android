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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.PlayerProfileActvity;
import com.example.gark.R;
import com.example.gark.entites.Skills;

import java.util.ArrayList;

public class CommunityTopPlayerAdapter extends RecyclerView.Adapter<CommunityTopPlayerAdapter.CommunityTopPlayerHolder> {
    private Context mContext;
    private ArrayList<Skills> players;
    Skills player;
    public CommunityTopPlayerAdapter(Context mContext, ArrayList<Skills> players) {
        this.mContext = mContext;
        this.players = players;
    }

    @NonNull
    @Override
    public CommunityTopPlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.community_top_player_single,parent,false);
        return new CommunityTopPlayerAdapter.CommunityTopPlayerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityTopPlayerHolder holder, int position) {
        player = players.get(position);
        if (player.getPlayer().getPhoto().equals("Not mentioned")){
            holder.playerImage.setImageResource(R.drawable.ic_default_user);
        }else {
            Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
            Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);
            holder.playerImage.setBackground(d);
        }
        holder.countryImage.setImageResource(mContext.getResources().getIdentifier(player.getNationality().toString(),"drawable",mContext.getPackageName()));
        holder.role.setText(player.getRole().toString());
        holder.playerName.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
        switch (player.getRating()){
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
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class CommunityTopPlayerHolder extends RecyclerView.ViewHolder {
        ImageButton playerImage;
        ImageView countryImage,start_one,start_two,start_three,start_four,start_five;
        TextView playerName,role;
        public CommunityTopPlayerHolder(@NonNull View itemView) {
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
            playerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,PlayerProfileActvity.class);
                    intent.putExtra("playerId",players.get(getAdapterPosition()).getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
