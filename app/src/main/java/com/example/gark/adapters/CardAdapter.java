package com.example.gark.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.PlayerProfileActvity;
import com.example.gark.R;
import com.example.gark.entites.Skills;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.CardHolder>{
    private Context mContext;
    private ArrayList<Skills> players;
    Skills player;
    String captinId;
    public CardAdapter(Context mContext, ArrayList<Skills> players) {
        this.mContext = mContext;
        this.players = players;
    }
    public CardAdapter(Context mContext, ArrayList<Skills> players,String captinId) {
        this.mContext = mContext;
        this.players = players;
        this.captinId=captinId;
    }

    @NonNull
    @Override
    public CardAdapter.CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.card_single,parent,false);
        return new CardAdapter.CardHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CardHolder holder, int position) {
        player = players.get(position);
        Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        holder.playerImage.setImageBitmap(bitmap);

        bitmap = getBitmapFromString(player.getPlayer().getPhoto());
        holder.countryImage.setImageResource(mContext.getResources().getIdentifier(player.getNationality().toString(), "drawable", mContext.getPackageName()));
        if(player.getScore()<=17){
            holder.relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_bronze", "drawable", mContext.getPackageName()));
        }else if(player.getScore()<=34){
            holder.score.setTextColor(Color.WHITE);
            holder.pace.setTextColor(Color.WHITE);
            holder.playerName.setTextColor(Color.WHITE);
            holder.shooting.setTextColor(Color.WHITE);
            holder.passing.setTextColor(Color.WHITE);
            holder. dribbling.setTextColor(Color.WHITE);
            holder.defending.setTextColor(Color.WHITE);
            holder. physical.setTextColor(Color.WHITE);
            holder. role.setTextColor(Color.WHITE);
            holder.relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_dark_bronze", "drawable", mContext.getPackageName()));

        }else if(player.getScore()<=51){
            holder.relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_silver", "drawable", mContext.getPackageName()));

        }else if(player.getScore()<=67){
            holder.score.setTextColor(Color.WHITE);
            holder. pace.setTextColor(Color.WHITE);
            holder. playerName.setTextColor(Color.WHITE);
            holder.shooting.setTextColor(Color.WHITE);
            holder. passing.setTextColor(Color.WHITE);
            holder. dribbling.setTextColor(Color.WHITE);
            holder. defending.setTextColor(Color.WHITE);
            holder. physical.setTextColor(Color.WHITE);
            holder. role.setTextColor(Color.WHITE);
            holder. relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_dark_silver", "drawable", mContext.getPackageName()));

        }else if(player.getScore()<=84){
            holder. relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_gold", "drawable", mContext.getPackageName()));

        }else if(player.getScore()<100){
            holder. score.setTextColor(Color.WHITE);
            holder.  pace.setTextColor(Color.WHITE);
            holder.  playerName.setTextColor(Color.WHITE);
            holder.  shooting.setTextColor(Color.WHITE);
            holder.  passing.setTextColor(Color.WHITE);
            holder.  dribbling.setTextColor(Color.WHITE);
            holder.  defending.setTextColor(Color.WHITE);
            holder.  physical.setTextColor(Color.WHITE);
            holder. role.setTextColor(Color.WHITE);
            holder. relativeLayout6.setImageResource(mContext.getResources().getIdentifier("card_dark_gold", "drawable", mContext.getPackageName()));

        }else {
            holder. score.setTextColor(Color.WHITE);
            holder. pace.setTextColor(Color.WHITE);
            holder. playerName.setTextColor(Color.WHITE);
            holder. shooting.setTextColor(Color.WHITE);
            holder. passing.setTextColor(Color.WHITE);
            holder. dribbling.setTextColor(Color.WHITE);
            holder.   defending.setTextColor(Color.WHITE);
            holder.  physical.setTextColor(Color.WHITE);
            holder.  role.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,20,0,0);
            params.gravity = Gravity.CENTER;
            holder.  playerName.setLayoutParams(params);
            holder.  relativeLayout6.setBackgroundResource(mContext.getResources().getIdentifier("card_plat", "drawable", mContext.getPackageName()));

        }
        holder. score.setText(""+player.getScore());
        holder. role.setText(player.getRole().toString());
        holder. playerName.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
        holder. pace.setText("PAC "+player.getPace());
        holder.shooting.setText("SHO "+player.getShooting());
        holder.passing.setText("PAS "+player.getPassing());
        holder.dribbling.setText(player.getDribbling()+" DRI");
        holder. defending.setText(player.getDefending()+" DEF");
        holder. physical.setText(player.getPhysical()+" PHY");


    }


    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {
        TextView score,role,playerName,pace,shooting,passing,dribbling,defending,physical;
        ImageView countryImage,playerImage,relativeLayout6;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout6=itemView.findViewById(R.id.relativeLayout6);
            score=itemView.findViewById(R.id.score);
            role=itemView.findViewById(R.id.role);
            playerName=itemView.findViewById(R.id.playerName);
            pace=itemView.findViewById(R.id.pace);
            shooting=itemView.findViewById(R.id.shooting);
            passing=itemView.findViewById(R.id.passing);
            dribbling=itemView.findViewById(R.id.dribbling);
            defending=itemView.findViewById(R.id.defending);
            physical=itemView.findViewById(R.id.physical);
            countryImage=itemView.findViewById(R.id.countryImage);
            playerImage=itemView.findViewById(R.id.playerImage);
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
