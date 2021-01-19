package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SkillsRepository implements CRUDRepository<Skills> {
    private IRepository iRepository;
    private static SkillsRepository instance;
    public static  ArrayList<Skills> players;

    public static SkillsRepository getInstance() {
        if (instance==null)
            instance = new SkillsRepository();
        return instance;
    }
    @Override
    public void add(Context mcontext, Skills skills, ProgressDialog dialog) {
        final String url = iRepository.baseURL + "/add_skills";
        JSONObject object = new JSONObject();
        try {
            object.put("pace", skills.getPace());
            object.put("shooting", skills.getShooting());
            object.put("passing", skills.getPassing());
            object.put("dribbling", skills.getDribbling());
            object.put("defending", skills.getDefending());
            object.put("physical", skills.getPhysical());
            object.put("score", skills.getScore());
            object.put("goals", skills.getGoals());
            object.put("role", skills.getRole().toString());
            object.put("player", skills.getPlayer().getId());
            object.put("nationality", skills.getNationality().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "added skill id: "+response.getString("message"));
                            getAll(mcontext,dialog);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail: "+error);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Skills skills, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/top_players", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           players=new ArrayList<Skills>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("topPlayers");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                players.add( convertJsonToObject(jsonTag));
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
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/top_players");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public Skills convertJsonToObject(JSONObject jsonTag) {
        try {
        return new Skills(jsonTag.getString("_id"),
                (byte)jsonTag.getInt("pace"),
                (byte)jsonTag.getInt("shooting"),
                (byte)jsonTag.getInt("passing"),
                (byte)jsonTag.getInt("dribbling"),
                (byte)jsonTag.getInt("defending"),
                (byte)jsonTag.getInt("physical"),
                (byte)jsonTag.getInt("score"),
                (byte)jsonTag.getInt("goals"),
                Role.valueOf(jsonTag.getString("role")),
                UserRepository.getInstance().convertJsonToObject((JSONObject)jsonTag.get("player")),
                (Nationality.valueOf(jsonTag.getString("nationality"))),jsonTag.getInt("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(Skills skills) {
        return null;
    }

    @Override
    public ArrayList<Skills> getList() {
        if (players==null)
            players = new ArrayList<Skills>();
        return players;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }
}
