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
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TeamRepository implements CRUDRepository<Team> {
    private IRepository iRepository;
    private static TeamRepository instance;
    public static  ArrayList<Team> teams;

    public static TeamRepository getInstance() {
        if (instance==null)
            instance = new TeamRepository();
        return instance;
    }


    @Override
    public void add(Context mcontext, Team team, ProgressDialog dialog) {

    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Team team, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_teams", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teams=new ArrayList<Team>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("teams");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                teams.add( convertJsonToObject(jsonTag));

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
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_teams");
            }
        });
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public Team convertJsonToObject(JSONObject jsonTag) {
        try {
            List<User> titulares = new ArrayList<>();
            List<User> substitutes = new ArrayList<>();
            JSONArray jsonArrayTitulares = jsonTag.getJSONArray("titulares");
            for (int i = 0; i < jsonArrayTitulares.length(); i++) {
                JSONObject titulaire = jsonArrayTitulares.getJSONObject(i);
                titulares.add(UserRepository.getInstance().convertJsonToObject(titulaire));
            }
            JSONArray jsonArraySubstitutes = jsonTag.getJSONArray("substitutes");
            for (int i = 0; i < jsonArraySubstitutes.length(); i++) {
                JSONObject substitute = jsonArrayTitulares.getJSONObject(i);
                substitutes.add(UserRepository.getInstance().convertJsonToObject(substitute));
            }

            return new Team(jsonTag.getString("_id"),
                    jsonTag.getString("name"),
                    jsonTag.getString("image"),
                    UserRepository.getInstance().convertJsonToObject((JSONObject)jsonTag.get("capitaine")),
                    titulares,
                    substitutes,
                    jsonTag.getInt("victories"),
                    jsonTag.getInt("defeats"),
                    jsonTag.getInt("points"),
                    jsonTag.getInt("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(Team team) {
        return null;
    }

    @Override
    public ArrayList<Team> getList() {
        if (teams==null)
            teams = new ArrayList<Team>();
        return teams;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }
}
