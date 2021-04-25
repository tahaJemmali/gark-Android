package com.example.gark.chat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.R;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity  implements IRepository {
    //UI
    EditText search_bar_messenger;
    RecyclerView chatRecyclerView;
    ProgressDialog dialogg;
    //Var
    ChatAdapter listMessageAdapter;
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
        dialogg = ProgressDialog.show(this, "", getString(R.string.loading), true);
        ChatRepository.getInstance().setiRepository(this);
        ChatRepository.getInstance().getAll(this,dialogg);
    }
    void reloadData(){
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listMessageAdapter = new ChatAdapter(this,chats );
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
                    row.getMessages().add(key);
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