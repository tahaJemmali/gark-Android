package com.example.gark.chat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.MessageRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity  implements IRepository {
    //UI
    EditText search_bar_messenger;
    RecyclerView chatRecyclerView;
    ProgressDialog dialogg;
    //Var
    ChatAdapter listMessageAdapter;
    public static ArrayList<Chat> chats;
    ArrayList<Chat> chats_search;
    ArrayList<Message> messages;
    boolean getAllMessagesDone=false;
    private static final String COLLECTION_NAME = "messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages=new ArrayList<Message>();
        chats=new ArrayList<Chat>();
        chats_search=new ArrayList<Chat>();
        setContentView(R.layout.activity_group_list);
        search_bar_messenger=findViewById(R.id.search_bar_messenger);
        search_bar_messenger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                find(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
    void find(CharSequence charSequence){
        chats_search.clear();
        if(charSequence.toString().isEmpty()){
           reloadData();
        }else {
            for (Chat row : chats){
                if (row.getUser1().equals(MainActivity.getCurrentLoggedInUser())){
                    if(row.getUser2().getFirstName().toLowerCase().contains(charSequence)||row.getUser2().getLastName().toLowerCase().contains(charSequence))
                        chats_search.add(row);

                } else {
                    if(row.getUser1().getFirstName().toLowerCase().contains(charSequence)||row.getUser1().getLastName().toLowerCase().contains(charSequence))
                        chats_search.add(row);

                }
            }
            listMessageAdapter = new ChatAdapter(this,chats_search );
            chatRecyclerView.setAdapter(listMessageAdapter);
        }
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
                if(messages.size()>0){
                    for (Message key : messages) {
                        row.getMessages().add(key);
                    }
                }else {

                }
                listenDataChangeMessageRecived(row);
                }
            reloadData();
    }
    public void listenDataChangeMessageRecived(Chat chat){
        final CollectionReference docRef = ChatRepository.myFireBaseDB.document(chat.getId()).collection(COLLECTION_NAME);
        docRef.orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                    return;
                }
                ArrayList<Message> tmp = (ArrayList<Message>) value.toObjects(Message.class);
                if(messages.size()!=tmp.size()){
                    chat.getMessages().clear();
                    for (Message key : tmp) {
                        chat.getMessages().add(key);
                    }
                    reloadData();
                }
            }
        });
    }


    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }

    public void refresh(View view) {
        reloadData();
    }
}