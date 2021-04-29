package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gark.MainActivity;
import com.example.gark.chat.Chat;
import com.example.gark.chat.Message;
import com.example.gark.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository{
    private IRepository iRepository;
    private static MessageRepository instance;
    public  ArrayList<Message> messages;
    private static final String COLLECTION_NAME = "messages";
    public static User user;
    DocumentReference documentReference;
    public static MessageRepository getInstance() {
        if (instance==null){
            instance = new MessageRepository();
        }
        return instance;
    }


    public void add(Context mcontext, Message message) {
        iRepository.showLoadingButton();
        documentReference.collection(COLLECTION_NAME).add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                iRepository.dismissLoadingButton();
                iRepository.doAction();
            }
        });
    }


    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }


    public void update(Context mcontext, Message message, String id) {

    }


    public void getAll( Chat chat) {
        iRepository.showLoadingButton();
        documentReference.collection(COLLECTION_NAME).orderBy("dateCreated", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.e("TAG", "onSuccess: LIST EMPTY");
                        } else {
                            messages = (ArrayList<Message>) documentSnapshots.toObjects(Message.class);
                           // UserRepository.getInstance().setiRepository((IRepository) mContext);
                            for (Message row :messages){
                                if (row.getreciverId().equals(chat.getUser1().getId())){
                                    row.setreciverId(chat.getUser1().getId());
                                    row.setsenderId(chat.getUser2().getId());
                                }
                                else{
                                    row.setreciverId(chat.getUser2().getId());
                                    row.setsenderId(chat.getUser1().getId());
                                }
                            }
                        }
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }}). addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Error getting data!!!");
                        }
                    });
    }


    public void findById(Context mContext, String userid) {

    }


    public Message convertJsonToObject(JSONObject object) {
        return null;
    }


    public JSONObject convertObjectToJson(JSONObject object, Message message) {
        return null;
    }


    public ArrayList<Message> getList() {
        return messages;
    }


    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    public void setiDocument(DocumentReference document) {
        this.documentReference = document;
    }


    public Message convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }

    public Message getElement() {
        return null;
    }


}
