package com.example.gark.chat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.adapters.AddMatchAdapter;
import com.example.gark.entites.User;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;


public class ChatActivity extends AppCompatActivity implements IRepository {
Chat chat;
User sender;
User reciever;
MessageAdapter messageAdapter;
    private static final String COLLECTION_NAME = "messages";
//UI
ProgressDialog dialogg;
    EditText textInput;
    RecyclerView messageRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        textInput=findViewById(R.id.textInput);
        messageRV=findViewById(R.id.messageRV);
        chat=GroupListActivity.chats.get(getIntent().getIntExtra("chatId",-1));
        sender=MainActivity.getCurrentLoggedInUser();
        if(!chat.getUser1().equals(sender))
        reciever=chat.getUser1();
                else
            reciever=chat.getUser2();
        initMessageRecyclerView();
        newMessageListener();
    }
    void newMessageListener(){
        final DocumentReference docRef = ChatRepository.myFireBaseDB.document(chat.getId()).collection(COLLECTION_NAME).document();
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
    }

    void initMessageRecyclerView(){
        messageRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        messageAdapter = new MessageAdapter(this, chat.getMessages(),reciever);
        messageRV.setAdapter(messageAdapter);
    }
    public void sendMessage(View view) {
       MessageRepository.getInstance().setiRepository(this);
        Message message = new Message();
        message.setsenderId(sender.getId());
        message.setreciverId(reciever.getId());
        message.setMessage(textInput.getText().toString());
        message.setUrlImage("none");
        message.setDateCreated(new Date(System.currentTimeMillis()));
        chat.getMessages().add(0,message);
        textInput.getText().clear();
        MessageRepository.getInstance().add(this,message);
    }

    @Override
    public void showLoadingButton() {
        dialogg = ProgressDialog.show(this, "",getString(R.string.loading) , true);
    }

    @Override
    public void doAction() {
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}