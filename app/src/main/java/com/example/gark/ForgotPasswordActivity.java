package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.gark.login.LoginActivity;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.UserRepository;


public class ForgotPasswordActivity extends AppCompatActivity implements IRepository {

    ScrollView scrollView;
    Button nextBtn;
    EditText edtEmail;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        UserRepository.getInstance().setiRepository(this);

        initUI();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getInstance().setiRepository(this);
    }

    public void initUI(){
        pb = new ProgressDialog(this);
        scrollView  = findViewById(R.id.scrollView);
        nextBtn  = findViewById(R.id.nextBtn);
        edtEmail  = findViewById(R.id.edtEmail);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEmail.getText().toString().equals("")){
                    sendCode(edtEmail.getText().toString());
                }
                else
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.validator_email_empty),Toast.LENGTH_SHORT).show();
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }

    private void sendCode(String email) {
        pb.setMessage("");
        pb.show();
        UserRepository.getInstance().passwordRecovery(email,this);
    }

    @Override
    public void showLoadingButton() {
        pb.dismiss();
    }

    @Override
    public void doAction() {
        pb.dismiss();
        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void dismissLoadingButton() {

    }

}