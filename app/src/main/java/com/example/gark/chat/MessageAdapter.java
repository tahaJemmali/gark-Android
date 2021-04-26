package com.example.gark.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.Match;
import com.example.gark.entites.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private final Context mContext;
    private final ArrayList<Message> messages;
    Message message;
    User current;
    User friend;
    public MessageAdapter(Context mContext, ArrayList<Message> messages,User friend) {
        this.mContext = mContext;
        this.messages = messages;
        this.friend=friend;
        this.current= MainActivity.getCurrentLoggedInUser();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.single_message,parent,false);
        return new MessageAdapter.MessageHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        message = messages.get(position);
        if(message.getsenderId().equals(current.getId()))
        {
            holder.bulReciver.setVisibility(View.VISIBLE);
            holder.bulSender.setVisibility(View.INVISIBLE);
            holder.currentUserImage.setImageBitmap(getBitmapFromString(current.getPhoto()));
            holder.reciverMessage.setText(message.getMessage());
            holder.dateMessage.setGravity(Gravity.RIGHT);
        }else {
            holder.bulSender.setVisibility(View.VISIBLE);
            holder.bulReciver.setVisibility(View.INVISIBLE);
            holder.friendImage.setImageBitmap(getBitmapFromString(friend.getPhoto()));
            holder.senderMessage.setText(message.getMessage());
        }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy HH:mm");
            holder.dateMessage.setText(formatter.format(message.getDateCreated()));

    }
    public Boolean compareDates(Date createdDate, Date today) {
        if( (createdDate.getTime() - today.getTime()) >= 20*60*1000) {
            return true;
        }
        return false;
    }
    public static Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        View bulSender,bulReciver;
        TextView senderMessage,reciverMessage,dateMessage;
        ImageView friendImage,currentUserImage;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            bulReciver=itemView.findViewById(R.id.bulReciver);
            bulSender=itemView.findViewById(R.id.bulSender);
            dateMessage=itemView.findViewById(R.id.dateMessage);
            senderMessage=itemView.findViewById(R.id.senderMessage);
            reciverMessage=itemView.findViewById(R.id.reciverMessage);
            friendImage=itemView.findViewById(R.id.friendImage);
            currentUserImage=itemView.findViewById(R.id.currentUserImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dateMessage.getVisibility()==View.GONE)
                        dateMessage.setVisibility(View.VISIBLE);
                        else
                    dateMessage.setVisibility(View.GONE);
                }
            });
        }
    }
}
