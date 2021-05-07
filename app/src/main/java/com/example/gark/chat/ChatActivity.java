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
import com.example.gark.PlayerProfileActvity;
import com.example.gark.R;
import com.example.gark.entites.User;
import com.example.gark.fragments.AcceuilFragment;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


public class ChatActivity extends AppCompatActivity implements IRepository{
    Chat chat;
    User sender;
    User reciever;
    MessageAdapter messageAdapter;
    boolean firstMessage=false;
    private static final String COLLECTION_NAME = "messages";
    //UI
    ProgressDialog dialogg;
    EditText textInput;
    RecyclerView messageRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        textInput = findViewById(R.id.textInput);
        messageRV = findViewById(R.id.messageRV);
        if(getIntent().hasExtra("chatIdSingle")){
            chat = PlayerProfileActvity.chat;
            firstMessage=true;
        }else {
            chat = AcceuilFragment.chats.get(getIntent().getIntExtra("chatId", -1));
        }
        if(Objects.isNull(chat.getMessages()))
        chat.setMessages(new ArrayList<>());
        sender = MainActivity.getCurrentLoggedInUser();
        if (!chat.getUser1().equals(sender))
            reciever = chat.getUser1();
        else
            reciever = chat.getUser2();
        if(chat.getMessages().size()>0)
        if(Objects.isNull(chat.getMessages().get(0).getDateCreated()))
            chat.getMessages().clear();

        initMessageRecyclerView();
        MessageRepository.getInstance().setiRepository(this);
        if(!Objects.isNull(chat.getId()))
        listenDataChangeMessageRecived(chat);
    }


    public void listenDataChangeMessageRecived(Chat chat) {
        final CollectionReference docRef = ChatRepository.myFireBaseDB.document(chat.getId()).collection(COLLECTION_NAME);
        docRef.orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                } else {
                    Log.e("TAG," ,"CHAT ACTIVITY DATA CHANGE: " );
                    messageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    void initMessageRecyclerView() {
        messageRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        messageAdapter = new MessageAdapter(this, chat.getMessages(), reciever);
        messageRV.setAdapter(messageAdapter);
    }

    public void sendMessage(View view) {
        if(firstMessage){
            ChatRepository.getInstance().setiRepository(this);
            ChatRepository.getInstance().add(this,chat,null);
        }else {

            Message message = new Message();
            message.setsenderId(sender.getId());
            message.setreciverId(reciever.getId());
            message.setMessage(textInput.getText().toString());
            message.setUrlImage("none");
            message.setDateCreated(new Date(System.currentTimeMillis()));
            chat.getMessages().add(0, message);
            textInput.getText().clear();
            MessageRepository.getInstance().add(this, message);
        }
    }

    @Override
    public void showLoadingButton() {
        dialogg = ProgressDialog.show(this, "", getString(R.string.loading), true);
    }

    @Override
    public void doAction() {
        if(dialogg.isShowing())
            dialogg.dismiss();
        if(firstMessage){
            chat=ChatRepository.getInstance().getElement();
            Message message = new Message();
            message.setsenderId(sender.getId());
            message.setreciverId(reciever.getId());
            message.setMessage(textInput.getText().toString());
            message.setUrlImage("none");
            message.setDateCreated(new Date(System.currentTimeMillis()));
            chat.getMessages().add(0, message);
            textInput.getText().clear();
            MessageRepository.getInstance().add(this, message);
            listenDataChangeMessageRecived(chat);
            firstMessage=false;
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}