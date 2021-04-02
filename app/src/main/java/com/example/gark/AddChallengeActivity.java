package com.example.gark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.ChallengeType;
import com.example.gark.entites.Match;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.Terrain;
import com.example.gark.entites.User;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddChallengeActivity extends AppCompatActivity implements IRepository {
//////////////////////////IMAGE
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

//////////////VAR
    Challenge challenge;

/////////////Widget
    ImageView challengeImage;
    EditText challengeNameET;
    EditText editDescription;
    EditText prizeET;
    EditText locationET;
    DatePicker startdateET;
    DatePicker enddateET;
    RadioButton teams14;
    RadioButton teams16;
    Spinner typePicker;
    ProgressDialog dialogg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge);
        initUI();
    }

    void initUI(){

    challengeImage=findViewById(R.id.challengeImage);
         challengeNameET=findViewById(R.id.challengeNameET);
         editDescription=findViewById(R.id.editDescription);
         prizeET=findViewById(R.id.prizeET);
        locationET=findViewById(R.id.locationET);
         startdateET=findViewById(R.id.startdateET);
         enddateET=findViewById(R.id.enddateET);
         teams14=findViewById(R.id.teams14);
         teams16=findViewById(R.id.teams16);
         typePicker=findViewById(R.id.typePicker);
        typePicker.setAdapter(new ArrayAdapter<ChallengeType>(this, android.R.layout.simple_spinner_item, ChallengeType.values()));
    }

    boolean validator(){
      if (image.equals("noImage")){
            Toast.makeText(AddChallengeActivity.this,"Please add an Image to the challenge !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (challengeNameET.getText().toString().isEmpty()){
            Toast.makeText(AddChallengeActivity.this,"Please add your challenge name !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (editDescription.getText().toString().isEmpty()){
            Toast.makeText(AddChallengeActivity.this,"Please add your description !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (prizeET.getText().toString().isEmpty()){
            Toast.makeText(AddChallengeActivity.this,"Please give a prize to your challlenge !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (locationET.getText().toString().isEmpty()){
            Toast.makeText(AddChallengeActivity.this,"Please set the challenge location !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (getDateFromDatePicker(startdateET).after(getDateFromDatePicker(enddateET))){
            Toast.makeText(AddChallengeActivity.this,"Start date must not be after the end date !",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!teams14.isChecked() && !teams16.isChecked()){
            Toast.makeText(AddChallengeActivity.this,"Please select the challenge capacity of teams !",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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
                        Toast.makeText(AddChallengeActivity.this,"File size too large !",Toast.LENGTH_SHORT).show();
                    else if (!extensions.contains(GetFileExtension(image_uri)))
                        Toast.makeText(AddChallengeActivity.this,"Please select an image !",Toast.LENGTH_SHORT).show();
                    else
                        challengeImage.setImageURI(image_uri);

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
    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    public void addImage(View view) {
        showImagePickDialog();
    }

    public void addChallenge(View view) {
        if(validator()){
            challenge = new Challenge(challengeNameET.getText().toString(),
                    getDateFromDatePicker(startdateET),
                    getDateFromDatePicker(enddateET),
                    new Date(),
                    teams14.isChecked() ? 14 : 16,
                    (List<Team>)new ArrayList<Team>(),
                    (List<Match>)new ArrayList<Match>(),
                    new Team(),
                    Float.parseFloat(prizeET.getText().toString()),
                    locationET.getText().toString(),
                    editDescription.getText().toString(),
                    image,
                    MainActivity.getCurrentLoggedInUser(),
                   new Terrain(),
                    ChallengeType.valueOf(typePicker.getSelectedItem().toString()),
                    ChallengeState.Pending);
            dialogg = ProgressDialog.show(this, "", "Loading Data ..Wait..", true);
            ChallengeRepository.getInstance().setiRepository(this);
            ChallengeRepository.getInstance().add(this,challenge,null);
        }

    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        Toast.makeText(AddChallengeActivity.this,"Challenge Added Sucessfully !",Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
}