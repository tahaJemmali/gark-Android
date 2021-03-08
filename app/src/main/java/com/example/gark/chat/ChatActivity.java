package com.example.gark.chat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gark.R;


public class ChatActivity extends AppCompatActivity {
Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat=GroupListActivity.chats.get(getIntent().getIntExtra("chatId",-1));
        Log.e("TAG", "onCreate: "+chat.getMessages().get(0).getMessage() );
        setContentView(R.layout.activity_chat);

    }

}