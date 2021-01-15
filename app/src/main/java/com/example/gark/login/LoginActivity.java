package com.example.gark.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gark.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {
//UI
CircularProgressButton login;
    //VAR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }
    void initUI(){
       login = (CircularProgressButton) findViewById(R.id.btn_id);


    }

    public void login(View view) {
        login.startAnimation();
//[do some async task. When it finishes]
//You can choose the color and the image after the loading is finished
        //login.doneLoadingAnimation(fillColor, bitmap);

//[or just revert de animation]
        //  login.revertAnimation();
    }

    public void forgetPassword(View view) {
        Log.e("TAG", "forgetPassword: " );
    }

    public void signUp(View view) {
        Log.e("TAG", "signUp: " );
    }
}