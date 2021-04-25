package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.MainActivity;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.chat.Chat;

import com.example.gark.chat.Message;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

public class ChatRepository implements CRUDRepository<Chat> {
    private IRepository iRepository;
    private static ChatRepository instance;
    public static  ArrayList<Chat> chats;
    public static  Chat chat;
    public static CollectionReference myFireBaseDB;
    private static final String COLLECTION_NAME = "chats";

    public static ChatRepository getInstance() {
        if (instance==null){
            myFireBaseDB = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
            instance = new ChatRepository();
        }
        return instance;
    }



    @Override
    public void add(Context mcontext, Chat chat, ProgressDialog dialog) {

    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Chat chat, String id) {

    }
    public void getAllChatsFromFireBase(Context mContext,ArrayList<Chat> chats){
        MessageRepository.getInstance().setiRepository((IRepository) mContext);
        for (Chat chat1:chats) {
            myFireBaseDB.document(chat1.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            //Log.e("LOGGER","First "+document.toString());
                            MessageRepository.getInstance().setiDocument( myFireBaseDB.document(chat1.getId()));
                                MessageRepository.getInstance().getAll(mContext, chat1);


                        } else {
                            Log.e("LOGGER", "No such document");
                        }
                    } else {
                        Log.e("LOGGER", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_chats/"+ MainActivity.getCurrentLoggedInUser().getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chats=new ArrayList<Chat>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("chats");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Chat tmp=convertJsonToObject(object);
                                chats.add(tmp);
                            }
                            getAllChatsFromFireBase(mContext,chats);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_chats");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void findById(Context mContext, String id) {

    }

    public Date getDate(String key){
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
            date = format.parse(key);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public Chat convertJsonToObject(JSONObject object) {
        try {
          /*  ArrayList<String> messages= new ArrayList<String>();
            JSONArray jsonArrayTitulares = object.getJSONArray("messages");
            for (int i = 0; i < jsonArrayTitulares.length(); i++) {
                String message = jsonArrayTitulares.getString(i);
                messages.add(message);
            }*/
            Date date = this.getDate(object.getString("date_created"));
            return new Chat(object.getString("_id"),
                    UserRepository.getInstance().convertJsonToObject((JSONObject)object.get("user1")),
                    UserRepository.getInstance().convertJsonToObject((JSONObject)object.get("user2")),
                    date);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object, Chat chat) {
        return null;
    }

    @Override
    public ArrayList<Chat> getList() {
        return chats;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    @Override
    public Chat convertJsonToObjectDeepPopulate(JSONObject object) {
        return null;
    }

    @Override
    public Chat getElement() {
        if (chat==null)
            chat = new Chat();
        return chat;
    }
}
