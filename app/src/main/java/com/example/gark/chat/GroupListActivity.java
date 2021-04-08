package com.example.gark.chat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.User;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;
import com.example.gark.repositories.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity  implements IRepository {
    //UI
    EditText search_bar_messenger;
    RecyclerView chatRecyclerView;
    ProgressDialog dialogg;
    //Var
    ListMessageAdapter listMessageAdapter;
    public static ArrayList<Chat> chats;
    ArrayList<Message> messages;
    boolean getAllMessagesDone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages=new ArrayList<Message>();
        chats=new ArrayList<Chat>();
        setContentView(R.layout.activity_group_list);
        search_bar_messenger=findViewById(R.id.search_bar_messenger);
        chatRecyclerView=findViewById(R.id.chatRecyclerView);
        dialogg = ProgressDialog.show(this, "", "Loading Data ..Wait..", true);
        ChatRepository.getInstance().setiRepository(this);
        ChatRepository.getInstance().getAll(this,dialogg);
    }
    void reloadData(){
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listMessageAdapter = new ListMessageAdapter(this,chats );
        chatRecyclerView.setAdapter(listMessageAdapter);
    }

    @Override
    public void showLoadingButton() {
    dialogg.show();
    }

    public void getBack(View view) {
        super.onBackPressed();
    }

    @Override
    public void doAction() {

            if (chats.isEmpty()){
                chats=ChatRepository.getInstance().getList();
            }

            messages=MessageRepository.getInstance().getList();
            for (Chat row:chats) {
                for (Message key : messages) {
                    if (!row.getMessages().contains(key)) {
                        if (key.getChatId().equals(row.getId())) {
                                row.getMessages().add(key);
                            }
                        }
                    }
                }
            reloadData();
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }

    public void refresh(View view) {
        reloadData();
    }
}