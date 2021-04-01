package com.example.gark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.ProfileListAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.entites.Post;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.login.LoginActivity;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.TeamRepository;
import com.example.gark.repositories.UserRepository;
import com.example.gark.tutorial.DescriptionActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


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
    Boolean generated = false;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    Uri image_uri = null;
    String image = "noImage";
    private List<String> extensions = Arrays.asList("png", "jpg", "jpeg", "tif", "tiff", "bmp");

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
        showImagePickDialog();
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        /////posts
        if(!generated){
            posts= PostRepository.getInstance().getList();
            postAdapter = new PostAdapter(this, posts);
            recycleViewPosts.setAdapter(postAdapter);
            generated=true;
        }

    }
    
    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }



    public void settings(View view) {
    Intent intent= new Intent(this,SettingActivity.class);
    startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
            }

            try {
                InputStream is = getContentResolver().openInputStream(image_uri);
                Bitmap image = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                long lengthbmp = imageInByte.length;

                if (((float)lengthbmp / (1024 * 1024))>2.0)
                    Toast.makeText(ProfileActivity.this,"File size too large !",Toast.LENGTH_SHORT).show();
                else if (!extensions.contains(GetFileExtension(image_uri)))
                    Toast.makeText(ProfileActivity.this,"Please select an image !",Toast.LENGTH_SHORT).show();
                else
                    profileImage.setImageURI(image_uri);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void sendImage(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        this.image = Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT);
        //update image
        User user = MainActivity.getCurrentLoggedInUser();

        if (!this.image.equals("noImage"))
            user.setPhoto(this.image);

        UserRepository.getInstance().setiRepository(this);
        UserRepository.getInstance().updateUser(user,this);
    }

    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);

    }
    void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (which == 0) {
                    //camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    //gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

}
