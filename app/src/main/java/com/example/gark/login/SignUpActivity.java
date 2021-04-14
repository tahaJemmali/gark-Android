package com.example.gark.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.User;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.UserRepository;
import com.example.gark.tutorial.WelcomeActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity  implements IRepository {
    //UI
    ScrollView scrollView;
    Button signUpBtn;
    EditText edtFistName,edtLastName,edtEmail,edtPassword,edtConfirmPassword,edtAddress,edtPhone;
    ProgressDialog dialogg;
    DatePicker birthDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        UserRepository.getInstance().setiRepository(this);
        //InitUI
        initUI();

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }



    public void initUI(){
        scrollView  = findViewById(R.id.scrollView);
        signUpBtn  = findViewById(R.id.signUpBtn);
        birthDatePicker=findViewById(R.id.birthDate);
        edtFistName  = findViewById(R.id.edtFistName);
        edtLastName  = findViewById(R.id.edtLastName);
        edtEmail  = findViewById(R.id.edtEmail);
        edtPassword  = findViewById(R.id.edtPassword);
        edtConfirmPassword  = findViewById(R.id.edtConfirmPassword);
        edtAddress  = findViewById(R.id.edtAddress);
        edtPhone  = findViewById(R.id.edtPhone);
        edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(edtFistName.getText().toString(),
                        edtLastName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        edtConfirmPassword.getText().toString(),
                        edtAddress.getText().toString(),
                        edtPhone.getText().toString(),
                        getDateFromDatePicker(birthDatePicker)
                );
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    void registerUser(String firstName, String lastName, String email, String password, String confirmPassword, String address, String phone, Date birthDate){
        String namePattern = "^[\\p{L} .'-]+$";

        if (TextUtils.isEmpty(firstName))
        {
            Toast.makeText(SignUpActivity.this,"First name cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }
        if (birthDate.equals(new Date()))
        {
            Toast.makeText(SignUpActivity.this,"Select your birth date you can't be born today !",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(lastName))
        {
            Toast.makeText(SignUpActivity.this,"Last name cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (!firstName.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid first name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!lastName.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid last name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(SignUpActivity.this,"Email cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z][a-z]+";
        if (!email.matches(emailPattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid email address!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length()<8)
        {
            Toast.makeText(SignUpActivity.this,"Password must have at least 8 characters!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(SignUpActivity.this,"Password  cannot be empty!",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(address)){
            address="Not mentioned";
        } else if (!address.matches(namePattern))
        {
            Toast.makeText(SignUpActivity.this,"Invalid home town!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            phone="Not mentioned";
        }else if (phone.length()!=8)
        {
            Toast.makeText(SignUpActivity.this,"Invalid phone number!",Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhone(phone);
        user.setPassword(password);
        user.setBirth_date(birthDate);

        //register
        UserRepository.getInstance().register(user,this);

    }

    @Override
    public void showLoadingButton() {
        dialogg = ProgressDialog.show(this, "", "Registration in progress  ...", true);

    }

    @Override
    public void doAction() {
        Toast.makeText(this,"Registration succeeded please verify your mail !",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }

}