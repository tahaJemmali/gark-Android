package com.example.gark.repositories;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeType;
import com.example.gark.entites.Match;
import com.example.gark.entites.Team;
import com.example.gark.entites.Terrain;
import com.example.gark.entites.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChallengeRepository {
    private IRepository iRepository;
    private static ChallengeRepository instance;
    public static  ArrayList<Challenge> challenges;
    public static  Challenge challenge;

    public static ChallengeRepository getInstance() {
        if (instance==null){
            instance = new ChallengeRepository();
        }
        return instance;
    }

    public void add(Context mcontext, Challenge challenge, ProgressDialog dialog) {

    }

    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    public void update(Context mcontext, Challenge challenge, String id) {

    }

    public void addChallengeToChallenge(Context mcontext, String challenge_id, String Challenge_id){

    }


    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_challenges", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            challenges=new ArrayList<Challenge>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("challenges");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                challenges.add( convertJsonToObject(jsonTag));

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
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_challenges");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findByName(Context mContext, String challengeType) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByName/"+challengeType, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "onResponse: "+response.toString() );
                        challenge=convertJsonToObject(response);
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL +  "/findByName/"+challengeType);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findById(Context mContext, String id) {

    }


    public Challenge convertJsonToObject(JSONObject object) {
        try {
            List<Team> teams = new ArrayList<Team>();

            if (object.has("teams")){
                JSONArray jsonArrayTeams = object.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new Team( jsonArrayTeams.getString(i)));
                }
            }
            Team winner=null;
            if (!object.getString("winner").equals("null")){
                winner=TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("winner"));
            }

            Log.e("TAG", "convertJsonToObject: "+ object.getString("name"));
            return new Challenge(object.getString("name"),
                    getDate(object.getString("start_date")),
                    getDate(object.getString("end_date")),
                    getDate(object.getString("date_created")),
                    object.getInt("maxNumberOfTeams"),
                    teams,
                    null,
                    winner,
                    object.getInt("prize"),
                    object.getString("location"),
                    object.getString("description"),
                    object.getString("image"),
                    new User(object.getString("creator")),
                            new Terrain(object.getString("terrain")),
                            ChallengeType.valueOf(object.getString("type")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public JSONObject convertObjectToJson(JSONObject object, Challenge challenge) {
        return null;
    }


    public ArrayList<Challenge> getList() {
        return challenges;
    }


    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    public Challenge convertJsonToObjectDeepPopulate(JSONObject object) {
        try {
            List<Team> teams = new ArrayList<Team>();

            if (object.has("teams")){
                JSONArray jsonArrayTeams = object.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new TeamRepository().convertJsonToObject((JSONObject) jsonArrayTeams.get(i)));
                }
            }
            Team winner=null;
            if (!object.getString("winner").equals("null")){
                winner=TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("winner"));
            }
            Log.e("TAG", "convertJsonToObject: "+ object.getString("name"));
            return new Challenge(object.getString("name"),
                    getDate(object.getString("start_date")),
                    getDate(object.getString("end_date")),
                    getDate(object.getString("date_created")),
                    object.getInt("maxNumberOfTeams"),
                    teams,
                    null,
                    winner,
                    object.getInt("prize"),
                    object.getString("location"),
                    object.getString("description"),
                    object.getString("image"),
                    new User(object.getString("creator")),
                    new Terrain(object.getString("terrain")),
                    ChallengeType.valueOf(object.getString("type")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public Challenge getElement() {
        return challenge;
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
}
