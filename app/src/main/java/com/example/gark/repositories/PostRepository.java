package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Post;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostRepository implements CRUDRepository<Post>  {
    private IRepository iRepository;
    private static PostRepository instance;
    public static  ArrayList<Post> posts;

    public static PostRepository getInstance() {
        if (instance==null)
            instance = new PostRepository();
        return instance;
    }

    @Override
    public void add(Context mcontext, Post post, ProgressDialog dialog) {

    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Post post, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_posts", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            posts=new ArrayList<Post>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("posts");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                posts.add( convertJsonToObject(jsonTag));
                            }
                            iRepository.doAction();
                            iRepository.dismissLoadingButton();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_posts");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Post convertJsonToObject(JSONObject object) {
        try {
         return new Post(object.getString("_id"),
                object.getString("title"),
                object.getString("description"),
                object.getString("image"),
                UserRepository.getInstance().convertJsonToObject((JSONObject)object.get("creator")),
                object.getInt("likes"),
                CRUDRepository.getDate(object.getString("date_created")) ,
                object.getInt("views")
                );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(Post post) {
        return null;
    }

    @Override
    public ArrayList<Post> getList() {
        if (posts==null)
            posts = new ArrayList<Post>();
        return posts;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }
}
