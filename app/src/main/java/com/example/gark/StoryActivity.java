package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gark.adapters.PostAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.User;
import com.example.gark.fragments.AcceuilFragment;
import com.example.gark.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    //Var
    int counter = 0;
    ArrayList<Post> posts;
    Post post;
    int numberOfStories=0;
    int clickCount = 0;
    //variable for storing the time of first click
    long startTime;
    //variable for calculating the total time
    long duration;
    //constant for defining the time duration between the click that can be considered as double-tap
    static final int MAX_DURATION = 500;
    //UI
    StoriesProgressView storiesProgressView;
    ImageView image;
    CircleImageView story_photo;
    TextView story_username;
    LinearLayout r_seen;
    TextView seen_number;
    LinearLayout likes;
    TextView likes_number;
    View mainView;
    View skip;
    View reverse;
    ImageView bigLike;
    //Animation
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        initUI();
    }

    public void initUI(){
        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        bigLike = findViewById(R.id.bigLike);
        story_photo = findViewById(R.id.story_photo);
        story_username = findViewById(R.id.story_username);
        r_seen = findViewById(R.id.r_seen);
        seen_number = findViewById(R.id.seen_number);
        likes = findViewById(R.id.likes);
        likes_number = findViewById(R.id.likes_number);
        mainView = findViewById(R.id.mainView);
        reverse = findViewById(R.id.reverse);
        skip = findViewById(R.id.skip);
        post=new Post();
//get intent
        if(getIntent().hasExtra("size")){
            posts= AcceuilFragment.posts;
            showView(counter);
            numberOfStories=posts.size();
            reverse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storiesProgressView.reverse();
                }
            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storiesProgressView.skip();

                }
            });
        }else if(getIntent().hasExtra("sizeTen")){
            posts= AcceuilFragment.topTen;
            showView(counter);
            numberOfStories=posts.size();
            reverse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storiesProgressView.reverse();
                }
            });

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storiesProgressView.skip();

                }
            });
        }
        else{
            post = PostAdapter.selectedPost;
            numberOfStories=1;
            //set content
            Bitmap bitmap = getBitmapFromString(post.getCreator().getPhoto());
            story_photo.setImageBitmap(bitmap);
            bitmap = getBitmapFromString(post.getImage());
            image.setImageBitmap(bitmap);
            story_username.setText(post.getCreator().getFirstName()+" "+post.getCreator().getLastName());
            seen_number.setText(""+post.getViews().size());
            likes_number.setText(""+post.getLikes().size());
        }
        //set story
        storiesProgressView.setStoriesCount(numberOfStories); // <- set stories
        storiesProgressView.setStoryDuration(5000L); // <- set a story duration
        storiesProgressView.setStoriesListener(StoryActivity.this); // <- set listener
        storiesProgressView.startStories(counter); // <- start progress
      //  counter++;
        boolean alreadySeenPost = false;
        for (User u : post.getViews()){
            if (u.getId().equals(MainActivity.getCurrentLoggedInUser().getId()))
            {
                alreadySeenPost=true;
            }
        }
        if (!alreadySeenPost){
            PostRepository.getInstance().viewPost(StoryActivity.this,post.getId(),MainActivity.getCurrentLoggedInUser().getId());
            post.getViews().add(MainActivity.getCurrentLoggedInUser());
            seen_number.setText(""+post.getViews().size());
        }

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        clickCount++;
                        storiesProgressView.pause();
                        return true;
                    case MotionEvent.ACTION_UP:
                        long time = System.currentTimeMillis() - startTime;
                        duration=  duration + time;
                        if(clickCount == 2)
                        {
                            if(duration<= MAX_DURATION)
                            {
                                boolean exist= false;
                                    for (User u : post.getLikes()){
                                        if (u.getId().equals(MainActivity.getCurrentLoggedInUser().getId()))
                                        {
                                            exist=true;
                                        }
                                    }
                                if(!exist){
                                    post.getLikes().add(MainActivity.getCurrentLoggedInUser());
                                    PostRepository.getInstance().likePost(StoryActivity.this,post.getId(),MainActivity.getCurrentLoggedInUser().getId());
                                    final Drawable drawable = bigLike.getDrawable();
                                    bigLike.setAlpha(0.85f);
                                    if(drawable instanceof AnimatedVectorDrawableCompat){
                                        avd = (AnimatedVectorDrawableCompat) drawable;
                                        avd.start();
                                    }else if (drawable instanceof  AnimatedVectorDrawable){
                                        avd2 = (AnimatedVectorDrawable) drawable;
                                        avd2.start();
                                    }
                                }else{
                                    PostRepository.getInstance().disLikePost(StoryActivity.this,post.getId(),MainActivity.getCurrentLoggedInUser().getId());
                                    for (User u : post.getLikes()){
                                        if (u.getId().equals(MainActivity.getCurrentLoggedInUser().getId()))
                                        {
                                            post.getLikes().remove(u);
                                        }
                                    }
                                    post.getLikes().remove(MainActivity.getCurrentLoggedInUser());
                                    Toast.makeText(StoryActivity.this, "You disliked this post", Toast.LENGTH_SHORT).show();
                                }
                                likes_number.setText(""+post.getLikes().size());
                            }
                            clickCount = 0;
                            duration = 0;
                        }
                        storiesProgressView.resume();
                        return true;
                }
                return true;
            }
        });


        r_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StoryActivity.this, "Views", Toast.LENGTH_SHORT).show();
            }
        });
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StoryActivity.this, "Likes", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onNext() {
        counter++;
        if(counter<posts.size()){
            showView(counter);
        }else {
            counter=posts.size()-1;
        }

    }

    @Override
    public void onPrev() {
        counter--;
        if(counter>=0){
            showView(counter);
        }else {
            counter=0;
        }
    }

    @Override
    public void onComplete() {
        finish();
    }

    public void showView(int i){
        Log.e("TAG", "onNext: "+i );
        //set content
        post=posts.get(i);
        Bitmap bitmap = getBitmapFromString(posts.get(i).getCreator().getPhoto());
        story_photo.setImageBitmap(bitmap);
        bitmap = getBitmapFromString(posts.get(i).getImage());
        image.setImageBitmap(bitmap);
        story_username.setText(posts.get(i).getCreator().getFirstName()+" "+posts.get(i).getCreator().getLastName());
        seen_number.setText(""+posts.get(i).getViews().size());
        likes_number.setText(""+posts.get(i).getLikes().size());
    }
    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    public void getBack(View view) {
        super.onBackPressed();
    }
}