package com.example.gark.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.entites.Skills;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteTeamAdapter extends RecyclerView.Adapter<InviteTeamAdapter.InviteHolder> {
    private Context mContext;
    private ArrayList<Skills> players;
    Skills player;
    ArrayList<Skills> returnPlayers;
    public InviteTeamAdapter(Context mContext, ArrayList<Skills> players) {
       returnPlayers = new ArrayList<Skills>();
        this.mContext = mContext;
        this.players = players;
    }
    @NonNull
    @Override
    public InviteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.invitation_single,parent,false);
        return new InviteTeamAdapter.InviteHolder(rootView);
    }
    public ArrayList<Skills> returnSelectedMemembers(){
        return returnPlayers;
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHolder holder, int position) {
        player = players.get(position);
        if (player.getPlayer().getPhoto().equals("Not mentioned")){
            holder.inviteUserImage.setImageResource(R.drawable.ic_default_user);
        }else {
            Bitmap bitmap = getBitmapFromString(player.getPlayer().getPhoto());
            holder.inviteUserImage.setImageBitmap(bitmap);
        }
        holder.inviteUserName.setText(player.getPlayer().getFirstName()+" "+player.getPlayer().getLastName());
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    @Override
    public int getItemCount() {
        return players.size();
    }

    public class InviteHolder extends RecyclerView.ViewHolder {
        CircleImageView inviteUserImage;
        TextView inviteUserName;
        CheckBox invite;
        public InviteHolder(@NonNull View itemView) {
            super(itemView);
            inviteUserImage = itemView.findViewById(R.id.inviteUserImage);
            inviteUserName = itemView.findViewById(R.id.inviteUserName);
            invite = itemView.findViewById(R.id.invite);
            invite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        returnPlayers.add(players.get(getAdapterPosition()));
                    }else {
                        if (returnPlayers.contains(players.get(getAdapterPosition()))){
                            returnPlayers.remove(players.get(getAdapterPosition()));
                        }
                    }
                }
            });
        }
    }
}
