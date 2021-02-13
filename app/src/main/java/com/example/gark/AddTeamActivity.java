package com.example.gark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.entites.Categorie;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTeamActivity extends AppCompatActivity implements IRepository {
    Spinner nationalityPicker;
    Spinner categoriePicker;
    ImageView nationalityImage;
    ImageView userImage;
    ProgressDialog dialogg;
    EditText TeamName;
    EditText editDescription;
    //selected item
    Nationality selectedNationality;
    Skills currentUserSkills;
    Boolean addTeamClicked=false;
    ArrayList<Skills> returnInvited;
    RecyclerView teamMemberRecyclerView;
    TopPlayersAdapter topPlayersAdapter;

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
    Team team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        initUI();
    }

    ///send data
    void initUI() {
        returnInvited=new ArrayList<Skills>();
        teamMemberRecyclerView=findViewById(R.id.teamMemberRecyclerView);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        nationalityPicker=findViewById(R.id.nationalityPicker);
        categoriePicker=findViewById(R.id.categoriePicker);
        nationalityImage=findViewById(R.id.nationalityImage);
        userImage=findViewById(R.id.userImage);
        TeamName=findViewById(R.id.TeamName);
        editDescription=findViewById(R.id.editDescription);
        teamMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //get skills of current user
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().findPlayerById(this,MainActivity.getCurrentLoggedInUser().getId());

        ArrayList<String> nationalites = new ArrayList<String>();
        String tmp = "";
        for (Nationality row : Nationality.values()) {
            tmp = row.toString().replace("_", " ");
            tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1).toLowerCase();
            nationalites.add(tmp);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, nationalites);
            nationalityPicker.setAdapter(adapter);
            categoriePicker.setAdapter(new ArrayAdapter<Categorie>(this, android.R.layout.simple_spinner_item, Categorie.values()));
            nationalityPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedNationality = Nationality.values()[position];
                    nationalityImage.setImageResource(getResources().getIdentifier(String.valueOf(Nationality.values()[position]), "drawable", getPackageName()));

                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    boolean validator(){
        if (image.equals("noImage")){
            return false;
        }
        if(TeamName.getText().toString().isEmpty())
            return false;
        if (editDescription.getText().toString().isEmpty())
            return false;
        return true;
    }

    @Override
    public void showLoadingButton() {
        dialogg = ProgressDialog.show(this, "", "Fetching data ...", true);
        dialogg.show();
    }

    @Override
    public void doAction() {

        currentUserSkills=SkillsRepository.getInstance().getElement();
        if(addTeamClicked)
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                returnInvited= (ArrayList<Skills>) data.getSerializableExtra("result");
                topPlayersAdapter = new TopPlayersAdapter(this,returnInvited);
                teamMemberRecyclerView.setAdapter(topPlayersAdapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    else {
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
                    Toast.makeText(AddTeamActivity.this,"File size too large !",Toast.LENGTH_SHORT).show();
                else if (!extensions.contains(GetFileExtension(image_uri)))
                    Toast.makeText(AddTeamActivity.this,"Please select an image !",Toast.LENGTH_SHORT).show();
                else
                    userImage.setImageURI(image_uri);

                sendImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

    public void addImage(View view) {
        showImagePickDialog();
    }

    public void addTeam(View view) {

        if (validator()){
            team = new Team();
            team.setImage(image);
            team.setCapitaine(currentUserSkills);
            team.setSubstitutes(null);
            team.setTitulares(null);
            team.setName(TeamName.getText().toString());
            team.setDescription(editDescription.getText().toString());
            if (returnInvited.size()>0)
                team.setTitulares(returnInvited);
            //add team
            team.setNationality(selectedNationality);
            team.setCategorie(Categorie.valueOf(categoriePicker.getSelectedItem().toString()));
            addTeamClicked=true;
            TeamRepository.getInstance().setiRepository(this);
            TeamRepository.getInstance().add(this,team,null);

        }else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("EROOR")
                    .setMessage("Please add team image and fill the form")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void inviteMembers(View view) {
        int LAUNCH_SECOND_ACTIVITY = 1;
        Intent i = new Intent(this, AddTeamMembersActivity.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
    }
}