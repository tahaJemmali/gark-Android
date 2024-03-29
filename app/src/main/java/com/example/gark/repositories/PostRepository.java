package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Match;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void add(Context mContext, Post post, ProgressDialog dialog) {
        iRepository.showLoadingButton();
        final String url = iRepository.baseURL + "/add_post";
        Log.e("TAG", "add: "+post );
        JSONObject object = new JSONObject();
        convertObjectToJson(object,post);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "add post message: "+response.getString("message"));
                            iRepository.doAction();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            iRepository.dismissLoadingButton();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void delete(Context mContext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mContext, Post post, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_posts", null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
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
                        //    iRepository.dismissLoadingButton();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void likePost( Context mContext,String postId,String userId){
        String url = IRepository.baseURL + "/like_post"+"/"+postId+"/"+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.e("TAG", "onResponse: "+message );
                    } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public void disLikePost(Context mContext,String postId,String userId){
        String url = IRepository.baseURL + "/dislike_post"+"/"+postId+"/"+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.e("TAG", "onResponse: "+message );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public void viewPost(Context mContext,String postId,String userId){
        String url = IRepository.baseURL + "/view_post"+"/"+postId+"/"+userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.e("TAG", "onResponse: "+message );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findByCreator(Context mContext, String id) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByCreatorpost/"+id, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
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
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void findById(Context mContext,String id) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Post convertJsonToObject(JSONObject object) {
        try {
            List<User> likes = new ArrayList<>();
            List<User> views = new ArrayList<>();

            JSONArray jsonArrayLikes = object.getJSONArray("likes");
            JSONArray jsonArrayViews = object.getJSONArray("views");

            for (int i = 0; i < jsonArrayLikes.length(); i++) {
                likes.add(new User(jsonArrayLikes.getString(i)));
            }

            for (int i = 0; i < jsonArrayViews.length(); i++) {
                views.add(new User(jsonArrayViews.getString(i)));
            }
            return new Post(object.getString("_id"),
                object.getString("title"),
                object.getString("description"),
                object.getString("image"),
                UserRepository.getInstance().convertJsonToObject((JSONObject)object.get("creator")),
                CRUDRepository.getDate(object.getString("date_created")) ,
                    likes,
                views
                );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object,Post post) {
        JSONArray jsonArrayLikes = new JSONArray();
        JSONArray jsonArrayView = new JSONArray();
        if (post.getViews()!=null){
            for (User row:post.getViews()){
                jsonArrayView.put(row.getId());
            }
        }else {
            jsonArrayView.put(MainActivity.getCurrentLoggedInUser().getId());
        }
        if (post.getLikes()!=null){
            for (User row:post.getLikes()){
                jsonArrayLikes.put(row.getId());
            }
        }else {
            jsonArrayLikes.put(MainActivity.getCurrentLoggedInUser().getId());
        }

        try {
            object.put("title", post.getTitle());
            object.put("image", post.getImage());
            object.put("description", post.getDescription());
            object.put("likes", jsonArrayLikes);
            object.put("views", jsonArrayView);
            object.put("creator", post.getCreator().getId());
            object.put("date_created", new Date());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
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

    @Override
    public Post convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }

    @Override
    public Post getElement() {
        return null;
    }
}
