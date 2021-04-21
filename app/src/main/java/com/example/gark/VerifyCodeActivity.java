package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.fraggjkee.smsconfirmationview.SmsConfirmationView;


public class VerifyCodeActivity extends AppCompatActivity {
    public static String code;
    SmsConfirmationView code_view;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        initUI();
    }

    public void initUI(){
        code_view = findViewById(R.id.code_view);
        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code_view.getEnteredCode().equals(code)){
                    Intent intent = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(VerifyCodeActivity.this, getString(R.string.wrong_verification_code),Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }
}