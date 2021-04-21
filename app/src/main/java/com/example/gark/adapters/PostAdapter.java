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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.entites.Post;
import com.example.gark.entites.Team;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private final Context mContext;
    private final ArrayList<Post> posts;
    Post post;
    public static Post selectedPost;
    public PostAdapter(Context mContext, ArrayList<Post> posts) {
        this.mContext = mContext;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.top_post_single,parent,false);
        return new PostAdapter.PostHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostHolder holder, int position) {
        post = posts.get(position);
        Bitmap bitmap = getBitmapFromString(post.getCreator().getPhoto());
        holder.creatorImage.setImageBitmap(bitmap);
         bitmap = getBitmapFromString(post.getImage());
        holder.imageContent.setImageBitmap(bitmap);
        holder.creatorName.setText(post.getCreator().getFirstName()+" "+post.getCreator().getLastName() );
        if (post.getLikes().size()>=1000)
        {
            holder.views.setText((post.getLikes().size()/1000)+"K"+mContext.getString(R.string.likes));
        }else {
            holder.views.setText(post.getLikes().size()+mContext.getString(R.string.likes));
        }
        holder.post_title.setText(post.getTitle() );

    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        CircleImageView creatorImage;
        ImageView imageContent;
        TextView creatorName,views,post_title;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            creatorImage = itemView.findViewById(R.id.creatorImage);
            creatorName = itemView.findViewById(R.id.creatorName);
            views = itemView.findViewById(R.id.views);
            post_title = itemView.findViewById(R.id.post_title);
            imageContent= itemView.findViewById(R.id.imageContent);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, StoryActivity.class);
                    selectedPost=posts.get(getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
