package com.example.gark.tutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import com.example.gark.AddChallengeActivity;
import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Ressource;
import com.example.gark.entites.RessourceType;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.User;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.RessourceRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.UserRepository;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DescriptionActivity extends AppCompatActivity implements IRepository {
    SearchableSpinner nationalityPicker;
    SearchableSpinner rolePicker;
    ImageView nationalityImage;
    ImageView userImage;
    EditText description;
    ProgressDialog dialogg;
    EditText height;
    EditText weight;
    ImageView TeamInternation, TeamNational, PlayerNational, PlayerTunisia;
    SearchableSpinner bestTeamInternationalSpinner;
    SearchableSpinner bestTeamTunisiaSpinner;
    SearchableSpinner bestPlayerInternationalSpinner;
    SearchableSpinner bestPlayerTunisiaSpinner;
    //selected item
    Nationality selectedNationality;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;
    boolean updateUser = false;
    boolean addSkills = false;
    boolean getClubs = false;
    boolean getPlayers = false;
    ArrayList<Ressource> clubs;
    ArrayList<Ressource> players;

    Uri image_uri = null;
    String image = "noImage";
    private List<String> extensions = Arrays.asList("png", "jpg", "jpeg", "tif", "tiff", "bmp");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initUI();
    }

    public void procede(View view) {
        ///send data
        if (validator()) {
            //update image
            User user = MainActivity.getCurrentLoggedInUser();
            user.setPhoto(image);

            UserRepository.getInstance().setiRepository(this);
            updateUser = true;
            UserRepository.getInstance().updateUser(user, this);


        }
    }

    boolean validator() {
        if (image.equals("noImage")) {
            Toast.makeText(this, "Please put your image image don't be shy", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (height.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please put your height", Toast.LENGTH_SHORT).show();
        }
        if (weight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please put your weight", Toast.LENGTH_SHORT).show();
        }
        try {
            Float.parseFloat(height.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Please chose a valid value for prize !", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            Float.parseFloat(weight.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Please chose a valid value for prize !", Toast.LENGTH_LONG).show();
            return false;
        }

        if (description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please describe yourself in a few words!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void initUI() {
        dialogg = ProgressDialog.show(this, "", "Loading", true);
        //init permissions arrays
        RessourceRepository.getInstance().setiRepository(this);
        RessourceRepository.getInstance().getAll(this, RessourceType.club);
        getClubs = true;
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        nationalityPicker = findViewById(R.id.nationalityPicker);
        nationalityPicker.setTitle("Select your Country");
        nationalityPicker.setPositiveButton("OK");
        rolePicker = findViewById(R.id.rolePicker);
        rolePicker.setTitle("Select your position");
        rolePicker.setPositiveButton("OK");
        nationalityImage = findViewById(R.id.nationalityImage);
        userImage = findViewById(R.id.userImage);
        description = findViewById(R.id.description);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        TeamInternation = findViewById(R.id.TeamInternation);
        TeamNational = findViewById(R.id.TeamNational);
        PlayerNational = findViewById(R.id.PlayerNational);
        PlayerTunisia = findViewById(R.id.PlayerTunisia);

        bestTeamInternationalSpinner = findViewById(R.id.bestTeamInternationalSpinner);
        bestTeamInternationalSpinner.setTitle("Select your best team worldwide");
        bestTeamInternationalSpinner.setPositiveButton("OK");


        bestTeamTunisiaSpinner = findViewById(R.id.bestTeamTunisiaSpinner);
        bestTeamTunisiaSpinner.setTitle("Select your best team in tunisia");
        bestTeamTunisiaSpinner.setPositiveButton("OK");


        bestPlayerInternationalSpinner = findViewById(R.id.bestPlayerInternationalSpinner);
        bestPlayerInternationalSpinner.setTitle("Select your best player worldwide");
        bestPlayerInternationalSpinner.setPositiveButton("OK");


        bestPlayerTunisiaSpinner = findViewById(R.id.bestPlayerTunisiaSpinner);
        bestPlayerTunisiaSpinner.setTitle("Select your best player in tunisia");
        bestPlayerTunisiaSpinner.setPositiveButton("OK");

        ArrayList<String> nationalites = new ArrayList<String>();
        String tmp = "";
        for (Nationality row : Nationality.values()) {
            tmp = row.toString().replace("_", " ");
            tmp = tmp.substring(0, 1).toUpperCase() + tmp.substring(1).toLowerCase();
            nationalites.add(tmp);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, nationalites);
        nationalityPicker.setAdapter(adapter);
        rolePicker.setAdapter(new ArrayAdapter<Role>(this, android.R.layout.simple_spinner_item, Role.values()));
        nationalityPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNationality = Nationality.values()[position];
                nationalityImage.setImageResource(getResources().getIdentifier(String.valueOf(Nationality.values()[position]), "drawable", getPackageName()));

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
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

                if (((float) lengthbmp / (1024 * 1024)) > 2.0)
                    Toast.makeText(DescriptionActivity.this, "File size too large !", Toast.LENGTH_SHORT).show();
                else if (!extensions.contains(GetFileExtension(image_uri)))
                    Toast.makeText(DescriptionActivity.this, "Please select an image !", Toast.LENGTH_SHORT).show();
                else
                    userImage.setImageURI(image_uri);

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
        this.image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
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
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
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
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
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

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        if (getPlayers) {
            getPlayers = false;
            players = RessourceRepository.getInstance().getList();
            ArrayList<String> list = new ArrayList<String>();
            for (Ressource row : players) {
                list.add(row.getFirstName() + " " + row.getLastName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            bestPlayerInternationalSpinner.setAdapter(adapter);
            bestPlayerInternationalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String urll = players.get(bestPlayerInternationalSpinner.getSelectedItemPosition()).getImage();
                    Picasso.get().load(urll).into(PlayerNational);
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            bestPlayerTunisiaSpinner.setAdapter(adapter);
            bestPlayerTunisiaSpinner.setSelection(2165);
            bestPlayerTunisiaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String urll = players.get(bestPlayerTunisiaSpinner.getSelectedItemPosition()).getImage();
                    Picasso.get().load(urll).into(PlayerTunisia);

                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if (getClubs) {
            getClubs = false;
            getPlayers = true;
            clubs = RessourceRepository.getInstance().getList();
            ArrayList<String> list = new ArrayList<String>();
            for (Ressource row : clubs) {
                list.add(row.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            bestTeamInternationalSpinner.setAdapter(adapter);
            bestTeamInternationalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String urll = clubs.get(bestTeamInternationalSpinner.getSelectedItemPosition()).getImage();
                    Picasso.get().load(urll).into(TeamInternation);
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            bestTeamTunisiaSpinner.setAdapter(adapter);
            bestTeamTunisiaSpinner.setSelection(5368);
            bestTeamTunisiaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String urll = clubs.get(bestTeamTunisiaSpinner.getSelectedItemPosition()).getImage();
                    Picasso.get().load(urll).into(TeamNational);
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            RessourceRepository.getInstance().getAll(this, RessourceType.player);
        }

        if (updateUser) {
            //add skills
            Skills skills = new Skills(Role.valueOf(rolePicker.getSelectedItem().toString()), MainActivity.getCurrentLoggedInUser());
            skills.setNationality(selectedNationality);
            skills.setBestTeamWorld(clubs.get(bestTeamInternationalSpinner.getSelectedItemPosition()));
            skills.setBestTeamTunisia(clubs.get(bestTeamTunisiaSpinner.getSelectedItemPosition()));
            skills.setBestPlayerWorld(players.get(bestPlayerInternationalSpinner.getSelectedItemPosition()));
            skills.setBestPlayerTunisia(players.get(bestPlayerTunisiaSpinner.getSelectedItemPosition()));
            DecimalFormat df = new DecimalFormat("#.00");
            skills.setHeight(Float.parseFloat(height.getText().toString()));
            skills.setWeight(Float.parseFloat(weight.getText().toString()));

            skills.setDescription(description.getText().toString());
            SkillsRepository.getInstance().setiRepository(this);
            SkillsRepository.getInstance().add(this, skills, null);
            addSkills = true;
            updateUser = false;
        }

        //Intent
        if (addSkills) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }



    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

    public void addImage(View view) {
        showImagePickDialog();
    }
}
