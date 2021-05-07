package com.example.gark.chat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.entites.User;
import com.example.gark.fragments.AcceuilFragment;
import com.example.gark.repositories.ChatRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity{
    //UI
    EditText search_bar_messenger;
    RecyclerView chatRecyclerView;
   // ProgressDialog dialogg;
    //Var
    ChatAdapter listMessageAdapter;
    ArrayList<Chat> chats_search;
    ArrayList<Message> messages;
    private static final String COLLECTION_NAME = "messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messages=new ArrayList<Message>();
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
for(Chat row : AcceuilFragment.chats){
    listenDataChangeMessageRecived(row);
}
        reloadData();

    }
    void reloadData(){
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listMessageAdapter = new ChatAdapter(this,AcceuilFragment.chats );
        chatRecyclerView.setAdapter(listMessageAdapter);
    }
    void find(CharSequence charSequence){
        chats_search.clear();
        if(charSequence.toString().isEmpty()){
           reloadData();
        }else {
            for (Chat row : AcceuilFragment.chats){
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

    public void getBack(View view) {
        super.onBackPressed();
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
                Log.e("TAG," ,"GROUP LIST DATA CHANGE: " );
                    reloadData();
            }
        });
    }




    public void refresh(View view) {
        reloadData();
    }
}