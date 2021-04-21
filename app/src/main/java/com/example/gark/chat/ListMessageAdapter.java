package com.example.gark.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.TeamProfileActivity;
import com.example.gark.entites.User;
import com.example.gark.repositories.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageAdapter  extends RecyclerView.Adapter<ListMessageAdapter.ListMessageHolder> {
    private final Context mContext;
    private final ArrayList<Chat> chats;

    public ListMessageAdapter(Context mContext, ArrayList<Chat> chats) {
        this.mContext = mContext;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ListMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.single_element_list_message,parent,false);
        return new ListMessageAdapter.ListMessageHolder(rootView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListMessageHolder holder, int position) {
        Chat chat = chats.get(position);
        if (!chat.getMessages().isEmpty()){
            Message message = chat.getMessages().get(0);
            User displayedUser=new User();
            if (message.getReciver()!=null && message.getSender()!=null){
                if (message.getReciver().getId().equals(MainActivity.getCurrentLoggedInUser().getId())){
                    displayedUser=message.getSender();
                    holder.symbole.setVisibility(View.INVISIBLE);
                }else {
                    displayedUser=message.getReciver();
                    holder.symbole.setVisibility(View.VISIBLE);
                    holder.senderName .setTypeface(null,Typeface.NORMAL);
                    holder.message.setTypeface(null,Typeface.NORMAL);
                }

                if (displayedUser.getPhoto().equals("Not mentioned")){
                    holder.senderImage.setImageResource(R.drawable.ic_default_user);
                }else {
                    Bitmap bitmap = getBitmapFromString(displayedUser.getPhoto());
                    holder.senderImage.setImageBitmap(bitmap);
                }
                holder.senderName.setText(displayedUser.getFirstName()+" "+displayedUser.getLastName());
                holder.message.setText(message.getMessage());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                holder.date.setText(formatter.format(message.getDateCreated()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public class ListMessageHolder extends RecyclerView.ViewHolder {
        CircleImageView senderImage;
        TextView senderName;
        TextView message;
        ImageView symbole;
        TextView date;
        public ListMessageHolder(@NonNull View itemView) {
            super(itemView);
            senderImage=itemView.findViewById(R.id.senderImage);
            senderName=itemView.findViewById(R.id.senderName);
            message=itemView.findViewById(R.id.message);
            symbole=itemView.findViewById(R.id.symbole);
            date=itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG", "onClick: " );
                    Intent intent=new Intent(mContext, ChatActivity.class);
                    intent.putExtra("chatId",getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
