package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.ProfileListAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Team;
import com.example.gark.login.LoginActivity;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.TeamRepository;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;


public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, IRepository {
    protected HeaderView toolbarHeaderView;
    protected HeaderView floatHeaderView;
    protected AppBarLayout appBarLayout;
    protected Toolbar toolbar;
    //taskList Adapter
    ProfileListAdapter profileListAdapter;
    PostAdapter postAdapter;
    //UI
    RecyclerView rvProfile;
    ImageView profileImage;
    ImageButton addPost;
    ProgressDialog dialogg;

    private boolean isHideToolbarView = false;
    ArrayList<Integer> icons;
    ArrayList<String> titles;
    ArrayList<String> descriptions;

    //Recycler View
    public static ArrayList<Post> posts;
    //Recycler View
    RecyclerView recycleViewPosts;
    TeamsAdapter teamsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        toolbarHeaderView = findViewById(R.id.toolbar_header_view);
        floatHeaderView = findViewById(R.id.float_header_view);
        appBarLayout = findViewById(R.id.appbar);
        rvProfile = findViewById(R.id.rvProfile);
        profileImage = findViewById(R.id.profileImage);
        addPost=findViewById(R.id.addPost);
        recycleViewPosts = findViewById(R.id.recycleViewPosts);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,AddPostActivity.class);
                startActivity(intent);
            }
        });
        dialogg = ProgressDialog.show(this
                , "","Loading Data ..Wait.." , true);
        if (MainActivity.getCurrentLoggedInUser().getPhoto().startsWith("/")) {
            Bitmap bitmap = getBitmapFromString(MainActivity.getCurrentLoggedInUser().getPhoto());
            profileImage.setImageBitmap(bitmap);
        }
        else if (!MainActivity.getCurrentLoggedInUser().getPhoto().equals("Not mentioned")){
            Picasso.get().load(MainActivity.getCurrentLoggedInUser().getPhoto()).into(profileImage);
        }
        else {
            Picasso.get().load("https://graph.facebook.com/10214899562601635/picture?height=1024").into(profileImage);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            w.setStatusBarColor(Color.BLACK);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(MainActivity.getCurrentLoggedInUser().getFirstName()+"'s profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //posts
        posts=new ArrayList<Post>();
        PostRepository.getInstance().setiRepository(this);
        PostRepository.getInstance().findByCreator(this,MainActivity.getCurrentLoggedInUser().getId());

        initUi();
    }
    private void initUIRecycleViewerPosts() {

        recycleViewPosts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        postAdapter = new PostAdapter(this, posts);
        recycleViewPosts.setAdapter(postAdapter);
    }

    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void initUi() {
        setArrays();
        rvProfile.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        profileListAdapter = new ProfileListAdapter(this,icons,titles,descriptions);
        rvProfile.setAdapter(profileListAdapter);
        initUIRecycleViewerPosts();
    }

    private void setArrays() {
        icons = new ArrayList<>();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        icons.add(R.drawable.ic_default_user);
        titles.add("Name");
        descriptions.add(MainActivity.getCurrentLoggedInUser().getFirstName()+" "+MainActivity.getCurrentLoggedInUser().getLastName());

        icons.add(R.drawable.ic_baseline_mail_outline_24);
        titles.add("Email");
        descriptions.add(MainActivity.getCurrentLoggedInUser().getEmail());

    //    if (!MainActivity.getCurrentLoggedInUser().getAddress().equals("Not mentioned")){
            icons.add(R.drawable.ic_baseline_location_on_24);
            titles.add("Home Town");
            descriptions.add(MainActivity.getCurrentLoggedInUser().getAddress());
      //  }

        //if (!MainActivity.getCurrentLoggedInUser().getPhone().equals("Not mentioned")){
            icons.add(R.drawable.ic_baseline_phone_24);
            titles.add("Mobile");
            descriptions.add(MainActivity.getCurrentLoggedInUser().getPhone());
        //}
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(MainActivity.getCurrentLoggedInUser().getSign_up_date());
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String month_name = month_date.format(calendar.getTime());
            String dateTime2 = month_name+" "+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.YEAR);
            icons.add(R.drawable.ic_baseline_join);
            titles.add("Member since");
            descriptions.add(dateTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    public void addImage(View view) {
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        /////posts
        posts= PostRepository.getInstance().getList();
        postAdapter = new PostAdapter(this, posts);
        recycleViewPosts.setAdapter(postAdapter);
    }
    
    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }



    public void settings(View view) {
    Intent intent= new Intent(this,SettingActivity.class);
    startActivity(intent);
    }
}